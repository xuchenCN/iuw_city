package com.iuwcity.redis.service;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.iuwcity.redis.RedisCacheableImpl;
import com.iuwcity.storage.cache.RedisCacheable;
import com.iuwcity.storage.cache.RedisService;
import com.iuwcity.storage.util.DateUtil;

/**
 * 
 * @author <a target=blank
 *         href=http://wpa.qq.com/msgrd?V=1&Uin=25878467&Exe=QQ&Site
 *         =im.qq.com&Menu=No><img border="0"
 *         SRC=http://wpa.qq.com/pa?p=1:25878467:1 alt="Q我"></a><a
 *         href=mailto:chenxu198511@gmail.com>xuchen</a>
 */
@Service
public class RedisServiceImpl implements RedisService {

	private JedisPool pool = null;

	public JedisPool getPool() {
		return pool;
	}

	public void setPool(JedisPool pool) {
		this.pool = pool;
	}

	/**
	 * @deprecated
	 * @param bean
	 * @throws Exception
	 * 
	 */
	public RedisCacheable putNewBean(final RedisCacheable bean) throws Exception {

		bean.setId(this.getNextId(this.getClass().getName()));
		String key = bean.getKey();

		Jedis jedis = this.pool.getResource();
		try {
			jedis.hmset(key, bean.getValuesMap());
			return bean;
		} catch (Exception ex) {
			throw ex;
		} finally {
			this.releaseConnection(jedis);
		}

	}

	/**
	 * @param bean
	 * @throws Exception
	 */
	public RedisCacheable putNewBean(final String[] keys, final RedisCacheable bean) throws Exception {


		Jedis jedis = this.pool.getResource();
		try {
			for (String key : keys) {
				jedis.hmset(key, bean.getValuesMap());
			}
			return bean;
		} catch (Exception ex) {
			throw ex;
		} finally {
			this.releaseConnection(jedis);
		}

	}

	/**
	 * @deprecated
	 * @param bean
	 * @throws Exception
	 */
	public RedisCacheable updateBean(final RedisCacheable bean) throws Exception {

		// bean.setId(this.getNextId(this.getClass().getName()));
		String key = bean.getKey();

		Jedis jedis = this.pool.getResource();
		try {
			jedis.hmset(key, bean.getValuesMap());
			return bean;
		} catch (Exception ex) {
			throw ex;
		} finally {
			this.releaseConnection(jedis);
		}

	}

	/**
	 * @param bean
	 * @throws Exception
	 */
	public RedisCacheable updateBean(final String[] keys, final RedisCacheable bean) throws Exception {

		// bean.setId(this.getNextId(this.getClass().getName()));
		Jedis jedis = this.pool.getResource();
		try {
			for (String key : keys) {
				jedis.hmset(key, bean.getValuesMap());
			}
			return bean;
		} catch (Exception ex) {
			throw ex;
		} finally {
			this.releaseConnection(jedis);
		}

	}
	
	/**
	 * @deprecated
	 */
	public void removeBean(final RedisCacheable bean) throws Exception {
		Jedis jedis = this.pool.getResource();
		try {

			jedis.del(bean.getKey());

		} catch (Exception ex) {
			throw ex;
		} finally {
			this.releaseConnection(jedis);
		}
	}
	
