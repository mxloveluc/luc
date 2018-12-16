package com.wechat.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text;
import com.wechat.common.model.TextMessage;
import com.wechat.common.util.Sha1Util;
import com.wechat.common.util.XmlUtil;

import net.sf.json.JSONObject;

@Service("messageService")
public class MessageServiceImpl implements MessageService{
	private String token = "loveyousomuch";
	private String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx4d9d59e5f12e3634&secret=c5bda1894f0021d6d2da1f2cfd714a4c";
	private static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static String access_token = null;
	private static long fetchTime = 0;
	@Override
	public String validate(String signature, String timestamp, String nonce, String echostr) {
		List<String> sortedList = new ArrayList<String>();
		sortedList.add(token);
		sortedList.add(timestamp);
		sortedList.add(nonce);
		Collections.sort(sortedList);
		
		StringBuilder strBuilder = new StringBuilder();
		for(String str : sortedList) {
			strBuilder.append(str);
		}
		String result = Sha1Util.sha1EnCode(strBuilder.toString());
		if(null != result && result.equals(signature)) {
			return echostr;
		} else {
			return null;
		}
	}

	@Override
	public TextMessage parse(HttpServletRequest request) {

		int len = request.getContentLength();
		System.out.println("数据流长度:" +len);
	    //获取HTTP请求的输入流
	    InputStream is = null;
	    //已HTTP请求输入流建立一个BufferedReader对象
	    BufferedReader br;
	    String buffer = null;
	    StringBuffer sb = new StringBuffer();
		try {
			is = request.getInputStream();
//			br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
//			while ((buffer = br.readLine()) != null) {
//				//在页面中显示读取到的请求参数
//			    sb.append(buffer);
//			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println(sb.toString());
		}
		TextMessage message = null;
		try {
			message = XmlUtil.XmlParse(is,new TextMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}

	public void sendMessage(String str) {
		HttpClient client = new DefaultHttpClient();
	}
	


	private String getEntityData(InputStream iStream) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(iStream));
		String line = null;
		StringBuilder builder = new StringBuilder();
		try {
			while((line = reader.readLine()) != null) {
				builder.append(line);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
	
	private String gettoken() {
		HttpClient client = new DefaultHttpClient();
		HttpGet HttpGet = new HttpGet(access_token_url);
		HttpResponse response = null;
		HttpEntity entity = null;
		String result = null;
		try {
			response = client.execute(HttpGet);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				entity = response.getEntity();
				InputStream iStream = entity.getContent();
				result = getEntityData(iStream);
				JSONObject jsonObject = JSONObject.fromObject(result);
				access_token = jsonObject.getString("access_token");
				fetchTime = new Date().getTime();
				return access_token;
			} 
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public String getAccess_token() {
		if(null == access_token) {
			return gettoken();
		} 
		else if(new Date().getTime() - fetchTime >= 2*60*60*1000) {
			return gettoken();
		}
		else {
			return access_token;
		}
	}
}
