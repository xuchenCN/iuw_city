package com.iuwcity.service;

import java.util.Map;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.exception.MemcachedException;

import com.iuwcity.service.relations.Relations.UserRelations;
import com.iuwcity.service.stats.Stats;
import com.iuwcity.storage.bean.UserInfo;


public interface IUserService {
	
	public static final String CACHE_KEY_HEAD_USER_ID = "user_cache_id:";
	
	public static final String CACHE_KEY_HEAD_USER_NAME = "user_cache_name:";
	
	/**
	 * 共同好友
	 */
	public static final String CACHE_KEY_HEAD_INTER_RELA = "user_cache_inter_rela:";
	
	/**
	 * 关注
	 */
	public static final String CACHE_KEY_HEAD_USER_FOLLOWER = "user_cache_follower:";
	
	/**
	 * 被关注
	 */
	public static final String CACHE_KEY_HEAD_USER_FOLLOWER_RE = "user_cache_follower_re:";
	
	public static final String DATA_SEPARATOR = "^";
	
	public UserInfo getUserInfoById(final int userId) throws TimeoutException, InterruptedException, MemcachedException;
	
	public UserInfo getUserInfoByName(final String userName) throws TimeoutException, InterruptedException, MemcachedException;
	
	public Stats.USER_REG_STAT registerUser(UserInfo user,Map<String,String> prop) throws Exception  ;
	
	/**
	 * 检查用户名是否重复(neo4j)
	 * @param userName
	 * @param prop
	 * @return
	 */
	public Stats.USER_CHECK_NAME_DUPLICATED checkUserNameDuplicated(final String userName);
	
	
	/**
	 * 检查用户名是否重复(redis)
	 * @param userName
	 * @param prop
	 * @return
	 */
	public Stats.USER_CHECK_NAME_DUPLICATED checkUserNameDuplicated(final String userName , Map<String,String> prop);
	
	/*
	 * 用户建立关系(neo4j)
	 * @param fromUser
	 * @param toUser
	 * @param relation
	 
	public void createRelationship(final int fromId , final int toId , UserRelations relation);
	*/
	
	/**
	 * 用户建立关系(redis)
	 * @param fromUser
	 * @param toUser
	 * @param relation
	 */
	public void createRelationship(final int fromId , final int toId , UserRelations relation, Map<String,String> prop);
	
	/*
	 * 获得用户关系下的用户列表(neo4j)
	 * @param fromId
	 * @param relation
	 * @return
	 * @throws Exception
	 
	public List<UserInfo> getUsersByRelationship(final int fromId,UserRelations relation) throws Exception;
	*/
	
	
	/**
	 * 获得用户关系下的用户
	 * @param fromId
	 * @param relation
	 * @param page
	 * @param pageSize
	 * @param prop
	 * @return
	 * @throws Exception
	 */
	public Map<String, UserInfo> getOutgoingUsersByRelationship(final int fromId,UserRelations relation ,int page,int pageSize , Map<String,String> prop ) throws Exception;
	
	/**
	 * 获得用户反向关系下的用户ID
	 * @param fromId
	 * @param relation
	 * @param page
	 * @param pageSize
	 * @param prop
	 * @return
	 * @throws Exception
	 */
	public Map<String, UserInfo> getIncomingUsersByRelationship(final int fromId,UserRelations relation ,int page,int pageSize , Map<String,String> prop ) throws Exception;
	
	
	/**
	 * 获得用户关系下的数量（反向）
	 * @param fromId
	 * @param relation
	 * @return
	 */
	public long getIncomingUsersByRelationshipCount(final int fromId,final UserRelations relation);
	
	/**
	 * 获得用户关系下的数量
	 * @param fromId
	 * @param relation
	 * @return
	 */
	public long getOutgoingUsersByRelationshipCount(final int fromId,final UserRelations relation);
	
	/**
	 * 获得用户共同关系下的用户
	 * 注意：此方法会根据userIds来创建新的Set在Redis里，所以不推荐将大量的userIds进行对比 
	 * @param relation
	 * @param userIds
	 * @return
	 */
	public Map<String, UserInfo> getInterRelationshipUser(UserRelations relation, int page,int pageSize,final Integer... userIds ) ;
	
	/**
	 * 获得用户共同关系下的用户
	 * @param relation
	 * @param userIds
	 * @return
	 */
	public Map<String, UserInfo> getInterRelationshipUser(UserRelations relation, int page,int pageSize,final int selfId,final int otherId );
	
	/**
	 * 获得用户共同关系下的用户数量
	 * @param relation
	 * @param userIds
	 * @return
	 */
	public long getInterRelationshipUserCount(UserRelations relation,final int selfId,final int otherId ); 
	
	/**
	 * 刷新memcached用户信息
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public void refreshUserInfoToMemcache() throws TimeoutException, InterruptedException, MemcachedException;
}
