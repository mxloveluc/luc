package com.wechat.common;

import javax.servlet.http.HttpServletRequest;

import com.wechat.common.model.TextMessage;

public interface MessageService {

	public String validate(String signature, String timestamp, String nonce, String echostr);

	public TextMessage parse(HttpServletRequest request);
	
	public String getAccess_token();
}
