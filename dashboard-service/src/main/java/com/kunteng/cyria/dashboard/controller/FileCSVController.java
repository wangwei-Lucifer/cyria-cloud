package com.kunteng.cyria.dashboard.controller;

import java.io.IOException;
import java.util.Map;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kunteng.cyria.dashboard.domain.RawCSV;
import com.kunteng.cyria.dashboard.service.FileCSVService;
import com.kunteng.cyria.dashboard.utils.CommonResult;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import com.kunteng.cyria.dashboard.repository.FinalCSVRepository;
import com.kunteng.cyria.dashboard.domain.FinalCSV;
import com.kunteng.cyria.dashboard.domain.TitleCell;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Slf4j
@RestController
public class FileCSVController {
	
	@Autowired
	private FileCSVService fileCSVService;

	@Autowired
	private FinalCSVRepository finalCSVRepository;	

	@RequestMapping(path = "/material/csv", method = RequestMethod.POST)
	public CommonResult uploadFileCSV(@RequestParam("file") MultipartFile file) throws IOException{
		return fileCSVService.uploadFileCSV(file);
	}

	@RequestMapping(path = "/material/list", method = RequestMethod.GET)
	public CommonResult getCSVList(@RequestParam Map<String,Object> map){
		return fileCSVService.getCSVList(map);
	}
	
	@RequestMapping(path = "/material/{hash}/save", method = RequestMethod.POST)
	public CommonResult saveCSVTitle(@PathVariable String hash, @RequestBody RawCSV rawCSV){
		return fileCSVService.saveCSVTitle(hash, rawCSV);
	}
	
	@RequestMapping(path = "/material/{hash}/view", method = RequestMethod.GET)
	public CommonResult viewCSVByHash(@PathVariable String hash) {
		return fileCSVService.viewCSVByHash(hash);
	}
	
	@RequestMapping(path = "/material/{hash}/delete", method = RequestMethod.DELETE)
	public CommonResult deleteCSVByHash(@PathVariable String hash) {
		return fileCSVService.deleteCSVByHash(hash);
	}
	
	@RequestMapping(path = "/material/{hash}/update", method = RequestMethod.POST)
	public CommonResult updateCSVData(@PathVariable String hash, MultipartFile file) throws IOException{
		return fileCSVService.updateCSVData(hash, file);
	}
	
	@RequestMapping(path = "/material/titleList", method = RequestMethod.GET)
	public CommonResult getTitleList() {
		return fileCSVService.getTitleList();
	}
	
	@RequestMapping(path = "/material/{hash}/cancel", method = RequestMethod.GET)
	public CommonResult cancelCSV(@PathVariable String hash) {
		return fileCSVService.cancelCSV(hash);
	}

