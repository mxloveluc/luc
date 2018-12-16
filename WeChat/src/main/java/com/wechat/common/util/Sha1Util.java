package com.wechat.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha1Util {

	private final static  String KEY_SHA = "SHA-1";
	
    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",  
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };  

	
	public static String sha1EnCode(String str) {

        // 创建具有指定算法名称的信息摘要  
        MessageDigest sha;
        byte[] bytes = null;
		try {
			sha = MessageDigest.getInstance(KEY_SHA);
	        sha.update(str.getBytes());  
	        // 完成摘要计算  
	        bytes = sha.digest();  
	        // 将得到的字节数组变成字符串返回  

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        // 使用指定的字节数组对摘要进行最后更新  
        return byteArrayToHexString(bytes);  
    }  
	
    private static String byteArrayToHexString(byte[] bytes) {  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < bytes.length; i++) {  
            sb.append(byteToHexString(bytes[i]));  
        }  
        return sb.toString();  
    }  
    private static String byteToHexString(byte b) {  
        int ret = b;  
        //System.out.println("ret = " + ret);  
        if (ret < 0) {  
            ret += 256;  
        }  
        int m = ret / 16;  
        int n = ret % 16;  
        return hexDigits[m] + hexDigits[n];  
    }  
}
