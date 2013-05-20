package com.iuwcity.storage.bean;

import java.io.Serializable;
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

public class UserInfo implements  RedisCacheable,MemcachedCacheable , RowMapper<UserInfo>{
	
	private static final long serialVersionUID = -8930056915841470198L;

	private int id;
	
	private String name;
	
	private String password;
	
	private int age;
	
	private int sex;
	
	private Date createTime = new Date();;
	
	private Date lastLoginDate = new Date();;
	
	private int status = 0;
	
	private String headImg;
	

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getKey() {
		return KeyUtils.getKey(this.getClass().getName(), "", this.getId() + "");
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Map<String,String> getValuesMap() {
		Map<String,String> map = new HashMap<String,String>();
		map.put("id", this.getId() + "");
		map.put("name", this.getName());
		map.put("password", this.getPassword());
		map.put("age", this.getAge() + "");
		map.put("sex", this.getSex() + "");
		map.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.getCreateTime()));
		map.put("lastLoginDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.getLastLoginDate()));
		map.put("status", this.getStatus() + "");
		map.put("headImg", this.getHeadImg() == null ? "" : this.getHeadImg());
		
		return map;
	}

	public UserInfo mapRow(ResultSet rs, int arg1) throws SQLException {
		final UserInfo result = new UserInfo();
		result.setId(rs.getInt("id"));
		result.setName(rs.getString("name"));
		result.setPassword(rs.getString("password"));
		result.setAge(rs.getInt("age"));
		result.setSex(rs.getInt("sex"));
		result.setCreateTime(rs.getTimestamp("create_time"));
		result.setLastLoginDate(rs.getTimestamp("last_login_date"));
		result.setStatus(rs.getInt("status"));
		result.setHeadImg(rs.getString("head_img"));
		return result;
	}
	
	
}
