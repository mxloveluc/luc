package com.wechat.common.model;

import java.util.Date;

public class baseInfo{

	public String toUserName;
	public String fromUserName;
	public Date createTime;
	public String msgType;
	public String MsgId;
	

	@Override
	public String toString() {
		return "toUserName: " + toUserName + ", fromUserName: ";
	}
}
