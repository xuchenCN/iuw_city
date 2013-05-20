package com.iuwcity.neo4j.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Date;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import com.iuwcity.storage.cache.Cacheable;
import com.iuwcity.storage.cache.RedisCacheable;
import com.iuwcity.storage.util.DateUtil;

public class NodeParser {
	
	public static Node createNodeAndIndex(EmbeddedGraphDatabase neo4jDB ,Index<Node> index, Serializable o) throws Exception {
		Transaction tx = neo4jDB.beginTx();
		Node node = neo4jDB.createNode();
		BeanInfo beanInfo = Introspector.getBeanInfo(o.getClass());
		PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
		for(PropertyDescriptor p : props){
			Class<?> type = p.getPropertyType();
			if (type == byte.class) {
				node.setProperty(p.getName(), p.getReadMethod().invoke(o, new Object[]{}));
				index.add(node, p.getName(), p.getReadMethod().invoke(o, new Object[]{}));
			} else if (type == short.class) {
				node.setProperty(p.getName(), p.getReadMethod().invoke(o, new Object[]{}));
				index.add(node, p.getName(), p.getReadMethod().invoke(o, new Object[]{}));
			} else if (type == int.class) {
				node.setProperty(p.getName(), p.getReadMethod().invoke(o, new Object[]{}));
				index.add(node, p.getName(), p.getReadMethod().invoke(o, new Object[]{}));
			} else if (type == long.class) {
				node.setProperty(p.getName(), p.getReadMethod().invoke(o, new Object[]{}));
				index.add(node, p.getName(), p.getReadMethod().invoke(o, new Object[]{}));
			} else if (type == float.class) {
				node.setProperty(p.getName(), p.getReadMethod().invoke(o, new Object[]{}));
				index.add(node, p.getName(), p.getReadMethod().invoke(o, new Object[]{}));
			} else if (type == double.class) {
				node.setProperty(p.getName(), p.getReadMethod().invoke(o, new Object[]{}));
				index.add(node, p.getName(), p.getReadMethod().invoke(o, new Object[]{}));
			} else if (type == Date.class) {
				node.setProperty(p.getName(), DateUtil.dateTime2String((Date)p.getReadMethod().invoke(o, new Object[]{})));
				index.add(node, p.getName(), DateUtil.dateTime2String((Date)p.getReadMethod().invoke(o, new Object[]{})));
			} else if (type == String.class) {
				node.setProperty(p.getName(), p.getReadMethod().invoke(o, new Object[]{}));
				index.add(node, p.getName(), p.getReadMethod().invoke(o, new Object[]{}));
			}else if (type == char.class) {
				node.setProperty(p.getName(), p.getReadMethod().invoke(o, new Object[]{}));
				index.add(node, p.getName(), p.getReadMethod().invoke(o, new Object[]{}));
			}
		}
		tx.success();
		
		return node;
	}
	
	public static Cacheable parseNode(Node node,Class<?> bean) throws Exception {
		Constructor<?> c = bean.getConstructor(new Class[] {});
		RedisCacheable newBean = (RedisCacheable)c.newInstance(new Object[] {});

			BeanInfo beanInfo = Introspector.getBeanInfo(newBean.getClass());
			PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor p : props) {
				
				if (node.getProperty(p.getName()) != null) {
					String value = (String)node.getProperty(p.getName());
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
					}else if (type == char.class) {
						m.invoke(newBean, value.charAt(0));
					}

				}
		}
			
		return newBean;
	}
}
