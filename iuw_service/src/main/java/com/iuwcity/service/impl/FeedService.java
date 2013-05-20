package com.iuwcity.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.iuwcity.service.IFeedService;
import com.iuwcity.service.IUserService;
import com.iuwcity.service.relations.Relations.UserRelations;
import com.iuwcity.storage.StorageService;
import com.iuwcity.storage.bean.Feed;
import com.iuwcity.storage.bean.FeedReply;
import com.iuwcity.storage.bean.UserInfo;
import com.iuwcity.storage.cache.MemcachedClientSevice;
import com.iuwcity.storage.cache.RedisService;
import com.iuwcity.storage.util.DateUtil;

@Service
public class FeedService implements IFeedService {

	public static final int MAX_THREAD = 5;

	@Autowired
	private IUserService userService;

	@Autowired
	private StorageService storageService;

	private RedisService redisService;

	private MemcachedClientSevice memcachedService;
	// FIX ME MAX_THREAD
	private ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREAD);

//	@Autowired
//	private Neo4jService neo4jService;

//	private EmbeddedGraphDatabase neo4jDB;

	/**
	 * 用户发送feed
	 */
	@Override
	public int userPushNewFeed(final int userId, final UserRelations relation, final Feed feed, final Map<String, String> prop)
			throws Exception {
		if (userId <= 0 || relation == null || feed == null) { // 加强cache命中率
			return 0;
		}

		UserInfo user = userService.getUserInfoById(userId);
		if (user != null) {
			// 分配新ID
			feed.setId(redisService.getNextId(feed.getClass().getName()));

			// 写入memcached
			memcachedService.getClient().add(IFeedService.FEED_CACHE_HEAD + feed.getId(), 0, feed);

			executorService.submit(new PushNewFeedTask(userId, relation, feed)); // 推送给所有可推送的人
			
			return feed.getId();
		}

		return 0;
	}

	/**
	 * 获得用户feed 注意 relation 和 createTime 可为null 如果为null 则不会采用此条件查询
	 * 
	 * @param userId
	 * @param relation
	 * @param createTime
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Feed> getUserFeed(final int userId, final UserRelations relation, final Date createTime, int page,
			int pageSize, Map<String, String> prop) throws Exception {

		Map<String, Feed> result = new HashMap<String, Feed>();

		if (userId <= 0) {
			return result;
		}

		Jedis conn = redisService.getConnection();

		try {
			StringBuilder sbKey = new StringBuilder();
			if (createTime != null) {
				sbKey.append(IFeedService.USER_FEED_CACHE_HEAD).append(userId).append(IUserService.DATA_SEPARATOR)
						.append(relation).append(IUserService.DATA_SEPARATOR)
						.append(DateUtil.date2StringAsyyyyMMdd(createTime));

			} else if (relation != null) {
				sbKey.append(IFeedService.USER_FEED_CACHE_HEAD).append(userId).append(IUserService.DATA_SEPARATOR)
						.append(relation);
			} else {
				sbKey.append(IFeedService.USER_FEED_CACHE_HEAD).append(userId);
			}

			Set<String> collectionKeys = conn.zrange(sbKey.toString(), page, pageSize);

			if (collectionKeys != null && collectionKeys.size() > 0) {
				Collection<String> c = new ArrayList<String>(collectionKeys.size());
				Iterator<String> it = collectionKeys.iterator();
				while (it.hasNext()) {
					c.add(FEED_CACHE_HEAD + it.next());
				}

				result = memcachedService.getClient().get(c);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			redisService.releaseConnection(conn);
		}

		return result;
	}
	
	/**
	 * 获得用户feed的数量 注意 relation 和 createTime 可为null 如果为null 则不会采用此条件查询
	 * 
	 * @param userId
	 * @param relation
	 * @param createTime
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@Override
	public long getUserFeedCount(final int userId, final UserRelations relation, final Date createTime, Map<String, String> prop) throws Exception {

		if (userId <= 0) {
			return 0;
		}

		Jedis conn = redisService.getConnection();

		try {
			StringBuilder sbKey = new StringBuilder();
			if (createTime != null) {
				sbKey.append(IFeedService.USER_FEED_CACHE_HEAD).append(userId).append(IUserService.DATA_SEPARATOR)
						.append(relation).append(IUserService.DATA_SEPARATOR)
						.append(DateUtil.date2StringAsyyyyMMdd(createTime));

			} else if (relation != null) {
				sbKey.append(IFeedService.USER_FEED_CACHE_HEAD).append(userId).append(IUserService.DATA_SEPARATOR)
						.append(relation);
			} else {
				sbKey.append(IFeedService.USER_FEED_CACHE_HEAD).append(userId);
			}

			return conn.zcard(sbKey.toString());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			redisService.releaseConnection(conn);
		}

		return 0;
	}
	
	/**
	 * 用户回复一个feed
	 * @param reply
	 * @param prop
	 * @return
	 * @throws Exception
	 */
	@Override
	public int replyFeed(final FeedReply reply, Map<String, String> prop) throws Exception {

		if (reply == null || reply.getUserId() <= 0 || reply.getFeedId() <= 0) {
			return 0;
		}

		reply.setId(redisService.getNextId(reply.getClass().getName()));

		Jedis conn = redisService.getConnection();
		try {
			// 写入redis
			conn.zadd(IFeedService.FEED_REPLY_LIST_CACHE_HEAD + reply.getFeedId(), reply.getId(), reply.getId() + "");
			
			// 写入memcached
			memcachedService.getClient().add(IFeedService.FEED_REPLY_CACHE_HEAD + reply.getId(), 0, reply);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			redisService.releaseConnection(conn);
		}
		return reply.getId();
	}
	
	/**
	 * 删除feed下的回复
	 * @param feedId
	 * @param replyId
	 */
	@Override
	public void removeFeedReply(final int feedId, final int replyId){
		
		if(feedId <= 0 || replyId <= 0){
			return ;
		}
		
		Jedis conn = redisService.getConnection();
		try {
			// 删除redis
			conn.zrem(IFeedService.FEED_REPLY_LIST_CACHE_HEAD + feedId,  replyId + "");
			
			// 删除memcached
			memcachedService.getClient().delete(IFeedService.FEED_REPLY_CACHE_HEAD + replyId);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			redisService.releaseConnection(conn);
		}
	}
	
	/**
	 * 获得feed下的回复
	 * @param feedId
	 * @param page
	 * @param pageSize
	 * @param prop
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, FeedReply> getFeedReply(final int feedId, int page, int pageSize, Map<String, String> prop)
			throws Exception {

		Map<String, FeedReply> result = new HashMap<String, FeedReply>();

		if (feedId <= 0) {
			return result;
		}

		Jedis conn = redisService.getConnection();
		try {
			
			Set<String> collectionKeys = conn.zrange(IFeedService.FEED_REPLY_LIST_CACHE_HEAD + feedId, page, pageSize);
			
			if (collectionKeys != null && collectionKeys.size() > 0) {
				Collection<String> c = new ArrayList<String>(collectionKeys.size());
				Iterator<String> it = collectionKeys.iterator();
				while (it.hasNext()) {
					c.add(FEED_REPLY_CACHE_HEAD + it.next());
				}

				result = memcachedService.getClient().get(c);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			redisService.releaseConnection(conn);
		}
		
		return result;
	}
	
	/**
	 * 获得feed下的回复数量
	 * @param feedId
	 * @param page
	 * @param pageSize
	 * @param prop
	 * @return
	 * @throws Exception
	 */
	public long getFeedReplyCount(final int feedId, Map<String, String> prop) {

		if (feedId <= 0) {
			return 0;
		}

		Jedis conn = redisService.getConnection();
		try {
			
			return conn.zcard(IFeedService.FEED_REPLY_LIST_CACHE_HEAD + feedId);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			redisService.releaseConnection(conn);
		}
		
		return 0;
	}
	
	/**
	 * 用户删除feed
	 * @param userId
	 * @param feed
	 * @param prop
	 */
	public void removeFeed(final int userId , final Feed feed , Map<String,String> prop){
		
		if(userId <= 0 || feed == null || feed.getRelation() == null || feed.getRelation().equals("")
				|| feed.getCreateTime() == null){
			return ;
		}
		
		Jedis conn = redisService.getConnection();
		try {
			//判断是否是删除自身feed
			if(userId != feed.getUserId()){
				// 头信息:[userId]^[relation]^[yyyy-MM-dd]
				StringBuilder sbKey1 = new StringBuilder();
				sbKey1.append(IFeedService.USER_FEED_CACHE_HEAD).append(userId)
						.append(IUserService.DATA_SEPARATOR).append(feed.getRelation())
						.append(IUserService.DATA_SEPARATOR)
						.append(DateUtil.date2StringAsyyyyMMdd(feed.getCreateTime()));

				// 头信息:[userId]^[relation]
				StringBuilder sbKey2 = new StringBuilder();
				sbKey2.append(IFeedService.USER_FEED_CACHE_HEAD).append(userId)
						.append(IUserService.DATA_SEPARATOR).append(feed.getRelation());

				// 头信息:[userId]
				StringBuilder sbKey3 = new StringBuilder();
				sbKey3.append(IFeedService.USER_FEED_CACHE_HEAD).append(userId);

				// 删除redis
				conn.zrem(sbKey1.toString(),  feed.getId() + "");// 时间维度
				conn.zrem(sbKey2.toString(),  feed.getId() + "");// 关系维度
				conn.zrem(sbKey3.toString(),  feed.getId() + "");// ID维度
			}else {
				//提交删除
				executorService.submit(new removeFeedTask(userId,Enum.valueOf(UserRelations.class, feed.getRelation()),feed));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			redisService.releaseConnection(conn);
		}
		
	}
	
	

	@PostConstruct
	public void initService() {
		redisService = storageService.getRedisService();
		memcachedService = storageService.getMemcachedService();
//		this.neo4jDB = neo4jService.getEmbeddedGraphDatabase();
	}

	/**
	 * 异步写feed的Task
	 * 
	 * @author xuchen
	 * 
	 */
	public class PushNewFeedTask implements Runnable {

		private int userId;

		private UserRelations relation;

		private Feed feed;

		public PushNewFeedTask(final int userId, UserRelations relation, final Feed feed) {
			this.userId = userId;
			this.relation = relation;
			this.feed = feed;
		}

		@Override
		public void run() {
			
			// FIX ME pageSize
			final int pageSize = 500;
			final int count = (int) FeedService.this.userService.getIncomingUsersByRelationshipCount(userId, relation);

			int page = 0;
			if (count % pageSize > 0) {
				page = (count / pageSize) + 1;
			} else {
				page = (count / pageSize);
			}

			Jedis conn = redisService.getConnection();

			try {
				// 写入自身列表
				// 头信息:[userId]
				StringBuilder sbKey = new StringBuilder();
				sbKey.append(IFeedService.USER_FEED_CACHE_HEAD).append(userId);
				conn.zadd(sbKey.toString(), -feed.getId(), feed.getId() + "");// ID维度

				for (int i = 0; i < page; i++) {
					Map<String, UserInfo> resultMap = FeedService.this.userService.getIncomingUsersByRelationship(userId,
							relation, i, pageSize, null);
					if (resultMap != null) {
						Iterator<String> it = resultMap.keySet().iterator();
						while (it.hasNext()) {
							UserInfo user = resultMap.get(it.next());
							if (user != null) {
								// 头信息:[userId]^[relation]^[yyyy-MM-dd]
								StringBuilder sbKey1 = new StringBuilder();
								sbKey1.append(IFeedService.USER_FEED_CACHE_HEAD).append(user.getId())
								.append(IUserService.DATA_SEPARATOR).append(relation)
										.append(IUserService.DATA_SEPARATOR)
										.append(DateUtil.date2StringAsyyyyMMdd(feed.getCreateTime()));

								// 头信息:[userId]^[relation]
								StringBuilder sbKey2 = new StringBuilder();
								sbKey2.append(IFeedService.USER_FEED_CACHE_HEAD).append(user.getId())
										.append(IUserService.DATA_SEPARATOR).append(relation);

								// 头信息:[userId]
								StringBuilder sbKey3 = new StringBuilder();
								sbKey3.append(IFeedService.USER_FEED_CACHE_HEAD).append(user.getId());

								// 写入redis
								conn.zadd(sbKey1.toString(), -feed.getId(), feed.getId() + "");// 时间维度
								conn.zadd(sbKey2.toString(), -feed.getId(), feed.getId() + "");// 关系维度
								conn.zadd(sbKey3.toString(), -feed.getId(), feed.getId() + "");// ID维度

							}
						}
					}

				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				redisService.releaseConnection(conn);
			}
		}

	}
	
	/**
	 * 异步删除feed的Task
	 * 
	 * @author xuchen
	 * 
	 */
	public class removeFeedTask implements Runnable {

		private int userId;

		private UserRelations relation;

		private Feed feed;

		public removeFeedTask(final int userId, UserRelations relation, final Feed feed) {
			this.userId = userId;
			this.feed = feed;
			this.relation = Enum.valueOf(UserRelations.class, feed.getRelation());
		}

		@Override
		public void run() {
			// FIX ME pageSize
			final int pageSize = 500;
			final int count = (int) FeedService.this.userService.getIncomingUsersByRelationshipCount(userId,relation );

			int page = 0;
			if (count % pageSize > 0) {
				page = (count / pageSize) + 1;
			} else {
				page = (count / pageSize);
			}

			Jedis conn = redisService.getConnection();

			try {
				// 删除自身列表
				// 头信息:[userId]
				StringBuilder sbKey = new StringBuilder();
				sbKey.append(IFeedService.USER_FEED_CACHE_HEAD).append(userId);
				conn.zrem(sbKey.toString(), feed.getId() + "");// ID维度

				for (int i = 0; i < page; i++) {
					Map<String, UserInfo> resultMap = FeedService.this.userService.getIncomingUsersByRelationship(userId,
							relation, i, pageSize, null);
					if (resultMap != null) {
						Iterator<String> it = resultMap.keySet().iterator();
						while (it.hasNext()) {
							UserInfo user = resultMap.get(it.next());
							if (user != null) {
								// 头信息:[userId]^[relation]^[yyyy-MM-dd]
								StringBuilder sbKey1 = new StringBuilder();
								sbKey1.append(IFeedService.USER_FEED_CACHE_HEAD).append(user.getId())
										.append(IUserService.DATA_SEPARATOR).append(relation)
										.append(IUserService.DATA_SEPARATOR)
										.append(DateUtil.date2StringAsyyyyMMdd(feed.getCreateTime()));

								// 头信息:[userId]^[relation]
								StringBuilder sbKey2 = new StringBuilder();
								sbKey2.append(IFeedService.USER_FEED_CACHE_HEAD).append(user.getId())
										.append(IUserService.DATA_SEPARATOR).append(relation);

								// 头信息:[userId]
								StringBuilder sbKey3 = new StringBuilder();
								sbKey3.append(IFeedService.USER_FEED_CACHE_HEAD).append(user.getId());

								// 删除redis
								conn.zrem(sbKey1.toString(),  feed.getId() + "");// 时间维度
								conn.zrem(sbKey2.toString(),  feed.getId() + "");// 关系维度
								conn.zrem(sbKey3.toString(),  feed.getId() + "");// ID维度

							}
						}
					}

				}
				//清除memcached
				memcachedService.getClient().delete(IFeedService.FEED_CACHE_HEAD + feed.getId());
				//清除feed reply
				conn.zremrangeByRank(IFeedService.FEED_REPLY_LIST_CACHE_HEAD + feed.getId(), 0, (int)FeedService.this.getFeedReplyCount(feed.getId(), null));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				redisService.releaseConnection(conn);
			}
		}

	}
}