	/**
	 * 删除Bean
	 */
	public void removeBean(final String[] keys,final RedisCacheable bean) throws Exception {
		Jedis jedis = this.pool.getResource();
		try {
			for (String key : keys) {
				jedis.del(key);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			this.releaseConnection(jedis);
		}
	}

	/**
	 * @deprecated
	 * @param key
	 * @param bean
	 * @return
	 */
	@Override
	public RedisCacheable getBean(final int id, Class<?> bean) {
		Jedis jedis = this.pool.getResource();
		try {
			Constructor<?> c = bean.getConstructor(new Class[] {});
			RedisCacheable newBean = (RedisCacheable) c.newInstance(new Object[] {});
			newBean.setId(id);
			
			Map<String, String> values = jedis.hgetAll(newBean.getKey());

			if (values != null && values.keySet().size() > 0) {
				
				BeanInfo beanInfo = Introspector.getBeanInfo(newBean.getClass());
				PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
				for (PropertyDescriptor p : props) {
					// System.out.println(p.getDisplayName() + ":" +
					// p.getPropertyType() + ":" + p.getReadMethod().getName());
					if (values.containsKey(p.getDisplayName())) {
						String value = values.get(p.getDisplayName());
						Method m = p.getWriteMethod();
						Class<?> type = p.getPropertyType();
						if (type == byte.class) {
							m.invoke(newBean, Byte.valueOf(value));
						} else if (type == short.class) {
							m.invoke(newBean, Short.valueOf(value));
						} else if (type == int.class) {
							m.invoke(newBean, Integer.valueOf(value));
						} else if (type == long.class) {
							m.invoke(newBean, Long.valueOf(value));
						} else if (type == float.class) {
							m.invoke(newBean, Float.valueOf(value));
						} else if (type == double.class) {
							m.invoke(newBean, Double.valueOf(value));
						} else if (type == Date.class) {
							m.invoke(newBean, DateUtil.string2Date(value, DateUtil.DEFAULT_DATETIME_FORMAT));
						} else if (type == String.class) {
							m.invoke(newBean, value);
						} else if (type == char.class) {
							m.invoke(newBean, value.charAt(0));
						}

					}
				}

				return newBean;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.releaseConnection(jedis);
		}

		return null;
	}

	/**
	 * @param key
	 * @param bean
	 * @return
	 */
	@Override
	public RedisCacheable getBean(final String key, Class<?> bean) {
		Jedis jedis = this.pool.getResource();
		try {
			Map<String, String> values = jedis.hgetAll(key);

			if (values != null && values.keySet().size() > 0) {
				
				Constructor<?> c = bean.getConstructor(new Class[] {});
				RedisCacheable newBean = (RedisCacheable) c.newInstance(new Object[] {});

				BeanInfo beanInfo = Introspector.getBeanInfo(newBean.getClass());
				PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
				for (PropertyDescriptor p : props) {
					// System.out.println(p.getDisplayName() + ":" +
					// p.getPropertyType() + ":" + p.getReadMethod().getName());
					if (values.containsKey(p.getDisplayName())) {
						String value = values.get(p.getDisplayName());
						Method m = p.getWriteMethod();
						Class<?> type = p.getPropertyType();
						if (type == byte.class) {
							m.invoke(newBean, Byte.valueOf(value));
						} else if (type == short.class) {
							m.invoke(newBean, Short.valueOf(value));
						} else if (type == int.class) {
							m.invoke(newBean, Integer.valueOf(value));
						} else if (type == long.class) {
							m.invoke(newBean, Long.valueOf(value));
						} else if (type == float.class) {
							m.invoke(newBean, Float.valueOf(value));
						} else if (type == double.class) {
							m.invoke(newBean, Double.valueOf(value));
						} else if (type == Date.class) {
							m.invoke(newBean, DateUtil.string2Date(value, DateUtil.DEFAULT_DATETIME_FORMAT));
						} else if (type == String.class) {
							m.invoke(newBean, value);
						} else if (type == char.class) {
							m.invoke(newBean, value.charAt(0));
						}

					}
				}

				return newBean;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.releaseConnection(jedis);
		}

		return null;
	}

	public static RedisCacheable parseMapToBean(Map<String, String> map, Class<?> bean) throws Exception {

		if (map != null) {
			
			Constructor<?> c = bean.getConstructor(new Class[] {});
			RedisCacheable newBean = (RedisCacheable) c.newInstance(new Object[] {});

			BeanInfo beanInfo = Introspector.getBeanInfo(newBean.getClass());
			PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor p : props) {
				
				if (map.containsKey(p.getDisplayName())) {
					String value = map.get(p.getDisplayName());
					Method m = p.getWriteMethod();
					Class<?> type = p.getPropertyType();
					if (type == byte.class) {
						m.invoke(newBean, Byte.valueOf(value));
					} else if (type == short.class) {
						m.invoke(newBean, Short.valueOf(value));
					} else if (type == int.class) {
						m.invoke(newBean, Integer.valueOf(value));
					} else if (type == long.class) {
						m.invoke(newBean, Long.valueOf(value));
					} else if (type == float.class) {
						m.invoke(newBean, Float.valueOf(value));
					} else if (type == double.class) {
						m.invoke(newBean, Double.valueOf(value));
					} else if (type == Date.class) {
						m.invoke(newBean, DateUtil.string2Date(value, DateUtil.DEFAULT_DATETIME_FORMAT));
					} else if (type == String.class) {
						m.invoke(newBean, value);
					} else if (type == char.class) {
						m.invoke(newBean, value.charAt(0));
					}

				}
			}

			return newBean;
		}
		
		return null;
	}

	public Jedis getConnection() {
		return this.pool.getResource();
	}

	public void releaseConnection(Jedis resource) {
		this.pool.returnResource(resource);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public int getNextId(final String key) {
		Jedis conn = this.getConnection();
		try {
			Long value = conn.incr(key);
			if (value != null) {
				return value.intValue();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.releaseConnection(conn);
		}

		return -1;
	}

}
