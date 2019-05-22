package com.kunteng.cyria.auth.controller;

import com.kunteng.cyria.auth.domain.User;
import com.kunteng.cyria.auth.service.UserService;
import com.kunteng.cyria.auth.tools.Tools;
import com.kunteng.cyria.auth.tools.BPwdEncoderUtil;

import org.apache.commons.lang.StringUtils;
//import org.json.JSONObject;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.Base64Codec;
import lombok.extern.slf4j.Slf4j;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
	private static final long EXPIRATIONTIME = 3600000l;
	private static final long OUTEXPIRATIONTIME = 7200000;
	//private static final long EXPIRATIONTIME = 120000l;
	//private static final long OUTEXPIRATIONTIME = 240000l;
	private static final String SECRET = "P@ssw02d";
	private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/current", method = RequestMethod.GET)
	public Principal getUser(Principal principal) {
		return principal;
	}

	@PreAuthorize("#oauth2.hasScope('server')")
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String register(@RequestBody User user) {
		log.info(user.toString());
		if(user.getUsername().contentEquals("admin")) {
			user.setIsAdmin("true");
		}else {
			user.setIsAdmin("false");
		}
		if(!userService.hasUser(user.getUsername())) {
			String hash = encoder.encode(user.getPassword());
			user.setPassword(hash);
			log.info("保存此用户");
			userService.saveUser(user);
			return Tools.fillResultString(0, "", user);
		}else {
			return "{errcode:401, msg:\"hasuser\"}";
		}
	}
	
	@PreAuthorize("#oauth2.hasScope('server')")
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String login(@RequestBody User user) {
		if(userService.hasUser(user.getUsername())) {
			User us = userService.findByUsername(user.getUsername());
			if(!BPwdEncoderUtil.matches(user.getPassword(), us.getPassword())) {
				return "{errcode: 402, msg:\"passworderror\"}";
			} else {
//				if(us.getPassword().equals(user.getPassword())) {
				log.info("userId="+us.getId());
				log.info("getIsAdmin="+us.getIsAdmin());
				String JWT = createToken(us);
				us.SetJwtToken(JWT);
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				us.setLastActTime(df.format(new Date(System.currentTimeMillis())));
				userService.updateUser(us);
			//	return Tools.fillResultString(0, "", us);
				return us.getJwtToken();
			}
		}else {
			return "{errcode: 402, msg:\"nulluser\"}";
		}
	}
	
	@PreAuthorize("#oauth2.hasScope('server')")
	@RequestMapping(value = "/refreshToken", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String refreshToken(@RequestBody String token) {
		Claims claims = null;
		String name = this.getIdByJWT(token);
		User usr = userService.findByUsername(name);
		if(usr == null) {
			return null;
		}
		
		try {
			claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(usr.getJwtToken()).getBody();
		}catch(ExpiredJwtException e) {
			
		}
		
		if(claims == null) {
			log.info("user超时了");
		}else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sDate = sdf.format(claims.getExpiration());
			log.info("getExpiration="+ sDate);
			log.info("getROLE=" + claims.get("authorities"));
			log.info("getID="+ claims.getSubject());
			User us = userService.find(claims.getSubject());
			if(us.getId().equals(claims.getSubject())) {
				log.info("id claims equals");
				log.info("getUserName = "+ us.getUsername());
				try {
					Date dt = (Date) sdf.parse(us.getLastActTime());
					log.info("上次行动时间为："+ sdf.format(dt));
					Date outdt1 = new Date(dt.getTime() + EXPIRATIONTIME);
					log.info("超时时间1为："+ sdf.format(outdt1));
					Date outdt2 = new Date(dt.getTime() + OUTEXPIRATIONTIME);
					log.info("超时时间2为：" + sdf.format(outdt2));
					Date now = new Date();
					log.info("当前时间为："+ sdf.format(now));
					if(outdt2.after(now) && dt.before(now)) {
						log.info("now允许刷新jwt时间");
						if(outdt1.after(now)) {
							log.info("没超过1小时不需要刷新jwt");
							return null;
						}else {
							log.info("超过了1小时需要刷新jwt");
							String JWT = createToken(us);
							us.SetJwtToken(JWT);
							log.info(JWT);
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							us.setLastActTime(df.format(new Date(System.currentTimeMillis())));
							userService.updateUser(us);
							return us.getJwtToken();
						}
					}else {
						log.info("now 不允许刷新jwt时间");
						return null;
					}
				}catch(ParseException e) {
					e.printStackTrace();
				}
			}else {
				log.info("id claims noequals");
				return null;
			}
		}
		return null;
	}
	
	private String getIdByJWT(String jwt) {
		if(!StringUtils.isBlank(jwt)) {
			if(jwt.split("\\.").length == 3) {
				log.info("jwt:"+jwt);
				String[] split = jwt.split("\\.");
				String content = split[1];
				String s = Base64Codec.BASE64URL.decodeToString(content);
				JSONObject jsonObject = JSONObject.fromObject(s);
				User user = (User)JSONObject.toBean(jsonObject, User.class);
				return user.getUsername();
			}
		}
		return null;
	}
	
	public String createToken(User us) {
		String JWT = "";
		if(us.getIsAdmin().equals("true")) {
			JWT = Jwts.builder()
					.claim("authorities","ROLE_ADMIN")
					.setSubject(us.getId())
					.setExpiration(new Date(System.currentTimeMillis()+ OUTEXPIRATIONTIME))
					.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		} else {
			JWT = Jwts.builder()
					.claim("authorities","ROLE_NORMAL")
					.setSubject(us.getId())
					.setExpiration(new Date(System.currentTimeMillis() + OUTEXPIRATIONTIME))
					.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		}
		return JWT;
	}
	
	@PreAuthorize("#oauth2.hasScope('server')")
	@RequestMapping(value = "/getJWTState", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String getJWTState(String jwtToken) {
		Claims claims = null;
		try {
			claims = Jwts.parser()
					.setSigningKey(SECRET)
					.parseClaimsJws(jwtToken).getBody();
		}catch(ExpiredJwtException e) {
			
		}
		if(claims == null) {
			log.info("jwt超时了");
			return "outtime";
		}else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dt = (Date) claims.getExpiration();
			log.info("jwt超时时间为："+ sdf.format(dt));
			Date outdt = new Date(dt.getTime() - EXPIRATIONTIME);
			log.info("jwt需刷新时间为："+ sdf.format(outdt));
			Date now = new Date();
			log.info("当前时间为："+ sdf.format(now));
			if(now.after(outdt)) {
				return "refresh";
			}else {
				return "intime";
			}
		}
	}
	
	public enum TokenState{
		intime, refresh, outtime;
	}
	
	@PreAuthorize("#oauth2.hasScope('server')")
	@RequestMapping(method = RequestMethod.POST)
	public void createUser(@Valid @RequestBody User user) {
		System.out.println("user.password="+user.getPassword());
		userService.create(user);
	}
}
