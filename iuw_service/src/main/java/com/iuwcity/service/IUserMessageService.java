package com.iuwcity.service;

import java.util.Map;

import com.iuwcity.storage.bean.UserMessage;

public interface IUserMessageService {
	
	public static final String CACHE_KEY_HEAD_USER_MSG = "user_msg_id:";
	
	public static final String CACHE_KEY_HEAD_RECV_READ_MSG = "user_recv_msg_list:";
	
	public static final String CACHE_KEY_HEAD_USER_SEND_MSG = "user_send_msg_list:";
	
	public static final String CACHE_KEY_HEAD_USER_NEW_MSG_COUNT = "user_new_msg_count:";
	
	/**
	 * 用户读消息
	 * @param userId
	 * @param messageId
	 */
	public void userReadMessage(final int userId, final int messageId) ;
	
	/**
	 * 发送消息
	 * @param message
	 */
	public void sendMessage(final UserMessage message,Map<String,String> prop);
	
	/**
	 * 获得用户发件总数
	 * @param userId
	 * @return
	 */
	public long getUserSendMessageCount(final int userId);
	
	/**
	 * 发送的消息
	 * @return
	 */
	public Map<String,UserMessage> getUserSendMessage(final int userId,int page,int pageSize,Map<String,String> prop);
	
	/**
	 * 获得用户收件总数
	 * @param userId
	 * @return
	 */
	public long getUserReceiveMessageCount(final int userId) ;
	
	/**
	 * 用户接收到的信息
	 * @return
	 */
	public Map<String,UserMessage> getUserReceiveMessage(final int userId,int page,int pageSize,Map<String,String> prop);
	
}
