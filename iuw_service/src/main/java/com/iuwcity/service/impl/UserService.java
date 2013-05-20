package com.iuwcity.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.iuwcity.dao.UserInfoDAO;
import com.iuwcity.service.IUserService;
import com.iuwcity.service.relations.Relations.UserRelations;
import com.iuwcity.service.stats.Stats;
import com.iuwcity.service.stats.Stats.USER_REG_STAT;
import com.iuwcity.storage.StorageService;
import com.iuwcity.storage.bean.UserInfo;
import com.iuwcity.storage.cache.MemcachedClientSevice;
import com.iuwcity.storage.cache.RedisCacheable;
import com.iuwcity.storage.cache.RedisService;

@Service
public class UserService implements IUserService {

	@Autowired
	private StorageService storageService;

	@Autowired
	private UserInfoDAO userDAO;

//	@Autowired
//	private Neo4jService neo4jService;

	private RedisService redisService;

	private MemcachedClientSevice memcachedService;

//	private EmbeddedGraphDatabase neo4jDB;

	public static final Integer USER_NAME_LEN = 3;
	public static final Integer USER_PWD_LEN = 6;

	/**
	 * 注册新用户
	 * 
	 * @param user
	 * @param prop
	 */
	public Stats.USER_REG_STAT registerUser(UserInfo user, Map<String, String> prop) throws Exception {
		if (user == null) {
			return USER_REG_STAT.fail;
		}

		// 验证必添字段
		if (user.getName() == null || "".equals(user.getName()) || user.getPassword() == null || "".equals(user.getPassword())) {
			return USER_REG_STAT.fail;
		}

		// 有效性验证
		if (user.getName().length() < USER_NAME_LEN || user.getPassword().length() < USER_PWD_LEN) {
			return USER_REG_STAT.illegal;
		}

		// 判断用户名是否重复
		if (Stats.USER_CHECK_NAME_DUPLICATED.name_duplicated == checkUserNameDuplicated(user.getName(), null)) {
			return USER_REG_STAT.name_duplicated;
		}
		// 写入存储 FIX ME 异步写非主要存储
		user.setId(redisService.getNextId(user.getClass().getName())); // 获得自增ID
		user.setLastLoginDate(new Date());
		// 为名称和ID两个维度建立查找key分别写如redis
		String[] keys = new String[] { IUserService.CACHE_KEY_HEAD_USER_ID + user.getId(),
				IUserService.CACHE_KEY_HEAD_USER_NAME + user.getName() };
		redisService.putNewBean(keys, user); // 获得新ID
		// 写入memcachend
		memcachedService.getClient().add(IUserService.CACHE_KEY_HEAD_USER_ID + user.getId(), 0, user);
		memcachedService.getClient().add(IUserService.CACHE_KEY_HEAD_USER_NAME + user.getName(), 0, user);
		// 写入node4j
//		neo4jService.createNodeFromBean(user);
		// 写入数据库
		userDAO.addNewUser(user);

		return USER_REG_STAT.success;
	}

	/**
	 * 检查用户名是否重复(redis)
	 * 
	 * @param userName
	 * @param prop
	 * @return
	 */
	public Stats.USER_CHECK_NAME_DUPLICATED checkUserNameDuplicated(final String userName, Map<String, String> prop) {

		RedisCacheable userInfo = redisService.getBean(IUserService.CACHE_KEY_HEAD_USER_NAME + userName, UserInfo.class);
		if (userInfo != null) {
			return Stats.USER_CHECK_NAME_DUPLICATED.name_duplicated;
		}
		return Stats.USER_CHECK_NAME_DUPLICATED.unduplicated;
	}

	/**
	 * 检查用户名是否重复(neo4j)
	 * 
	 * @param userName
	 * @param prop
	 * @return
	 */
	@Deprecated
	public Stats.USER_CHECK_NAME_DUPLICATED checkUserNameDuplicated(final String userName) {

//		Node node = neo4jService.getSingleNodeByProperty(Neo4jService.NODE_INDEX_NAME_USERNAME, userName);
//		if (node != null) {
//			return Stats.USER_CHECK_NAME_DUPLICATED.name_duplicated;
//		}
		return Stats.USER_CHECK_NAME_DUPLICATED.unduplicated;
	}

	/**
	 * 获得用户(memcachend)
	 * 
	 * @param userId
	 * @return
	 * @throws MemcachedException
	 * @throws InterruptedException
	 * @throws TimeoutException
	 */
	public UserInfo getUserInfoById(final int userId) throws TimeoutException, InterruptedException, MemcachedException {
		return memcachedService.getClient().get(IUserService.CACHE_KEY_HEAD_USER_ID + userId);
	}

