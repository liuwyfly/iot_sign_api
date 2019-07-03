package com.iotdiandian.signapi.beiwei;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;

public class BwSignToken {
	private String appId = "jbfkfxzcrmxbmlxdaxvz";
	private String secret = "mxnxjffxdjvzsbn";
	private static final Logger logger = LogManager.getLogger(BwSignToken.class);
	
	public String signToken(Object req) throws UnsupportedEncodingException {
		Date now = new Date();
		JWTCreator.Builder builder = JWT.create();
		builder.withIssuer(appId);//iss(即appId,申请的接口口登入入账号)
		builder.withIssuedAt(now);//iat
		builder.withNotBefore(new Date(now.getTime() - 1000 * 5 * 60));//ntf
		builder.withExpiresAt(new Date(now.getTime() + 1000 * 5 * 60));//exp
		builder.withClaim("nonce", this.getRandomStr(16));//nonce
		//req为请求接口口参数
		String reqStr = JSONObject.toJSONString(req, SerializerFeature.SortField);
		logger.info(reqStr);
		String hash = this.getSHA256Str(reqStr);
		builder.withClaim("hash", hash); //hash
		builder.withSubject("bwiot-openapi"); //sub
		
		return builder.sign(Algorithm.HMAC256(secret));//secret为申请的接口口登入入密钥
	}
	
	public String getSHA256Str(String str){
		MessageDigest messageDigest;
		String encodeStr = "";
		try{
		messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
		encodeStr = byte2Hex(messageDigest.digest());
		} catch (NoSuchAlgorithmException e){
		e.printStackTrace();
		}
		return encodeStr;
	}
	
	public String byte2Hex(byte[] bytes){
		StringBuffer buffer = new StringBuffer();
		String temp;

		for(byte aByte : bytes){
			temp = Integer.toHexString(aByte & 0xFF);
			if(temp.length() == 1){
				buffer.append("0");
			}
			buffer.append(temp);
		}
		return buffer.toString();
	}

	public String getRandomStr(int length){
		String str="zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
		Random random=new Random();
		StringBuilder sb=new StringBuilder();
		for(int i = 0; i< length; ++i){
			int number=random.nextInt(length);
			sb.append(str.charAt(number));
		}
		return sb.toString();
	}
}
