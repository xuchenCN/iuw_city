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

public class Feed implements  RedisCacheable,MemcachedCacheable , RowMapper<Feed>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7400415090405515284L;
	
	private int id;
	private int userId;
	private int status = 0;
	private String content;
	private Date createTime = new Date();
	private String relation;
	
	
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Feed mapRow(ResultSet rs, int arg1) throws SQLException {
		final Feed result = new Feed();
		result.setId(rs.getInt("id"));
		result.setUserId(rs.getInt("user_id"));
		result.setContent(rs.getString("content"));
		result.setStatus(rs.getInt("status"));
		result.setRelation(rs.getString("relation"));
		result.setCreateTime(rs.getTimestamp("create_time"));
		return result;
	}
	public String getKey() {
		return KeyUtils.getKey(this.getClass().getName(), "", this.getId() + "");
	}
	public Map<String, String> getValuesMap() {
		Map<String,String> map = new HashMap<String,String>();
		map.put("id", this.getId() + "");
		map.put("userId", this.getUserId() + "");
		map.put("status", this.getStatus() + "");
		map.put("content", this.getContent());
		map.put("relation", this.getRelation());
		map.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.getCreateTime()));
		return map;
	}
	
}
