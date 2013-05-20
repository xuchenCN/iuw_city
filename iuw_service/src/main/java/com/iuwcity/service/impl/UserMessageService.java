package com.iuwcity.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import net.rubyeye.xmemcached.exception.MemcachedException;

import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;

import com.iuwcity.service.IUserMessageService;
import com.iuwcity.service.IUserService;
import com.iuwcity.storage.StorageService;
import com.iuwcity.storage.bean.UserMessage;
import com.iuwcity.storage.cache.MemcachedClientSevice;
import com.iuwcity.storage.cache.RedisService;

public class UserMessageService implements IUserMessageService {
	
	@Autowired
	private IUserService userService;

	@Autowired
	private StorageService storageService;

	private RedisService redisService;

	private MemcachedClientSevice memcachedService;
	
	/**
	 * 用户接收到的信息
	 * @return
	 */
	@Override
	public Map<String,UserMessage> getUserReceiveMessage(final int userId,int page,int pageSize,Map<String,String> prop){
		Map<String,UserMessage> result = new HashMap<String,UserMessage>();
		if(userId <= 0){
			return result;
		}
		
		Jedis conn = redisService.getConnection();
		try {
			Set<String> msgs = conn.zrange(IUserMessageService.CACHE_KEY_HEAD_RECV_READ_MSG + userId, page, pageSize);
			
			result = memcachedService.getClient().get(msgs);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			redisService.releaseConnection(conn);
		}
		
		return result;
	}
	
	/**
	 * 获得用户收件总数
	 * @param userId
	 * @return
	 */
	@Override
	public long getUserReceiveMessageCount(final int userId) {
		if(userId <= 0){
			return 0;
		}
		
		Jedis conn = redisService.getConnection();
		try {
			return conn.zcard(IUserMessageService.CACHE_KEY_HEAD_RECV_READ_MSG + userId);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			redisService.releaseConnection(conn);
		}
		
		return 0;
		
	}
	
	/**
	 * 发送的消息
	 * @return
	 */
	@Override
	public Map<String,UserMessage> getUserSendMessage(final int userId,int page,int pageSize,Map<String,String> prop){
		Map<String,UserMessage> result = new HashMap<String,UserMessage>();
		if(userId <= 0){
			return result;
		}
		
		Jedis conn = redisService.getConnection();
		try {
			Set<String> msgs = conn.zrange(IUserMessageService.CACHE_KEY_HEAD_USER_SEND_MSG + userId, page, pageSize);
			
			result = memcachedService.getClient().get(msgs);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			redisService.releaseConnection(conn);
		}
		
		return result;
	}
	
	/**
	 * 获得用户发件总数
	 * @param userId
	 * @return
	 */
	@Override
	public long getUserSendMessageCount(final int userId) {
		if(userId <= 0){
			return 0;
		}
		
		Jedis conn = redisService.getConnection();
		try {
			return conn.zcard(IUserMessageService.CACHE_KEY_HEAD_USER_SEND_MSG + userId);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			redisService.releaseConnection(conn);
		}
		
		return 0;
		
	}
	
	/**
	 * 发送消息
	 * @param message
	 */
	@Override
	public void sendMessage(final UserMessage message,Map<String,String> prop){
		if(message == null || message.getFromUserId() <= 0 || message.getToUserId() <= 0){
			return;
		}
		
		Jedis conn = redisService.getConnection();
		try {
			//获得消息ID
			message.setId(redisService.getNextId(message.getClass().getName()));
			//写入接收方收件列表
			conn.zadd(IUserMessageService.CACHE_KEY_HEAD_RECV_READ_MSG + message.getToUserId(), -message.getId(), message.getId() + "");
			//写入发送方发件列表
			conn.zadd(IUserMessageService.CACHE_KEY_HEAD_USER_SEND_MSG + message.getFromUserId(), -message.getId(), message.getId() + "");
			//接收方未读消息数量自增
			conn.incr(IUserMessageService.CACHE_KEY_HEAD_USER_NEW_MSG_COUNT + message.getToUserId());
			
			memcachedService.getClient().add(IUserMessageService.CACHE_KEY_HEAD_USER_MSG + message.getFromUserId(), 0, message);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			redisService.releaseConnection(conn);
		}
	}
	
	/**
	 * 用户读消息
	 * @param userId
	 * @param messageId
	 */
	@Override
	public void userReadMessage(final int userId, final int messageId) {
		
		if(userId <= 0 || messageId <= 0){
			return ;
		}
		Jedis conn = redisService.getConnection();
		try {
			UserMessage message = memcachedService.getClient().get(IUserMessageService.CACHE_KEY_HEAD_USER_MSG + messageId);
			if(message != null && message.getToUserId() == userId){
				message.setStatus(1); //设置为已读
				memcachedService.getClient().add(IUserMessageService.CACHE_KEY_HEAD_USER_MSG + message.getFromUserId(), 0, message);
				//递减未读消息数量
				conn.decr(IUserMessageService.CACHE_KEY_HEAD_USER_NEW_MSG_COUNT + userId);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			redisService.releaseConnection(conn);
		}
	}
	
	
	@PostConstruct
	public void initService() {
		redisService = storageService.getRedisService();
		memcachedService = storageService.getMemcachedService();
	}
}
