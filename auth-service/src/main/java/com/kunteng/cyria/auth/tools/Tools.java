package com.kunteng.cyria.auth.tools;

import org.json.JSONObject;
//login
//curl -H "Content-Type: application/json" -X POST -d '{"username":"admin","password":"123456"}' http://127.0.0.1:8080/login
//reg
//curl -H "Content-Type: application/json" -X POST -d '{"username":"admin","password":"123456"}' http://127.0.0.1:8080/register
//user

public class Tools {
	//JSON相关参数封装到此类便于替换
	public static Object NULL=JSONObject.NULL;
    public static String fillResultString(final Integer status, final String message, final Object result){
    	JSONObject jsonObject = new JSONObject(){{
            put("status", status);
            put("message", message);
            put("result", result);
        }};

        return jsonObject.toString();
    }
}
