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

public class FeedReply  implements  RedisCacheable,MemcachedCacheable , RowMapper<FeedReply>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4636751574170791321L;

	private int id;
	
	private int feedId;
	
	private String content;
	
	private Date createTime;
	
	private int userId;
	
	private int status;
	

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFeedId() {
		return feedId;
	}

	public void setFeedId(int feedId) {
		this.feedId = feedId;
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

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public FeedReply mapRow(ResultSet rs, int arg1) throws SQLException {
		final FeedReply result = new FeedReply();
		result.setId(rs.getInt("id"));
		result.setUserId(rs.getInt("user_id"));
		result.setFeedId(rs.getInt("feed_id"));
		result.setContent(rs.getString("content"));
		result.setStatus(rs.getInt("status"));
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
		map.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.getCreateTime()));
		map.put("feedId", this.getFeedId() + "");
		return map;
	}
	
	
}
