package com.kunteng.cyria.dashboard.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;

import com.kunteng.cyria.dashboard.utils.JsonUtil;

public class CommonResult {
    //操作成功
    public static final int SUCCESS = 0;
    //操作失败
    public static final int FAILED = 1;
    //参数校验失败
    public static final int VALIDATE_FAILED = 404;
    //未认证
    public static final int UNAUTHORIZED = 401;
    //未授权
    public static final int  FORBIDDEN = 403;
    private int code;
    private int statusCode;
    private String msg;
    private Object data;
    private String id;
    private String hash;
    private String imgUrl;

    /**
     * 普通成功返回
     *
     * @param data 获取的数据
     */
    public CommonResult success(Object data) {
        this.code = SUCCESS;
        this.msg = "操作成功";
        this.data = data;
        return this;
    }
    
    public CommonResult customHash(String hash) {
        this.code = SUCCESS;
        this.msg = "操作成功";
        this.hash = hash;
        return this;
    }
    
    public CommonResult customUpload(String url) {
        this.code = SUCCESS;
        this.msg = "操作成功";
        this.imgUrl = url;
        return this;
    }

    /**
     * 普通失败提示信息
     */
    public CommonResult failed() {
        this.code = FAILED;
        this.msg = "操作失败";
        return this;
    }
    
    public CommonResult customFailed(String msg) {
    	this.code = 1;
    	this.msg = msg;
    	return this;
    }
    
    public CommonResult customFailed(int StatusCode, String msg) {
    	this.code = SUCCESS;
    	this.statusCode = StatusCode;
    	this.msg = msg;
    	return this;
    }

    /**
     * 参数验证失败使用
     *
     * @param message 错误信息
     */
    public CommonResult validateFailed(String msg) {
        this.code = VALIDATE_FAILED;
        this.msg = msg;
        return this;
    }

    /**
     * 未登录时使用
     *
     * @param message 错误信息
     */
    public CommonResult unauthorized(String message) {
        this.code = UNAUTHORIZED;
        this.msg = "暂未登录或token已经过期";
        this.data = message;
        return this;
    }

    /**
     * 未授权时使用
     *
     * @param message 错误信息
     */
    public CommonResult forbidden(String message) {
        this.code = FORBIDDEN;
        this.msg = "没有相关权限";
        this.data = message;
        return this;
    }

    /**
     * 参数验证失败使用
     * @param result 错误信息
     */
    public CommonResult validateFailed(BindingResult result) {
        validateFailed(result.getFieldError().getDefaultMessage());
        return this;
    }

    @Override
    public String toString() {
        return JsonUtil.objectToJson(this);
    }
    
    public int getStatusCode() {
    	return this.statusCode;
    }
    
    public void setStatusCode(int statusCode) {
    	this.statusCode = statusCode;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String message) {
        this.msg = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getHash() {
		return hash;
	}
	
	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
}