	/**
	 * 获得用户(memcachend)
	 * 
	 * @param userName
	 * @return
	 * @throws MemcachedException
	 * @throws InterruptedException
	 * @throws TimeoutException
	 */
	public UserInfo getUserInfoByName(final String userName) throws TimeoutException, InterruptedException, MemcachedException {
		return memcachedService.getClient().get(IUserService.CACHE_KEY_HEAD_USER_NAME + userName);
	}

	/*
	 * 用户建立关系(neo4j)
	 * 
	 * @param fromUser
	 * @param toUser
	 * @param relation
	 
	public void createRelationship(final int fromId, final int toId, UserRelations relation) {

		Node fromNode = neo4jService.getSingleNodeByProperty("id", fromId);
		Node toNode = neo4jService.getSingleNodeByProperty("id", toId);
		if (fromNode != null && toNode != null) {
			neo4jService.createRelationToNode(fromNode, toNode, relation);
		}
	}
	*/
	

	/**
	 * 用户建立关系(redis)
	 * 
	 * @param fromUser
	 * @param toUser
	 * @param relation
	 */
	public void createRelationship(final int fromId, final int toId, UserRelations relation, Map<String, String> prop) {
		Jedis conn = redisService.getConnection();
		try {
			// 不能关注自己
			if (fromId == toId || fromId <= 0 || toId <= 0 ) {
				return;
			}

			UserInfo fromUser = (UserInfo) redisService.getBean(CACHE_KEY_HEAD_USER_ID + fromId, UserInfo.class);
			UserInfo toUser = (UserInfo) redisService.getBean(CACHE_KEY_HEAD_USER_ID + toId, UserInfo.class);

			if (fromUser != null && toUser != null) {
				// 头信息:id^relation -> id
				conn.zadd(IUserService.CACHE_KEY_HEAD_USER_FOLLOWER + fromId + IUserService.DATA_SEPARATOR + relation, 0.0,
						toId + "");
				conn.zadd(IUserService.CACHE_KEY_HEAD_USER_FOLLOWER_RE + toId + IUserService.DATA_SEPARATOR + relation, 0.0,
						fromId + "");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			redisService.releaseConnection(conn);
		}
	}
	
	/**
	 * 用户取消关系(redis)
	 * 
	 * @param fromUser
	 * @param toUser
	 * @param relation
	 */
	public void removeRelationship(final int fromId, final int toId, UserRelations relation, Map<String, String> prop) {
		Jedis conn = redisService.getConnection();
		try {
			if (fromId == toId || fromId <= 0 || toId <= 0 ) {
				return;
			}

			UserInfo fromUser = (UserInfo) redisService.getBean(CACHE_KEY_HEAD_USER_ID + fromId, UserInfo.class);
			UserInfo toUser = (UserInfo) redisService.getBean(CACHE_KEY_HEAD_USER_ID + toId, UserInfo.class);

			if (fromUser != null && toUser != null) {
				// 头信息:id^relation -> id
				conn.zrem(IUserService.CACHE_KEY_HEAD_USER_FOLLOWER + fromId + IUserService.DATA_SEPARATOR + relation,toId + "");
				conn.zrem(IUserService.CACHE_KEY_HEAD_USER_FOLLOWER_RE + toId + IUserService.DATA_SEPARATOR + relation,fromId + "");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			redisService.releaseConnection(conn);
		}
	}

	/*
	 * 获得用户关系下的用户列表(neo4j)
	 * 
	 * @param fromId
	 * @param relation
	 * @return
	 * @throws Exception
	 
	public List<UserInfo> getUsersByRelationship(final int fromId, UserRelations relation) throws Exception {
		Node node = neo4jService.getSingleNodeByProperty("id", fromId);
		Traverser traverser = node.traverse(Order.BREADTH_FIRST, StopEvaluator.DEPTH_ONE,
				ReturnableEvaluator.ALL_BUT_START_NODE, relation, Direction.OUTGOING);
		List<UserInfo> users = new ArrayList<UserInfo>();
		for (Node n : traverser) {
			users.add((UserInfo) NodeParser.parseNode(n, UserInfo.class));
		}
		return users;
	}
	*/

	/**
	 * 获得用户关系下的用户ID
	 * 
	 * @param fromId
	 * @param relation
	 * @param page
	 * @param pageSize
	 * @param prop
	 * @return
	 * @throws Exception
	 */
	public Map<String, UserInfo> getOutgoingUsersByRelationship(final int fromId, UserRelations relation, int page,
			int pageSize, Map<String, String> prop) throws Exception {

		Map<String, UserInfo> result = new HashMap<String, UserInfo>();

		if (fromId <= 0) {
			return result;
		}

		Jedis conn = redisService.getConnection();
		try {
			UserInfo fromUser = (UserInfo) memcachedService.getClient().get(CACHE_KEY_HEAD_USER_ID + fromId);

			if (fromUser != null) {
				// 头信息:id^relation -> id
				Set<String> ids = conn.zrange(IUserService.CACHE_KEY_HEAD_USER_FOLLOWER + fromId + IUserService.DATA_SEPARATOR
						+ relation, page, page * pageSize);
				if (ids != null && ids.size() > 0) {
					Collection<String> c = new ArrayList<String>(ids.size());
					Iterator<String> it = ids.iterator();
					while (it.hasNext()) {
						c.add(CACHE_KEY_HEAD_USER_ID + it.next());
					}

					result = memcachedService.getClient().get(c);
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			redisService.releaseConnection(conn);
		}

		return result;
	}

	/**
	 * 获得用户反向关系下的用户ID
	 * 
	 * @param fromId
	 * @param relation
	 * @param page
	 * @param pageSize
	 * @param prop
	 * @return
	 * @throws Exception
	 */
	public Map<String, UserInfo> getIncomingUsersByRelationship(final int fromId, UserRelations relation, int page,
			int pageSize, Map<String, String> prop) throws Exception {
		Map<String, UserInfo> result = new HashMap<String, UserInfo>();

		if (fromId <= 0) {
			return result;
		}

		Jedis conn = redisService.getConnection();

		try {
			UserInfo fromUser = (UserInfo) memcachedService.getClient().get(CACHE_KEY_HEAD_USER_ID + fromId);

			if (fromUser != null) {
				// 头信息:id^relation -> id
				Set<String> ids = conn.zrange(IUserService.CACHE_KEY_HEAD_USER_FOLLOWER_RE + fromId
						+ IUserService.DATA_SEPARATOR + relation, page, page * pageSize);
				if (ids != null && ids.size() > 0) {
					Collection<String> c = new ArrayList<String>(ids.size());
					Iterator<String> it = ids.iterator();
					while (it.hasNext()) {
						c.add(CACHE_KEY_HEAD_USER_ID + it.next());
					}

					result = memcachedService.getClient().get(c);
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			redisService.releaseConnection(conn);
		}

		return result;
	}

	/**
	 * 获得用户关系下的数量（反向）
	 * 
	 * @param fromId
	 * @param relation
	 * @return
	 */
	public long getIncomingUsersByRelationshipCount(final int fromId, final UserRelations relation) {
		if (fromId <= 0) {
			return 0;
		}
		Jedis conn = redisService.getConnection();
		try {
			UserInfo fromUser = (UserInfo) memcachedService.getClient().get(CACHE_KEY_HEAD_USER_ID + fromId);
			if (fromUser != null) {
				return conn.zcard(IUserService.CACHE_KEY_HEAD_USER_FOLLOWER_RE + fromId + IUserService.DATA_SEPARATOR
						+ relation);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			redisService.releaseConnection(conn);
		}

		return 0;
	}

	/**
	 * 获得用户关系下的数量
	 * 
	 * @param fromId
	 * @param relation
	 * @return
	 */
	public long getOutgoingUsersByRelationshipCount(final int fromId, final UserRelations relation) {
		if (fromId <= 0) {
			return 0;
		}
		Jedis conn = redisService.getConnection();
		try {
			UserInfo fromUser = (UserInfo) memcachedService.getClient().get(CACHE_KEY_HEAD_USER_ID + fromId);
			if (fromUser != null) {
				return conn.zcard(IUserService.CACHE_KEY_HEAD_USER_FOLLOWER + fromId + IUserService.DATA_SEPARATOR + relation);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			redisService.releaseConnection(conn);
		}

		return 0;
	}

	/*
	 * 获得用户反向关系下的用户ID(neo4j)
	 * 
	 * @param fromId
	 * @param relation
	 * @param page
	 * @param pageSize
	 * @param prop
	 * @return
	 * @throws Exception
	
	public List<UserInfo> getIncomingUsersByRelationship(final int fromId, UserRelations relation) throws Exception {

		Node node = neo4jService.getSingleNodeByProperty("id", fromId);
		Traverser traverser = node.traverse(Order.BREADTH_FIRST, StopEvaluator.DEPTH_ONE,
				ReturnableEvaluator.ALL_BUT_START_NODE, relation, Direction.INCOMING);
		List<UserInfo> users = new ArrayList<UserInfo>();
		for (Node n : traverser) {
			users.add((UserInfo) NodeParser.parseNode(n, UserInfo.class));
		}
		return users;
	}
	 */
	
	/**
	 * 获得用户共同关系下的用户
	 * 注意：此方法会根据userIds来创建新的Set在Redis里，所以不推荐将大量的userIds进行对比 
	 * @param relation
	 * @param userIds
	 * @return
	 */
	public Map<String, UserInfo> getInterRelationshipUser(UserRelations relation, int page,int pageSize,final Integer... userIds ) {
		
		Map<String, UserInfo> result = new HashMap<String, UserInfo>();
		
		if (userIds.length <= 0 ) {
			return result;
		}
		@SuppressWarnings("unchecked")
		List<Integer> keys = (List<Integer>)Arrays.asList(userIds);
		Collections.sort(keys);
		String dstkey = IUserService.CACHE_KEY_HEAD_INTER_RELA;
		List<String> zsetKeys = new ArrayList<String>();
		for(Integer id : keys){
			if(id != null && id > 0){
				dstkey = dstkey + id + IUserService.DATA_SEPARATOR;
				zsetKeys.add(IUserService.CACHE_KEY_HEAD_USER_FOLLOWER + id + IUserService.DATA_SEPARATOR + relation);
			}else {
				return result;
			}
		}
		
		Jedis conn = redisService.getConnection();
		try {
			conn.zinterstore(dstkey,zsetKeys.toArray(new String[]{}));
			Set<String> resultKeys = conn.zrange(dstkey, page, pageSize);
			result = memcachedService.getClient().get(resultKeys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			redisService.releaseConnection(conn);
		}

		return result;
	}
	
	
	/**
	 * 获得用户共同关系下的用户
	 * @param relation
	 * @param userIds
	 * @return
	 */
	@Override
	public Map<String, UserInfo> getInterRelationshipUser(UserRelations relation, int page,int pageSize,final int selfId,final int otherId ) {
		
		Map<String, UserInfo> result = new HashMap<String, UserInfo>();
		
		if(relation == null ||selfId <= 0 || otherId <= 0){
			return result;
		}
		
		String dstkey = IUserService.CACHE_KEY_HEAD_INTER_RELA;
		String[] zsetKeys = new String[]{IUserService.CACHE_KEY_HEAD_USER_FOLLOWER + selfId + IUserService.DATA_SEPARATOR + relation,
			IUserService.CACHE_KEY_HEAD_USER_FOLLOWER + otherId + IUserService.DATA_SEPARATOR + relation};
		
		//排序生成KEY
		if(selfId > otherId){
			dstkey = dstkey + otherId + IUserService.DATA_SEPARATOR + selfId + IUserService.DATA_SEPARATOR;
		}else {
			dstkey = dstkey + selfId + IUserService.DATA_SEPARATOR + otherId + IUserService.DATA_SEPARATOR;
		}
		
		Jedis conn = redisService.getConnection();
		try {
			conn.zinterstore(dstkey,zsetKeys);
			Set<String> resultKeys = conn.zrange(dstkey, page, pageSize);
			result = memcachedService.getClient().get(resultKeys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			redisService.releaseConnection(conn);
		}

		return result;
	}
	
	/**
	 * 获得用户共同关系下的用户数量
	 * @param relation
	 * @param userIds
	 * @return
	 */
	@Override
	public long getInterRelationshipUserCount(UserRelations relation,final int selfId,final int otherId ) {
		if(relation == null ||selfId <= 0 || otherId <= 0){
			return 0;
		}
		
		String dstkey = IUserService.CACHE_KEY_HEAD_INTER_RELA;
		
		//排序生成KEY
		if(selfId > otherId){
			dstkey = dstkey + otherId + IUserService.DATA_SEPARATOR + selfId + IUserService.DATA_SEPARATOR;
		}else {
			dstkey = dstkey + selfId + IUserService.DATA_SEPARATOR + otherId + IUserService.DATA_SEPARATOR;
		}
		
		Jedis conn = redisService.getConnection();
		try {
			return conn.zcard(dstkey);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			redisService.releaseConnection(conn);
		}
		return 0;
	}
	
	/**
	 * 刷新memcache中用户信息
	 * @throws MemcachedException 
	 * @throws InterruptedException 
	 * @throws TimeoutException 
	 */
	public void refreshUserInfoToMemcache() throws TimeoutException, InterruptedException, MemcachedException{
		
		MemcachedClient client = this.memcachedService.getClient();
		
		int count = this.userDAO.countUserInfo();
		int page = 0;
		int pageSize = 1024;
		int pageCount = 0 ;
		
		if(count % pageSize == 0){
			pageCount = count / pageSize;
		}else {
			pageCount = (count / pageSize) + 1;
		}
		
		for(  ; page < pageCount; page ++){
			List<UserInfo> list = this.userDAO.getAll(page, pageSize);
			if(list != null){
				
			}
			for(UserInfo user : list){
				client.add(IUserService.CACHE_KEY_HEAD_USER_ID + user.getId(), 0, user);
				client.add(IUserService.CACHE_KEY_HEAD_USER_NAME + user.getName(), 0, user);
			}
		}
		
	}


	@PostConstruct
	public void initService() {
		redisService = storageService.getRedisService();
		memcachedService = storageService.getMemcachedService();
//		this.neo4jDB = neo4jService.getEmbeddedGraphDatabase();
	}

}