	@RequestMapping(value = "/material/jsonapi", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String jsonapi(@RequestParam(defaultValue = "") String groups, @RequestParam(defaultValue = "") String values,
			@RequestParam(defaultValue = "") String type, @RequestParam(defaultValue = "") String suuid,
			@RequestParam(defaultValue = "") String source) {
                boolean manyparam=false;
		log.info("RequestParam groups:" + groups);
		log.info("RequestParam values:" + values);
		log.info("RequestParam type :" + type);
		log.info("RequestParam suuid:" + suuid);
		log.info("RequestParam source:" + source);

        String srcgroups = "";
		String srcvalues = "";
		try {
			BASE64Decoder Base64 = new BASE64Decoder();
			srcgroups = new String(Base64.decodeBuffer(groups), "utf8");
			srcvalues = new String(Base64.decodeBuffer(values), "utf8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("RequestParam srcgroups:" + srcgroups);
		log.info("RequestParam srcvalues:" + srcvalues);
                FinalCSV fv = finalCSVRepository.findByHash(source);
                if(fv==null){
                   JSONObject jsob = new JSONObject();
                   jsob.put("statusCode", 1);
                   return jsob.toString();
                }
                ArrayList<TitleCell> list=fv.getTitle();
                for(TitleCell tv : list){
                  log.info("TV title:" + tv.getTitle());
                }
		if(srcvalues!=null) {
			if (srcvalues.split("-").length != 1) {
				manyparam=true;
			}
			if(type.equals("uuid6")) {
				manyparam=true;
			}
			if(type.equals("uuid7")) {
				manyparam=true;
			}
		}
		log.info("RequestParam manyparam:" + manyparam);//多参数标记如果此标记不为""则进入1:M的处理流程
                if (srcgroups.split("-").length == 1) {
                  ArrayList<String> strs1 = getCsvRaw(fv, srcgroups);
			if (manyparam==false) {//如果manyparam为false的话则是 柱状图，折线区域图，堆叠柱状图，玫瑰图，省份分布图 中的一种以UUID进行区分
                  		ArrayList<String> strs2 = getCsvRaw(fv, srcvalues);
				log.info("RequestParam srcgroups:" + srcgroups);
				log.info("RequestParam srcvalues:" + srcvalues);
				JSONObject jsob = new JSONObject();
                                jsob.put("statusCode", 0);
				int len = strs1.size();
				if (len >= 12) {
					len = 12;
				}
				if (type.equals("uuid1")) {//柱状图1:1
					jsob.put("code", 0);
					JSONObject data = new JSONObject();
					JSONArray ja = new JSONArray();
					for (int i = 0; i < len; i++) {
						JSONObject jo = new JSONObject();
						jo.put("x", strs1.get(i));
						jo.put("y", strs2.get(i));
						ja.add(jo);
					}
					data.put("statistics", ja);
					jsob.put("data", data);
				}  if (type.equals("uuid2")) {//折线区域图1:1
					jsob.put("code", 0);
					JSONObject data = new JSONObject();
					JSONArray ja = new JSONArray();
					for (int i = 0; i < len; i++) {
						JSONObject jo = new JSONObject();
						jo.put("x", strs1.get(i));
						jo.put("y", strs2.get(i));
						ja.add(jo);
					}
					data.put("statistics", ja);
					jsob.put("data", data);
				}  if (type.equals("uuid3")) {//堆叠柱状图1:1
					jsob.put("code", 0);
					JSONObject data = new JSONObject();
					JSONArray ja = new JSONArray();
					for (int i = 0; i < len; i++) {
						JSONObject jo = new JSONObject();
						jo.put("x", strs1.get(i));
						jo.put("y", strs2.get(i));
						ja.add(jo);
					}
					data.put("statistics", ja);
					jsob.put("data", data);
				}  if (type.equals("uuid4")) {//玫瑰图1:1
					jsob.put("code", 0);
					JSONObject data = new JSONObject();
					JSONArray ja = new JSONArray();
					for (int i = 0; i < len; i++) {
						JSONObject jo = new JSONObject();
						jo.put("x", strs1.get(i));
						jo.put("y", strs2.get(i));
						ja.add(jo);
					}
					data.put("statistics", ja);
					jsob.put("data", data);
				}  if (type.equals("uuid5")) {//地图省份分布图1:1
					jsob.put("code", 0);
					JSONObject data = new JSONObject();
					JSONArray ja = new JSONArray();
					for (int i = 0; i < len; i++) {
						JSONObject jo = new JSONObject();
						jo.put("name", strs1.get(i));
						jo.put("value", strs2.get(i));
						ja.add(jo);
					}
					data.put("china", ja);
					jsob.put("data", data);
				}  if (type.equals("uuid8")) {//地图分布散点图1:1的情况
					jsob.put("code", 0);
					JSONObject data = new JSONObject();
					JSONArray ja = new JSONArray();
					for (int i = 0; i < len; i++) {
						JSONObject jo = new JSONObject();
						jo.put("name", strs1.get(i));
						JSONArray joa= new JSONArray();
						String value=strs2.get(i);
						String pos[]=getCityPos(strs1.get(i));
						joa.add(Double.parseDouble(pos[0]));
						joa.add(Double.parseDouble(pos[1]));
						joa.add(Double.parseDouble(value));
						jo.put("value", joa);
						ja.add(jo);
					}
					data.put("china", ja);
					jsob.put("data", data);
				}
				return jsob.toString();
                  }else{//如果manyparam不为false的话则为折线堆叠图，雷达图，分布散点图其中一种以uuid进行区分
				String arrs[]=srcvalues.split("-");
				JSONObject jsob = new JSONObject();
                                jsob.put("statusCode", 0);
				int len = strs1.size();
				if (len >= 12) {
					len = 12;
				}
				ArrayList<String>[] strsarr = new ArrayList[arrs.length];
				for(int i=0;i<arrs.length;i++) {
					strsarr[i]= getCsvRaw(fv, arrs[i]);
				}
				if (type.equals("uuid6")) {//折线对叠图1:M的情况
					jsob.put("code", 0);
					JSONObject data = new JSONObject();
					JSONObject dataseries= new JSONObject();
					JSONArray categories = new JSONArray();
					for (int i = 0; i < len; i++) {
						categories.add(strs1.get(i));
					}
					dataseries.put("categories", categories);
					JSONArray statistics= new JSONArray();
					for (int i = 0; i < arrs.length; i++) {
						JSONObject jo = new JSONObject();
						jo.put("name", arrs[i]);
						JSONArray ja = new JSONArray();
						for (String s : strsarr[i]) {
							ja.add(Integer.parseInt(s));
						}
						jo.put("data",ja);
						statistics.add(jo);
					}
					dataseries.put("statistics", statistics);
					data.put("dataseries", dataseries);
					jsob.put("data", data);
				}
				if (type.equals("uuid7")) {//雷达图1:M的情况
					jsob.put("code", 0);
					JSONObject data = new JSONObject();
					JSONObject dataseries= new JSONObject();
					JSONArray categories = new JSONArray();
					for (int i = 0; i < len; i++) {
						JSONObject jo = new JSONObject();
						jo.put("name", strs1.get(i));
						//jo.put("max",600);
						categories.add(jo);
					}
					dataseries.put("categories", categories);
					JSONArray statistics= new JSONArray();
					for (int i = 0; i < arrs.length; i++) {
						JSONObject jo = new JSONObject();
						jo.put("name", arrs[i]);
						JSONArray ja = new JSONArray();
						for (String s : strsarr[i]) {
							ja.add(Double.parseDouble(s));
						}
						jo.put("value",ja);
						statistics.add(jo);
					}
					dataseries.put("statistics", statistics);
					data.put("dataseries", dataseries);
					jsob.put("data", data);
				}
				/*if (type.equals("uuid8")) {//地图分布散点图1:M的情况
					jsob.put("code", 0);
					JSONObject data = new JSONObject();
					JSONArray ja = new JSONArray();
					for (int i = 0; i < len; i++) {
						JSONObject jo = new JSONObject();
						jo.put("name", strs1.get(i));
						JSONArray joa= new JSONArray();
						//strsarr to jov;
						String posx=strsarr[0].get(i);
						String posy=strsarr[1].get(i);
						String value=strsarr[2].get(i);
						joa.add(Double.parseDouble(posx));
						joa.add(Double.parseDouble(posy));
						joa.add(Double.parseDouble(value));
						jo.put("value", joa);
						ja.add(jo);
					}
					data.put("china", ja);
					jsob.put("data", data);
				}*/
				return jsob.toString();
                  }
                }else{// 如果srcgroups长度不为1的话则是图表控件按图表控件标准进行返回即可
			String arrs[]=srcgroups.split("-");
			ArrayList<String>[] strsarr = new ArrayList[arrs.length];
			for(int i=0;i<arrs.length;i++) {
				strsarr[i]= getCsvRaw(fv, arrs[i]);
			}
			int len = strsarr[0].size();
			if (len >= 12) {
				len = 12;
			}
			JSONObject jsob = new JSONObject();
                        jsob.put("statusCode", 0);
			if (type.equals("uuid9")) {//表格M:0的情况
				jsob.put("status", 0);
				JSONObject data = new JSONObject();
				JSONArray columns= new JSONArray();
				//生成columns数据
				for(int i=0;i<arrs.length;i++) {
					JSONObject jo = new JSONObject();
					jo.put("name", arrs[i]);
					jo.put("id","vl"+i);
					columns.add(jo);
				}
				data.put("columns", columns);
				//生成row数据
				JSONArray rows = new JSONArray();
				for (int i = 0; i < len; i++) {
					JSONObject jo = new JSONObject();
					//fill jo by forloop
					for(int j=0;j<strsarr.length;j++) {
						String vlx=strsarr[j].get(i);
						jo.put("vl"+j, vlx);
					}
					rows.add(jo);
				}
				data.put("rows", rows);
				jsob.put("data", data);
			}
			return jsob.toString();
                }
                /*
                ArrayList<Map<String,ArrayList<String>>> data=fv.getData();
                for(Map<String,ArrayList<String>> mp : data){
                   ArrayList<String> slist=mp.get("负责人");
                   String val=slist.get(1);
                   System.out.println("Val:" + val);
                }*/
                //return "ok"+"-"+srcgroups+"-"+srcgroups;
                //return "err";
        }
	public ArrayList<String> getCsvRaw(FinalCSV fv, String rowtabname) {
		ArrayList<String> strs = new ArrayList<String>();
                ArrayList<Map<String,ArrayList<String>>> data=fv.getData();
                for(Map<String,ArrayList<String>> mp : data){
                   ArrayList<String> slist=mp.get(rowtabname);
                   String val=slist.get(1);
                   System.out.println("Val:" + val);
                   strs.add(val);
                }
                return strs;
        }
	public String[] getCityPos(String s) {
		String [] posdata= {
				"北京","101.78","36.62",
				"天津","127.5","50.25",
				"上海","121.15","31.89",
				"重庆","109.78","39.6",
				"重庆","120","37",
				"河北","122","29",
				"河南","123","47",
				"云南","120","33",
				"辽宁","118","42",
				"黑龙江","120","36",
		};
		String[] ss=new String[2];
		ss[0]="0.0";
		ss[1]="0.0";
		for(int i=0;i<posdata.length/3;i++) {
			if(posdata[i*3].equals(s)) {
				ss[0]=posdata[i*3+1];
				ss[1]=posdata[i*3+2];
			}
		}
		return ss;
	}
}
