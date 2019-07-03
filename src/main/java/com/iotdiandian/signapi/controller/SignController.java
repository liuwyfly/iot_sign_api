package com.iotdiandian.signapi.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.iotdiandian.signapi.beiwei.BwSignToken;

@RestController
public class SignController {
	@RequestMapping("/test_json")
    public String testJson() {
        return "{\"msg\": \"hello\"}";
    }
	
	@RequestMapping(value = "/sign/beiwei", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String signBeiwei2(HttpServletRequest request) {
		BwSignToken bwSign = new BwSignToken();
		String reqStr = this.getRequestInput(request);
		JSONObject jObj = JSONObject.parseObject(reqStr);
		String ret = null;
		try {
			ret = bwSign.signToken(jObj);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			ret = e.toString();
		}
		return ret;
    }

	private String getRequestInput(HttpServletRequest request) {
		try {
	        // 获取输入流
	        BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));

	        // 写入数据到Stringbuilder
	        StringBuilder sb = new StringBuilder();
	        String line = null;
	        while ((line = streamReader.readLine()) != null) {
	            sb.append(line);
	        }
	        
	        return sb.toString();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

		return null;
	}
}
