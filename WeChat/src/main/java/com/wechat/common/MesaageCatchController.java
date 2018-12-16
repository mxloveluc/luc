package com.wechat.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wechat.common.model.TextMessage;
import com.wechat.common.util.Sha1Util;
import com.wechat.common.util.XmlUtil;

import javafx.collections.transformation.SortedList;


@Controller
public class MesaageCatchController {
	
	@Autowired
	private MessageService messageService;
	
	@RequestMapping(value = "/WeChatInfo", method=RequestMethod.GET)
	@ResponseBody
	public String WeChatInfo(String signature, String timestamp, String nonce, String echostr) {
		return messageService.validate(signature, timestamp, nonce, echostr);
	}
	
	@RequestMapping(value = "/WeChatInfo", method=RequestMethod.POST)
	public void WeChatInfo(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml;charset=utf-8");  
			response.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		TextMessage	message = messageService.parse(request);
//		System.out.println(message.getContent());
//		System.out.println(message.getFromUserName());
//		System.out.println(message.getToUserName());
//		System.out.println(message.getMsgId());
//		System.out.println(message.getMsgType());
		String xml = null;
		String from = message.getFromUserName();
		String to = message.getToUserName();
		message.setToUserName(from);
		message.setFromUserName(to);
		message.setCreateTime(new Date());
		try {
			xml = XmlUtil.XmlGenerate(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


//		System.out.println(messageService.getAccess_token());

		System.out.println(xml);
		try {
			PrintWriter writer  = response.getWriter();
			writer.append(xml);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
