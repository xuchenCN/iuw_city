package com.iuwcity.storage.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.iuwcity.storage.cache.MemcachedCacheable;
import com.iuwcity.storage.cache.RedisCacheable;
import com.iuwcity.storage.util.KeyUtils;

public class UserMessage implements RedisCacheable,MemcachedCacheable , RowMapper<UserMessage>{
	
	private int id;
	private int fromUserId;
	private int toUserId;
	private String fromUserName;
	private String toUserName;
	private String body;
	private Date sendTime;
	private int status;

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(int fromUserId) {
		this.fromUserId = fromUserId;
	}
	public int getToUserId() {
		return toUserId;
	}
	public void setToUserId(int toUserId) {
		this.toUserId = toUserId;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public UserMessage mapRow(ResultSet rs, int arg1) throws SQLException {
		final UserMessage result = new UserMessage();
		result.setId(rs.getInt("id"));
		result.setFromUserId(rs.getInt("from_user_id"));
		result.setFromUserName(rs.getString("from_user_name"));
		result.setToUserId(rs.getInt("to_user_id"));
		result.setToUserName(rs.getString("to_user_name"));
		result.setSendTime(rs.getTimestamp("send_time"));
		result.setStatus(rs.getInt("status"));
		
		return result;
	}
	public String getKey() {
		return KeyUtils.getKey(this.getClass().getName(), "", this.getId() + "");
	}
	public Map<String, String> getValuesMap() {
		Map<String,String> map = new HashMap<String,String>();
		map.put("id", this.getId() + "");
		map.put("fromUserId", this.getFromUserId() + "");
		map.put("toUserId", this.getToUserId() + "");
		map.put("fromUserName", this.getFromUserName());
		map.put("toUserName", this.getToUserName());
		map.put("sendTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.getSendTime()));
		map.put("body", this.getBody());
		map.put("status", this.getStatus() + "");
		
		return map;
	}
	
	
}
