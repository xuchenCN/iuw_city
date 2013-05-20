package com.iuwcity.neo4j.service;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Date;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.springframework.stereotype.Service;

import com.iuwcity.neo4j.relation.Relations.UserRelations;
import com.iuwcity.storage.cache.Cacheable;
import com.iuwcity.storage.util.DateUtil;

@Service
public class Neo4jServiceImpl implements Neo4jService {

	public String dbPath = "target/neo";

	private EmbeddedGraphDatabase graphdb;
	
	@Override
	public synchronized EmbeddedGraphDatabase getEmbeddedGraphDatabase() {
		if (this.graphdb == null) {
			this.graphdb = new EmbeddedGraphDatabase(dbPath);
		}
		return graphdb;
	}

	public String getDbPath() {
		return dbPath;
	}

	public void setDbPath(String dbPath) {
		this.dbPath = dbPath;
	}
	
	@Override
	public void createRelationToNode(Node node, Node otherNode, RelationshipType relationshipType) {
		node.createRelationshipTo(otherNode, relationshipType);
	}
	
	@Override
	public Node createNodeFromBean(Cacheable bean) throws Exception{
		Index<Node> index = this.graphdb.index().forNodes(Neo4jService.NODE_INDEX_NAME_USERS);
		
		Transaction tx = graphdb.beginTx();
		
		Node node = graphdb.createNode();
		
		BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
		PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
		
		for(PropertyDescriptor p : props){
			Class<?> type = p.getPropertyType();
			if (type == byte.class) {
				node.setProperty(p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
				index.add(node, p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
			} else if (type == short.class) {
				node.setProperty(p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
				index.add(node, p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
			} else if (type == int.class) {
				node.setProperty(p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
				index.add(node, p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
			} else if (type == long.class) {
				node.setProperty(p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
				index.add(node, p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
			} else if (type == float.class) {
				node.setProperty(p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
				index.add(node, p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
			} else if (type == double.class) {
				node.setProperty(p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
				index.add(node, p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
			} else if (type == Date.class) {
				node.setProperty(p.getName(), DateUtil.dateTime2String((Date)p.getReadMethod().invoke(bean, new Object[]{})));
				index.add(node, p.getName(), DateUtil.dateTime2String((Date)p.getReadMethod().invoke(bean, new Object[]{})));
			} else if (type == String.class) {
				node.setProperty(p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
				index.add(node, p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
			}else if (type == char.class) {
				node.setProperty(p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
				index.add(node, p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
			}
		}
		index.add(node, Neo4jService.NODE_INDEX_NAME_KEY, bean.getKey());
		
		tx.success();
		
		createRelationToNode(node, this.graphdb.getReferenceNode(), UserRelations.MATRIX);
		
		return node;
	}
	
	@Override
	public Node updateNodeFromBean(Cacheable bean) throws Exception {
		
		Transaction tx = graphdb.beginTx();
		
		Index<Node> index = this.graphdb.index().forNodes(Neo4jService.NODE_INDEX_NAME_USERS);
		
		IndexHits<Node> indexHits = index.get(Neo4jService.NODE_INDEX_NAME_KEY, bean.getKey());
		
		Node node = indexHits.getSingle();
		
		if(node != null){
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
			PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
			
			for(PropertyDescriptor p : props){
				Class<?> type = p.getPropertyType();
				if (type == byte.class) {
					index.remove(node, p.getName());
					node.setProperty(p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
					index.add(node, p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
				} else if (type == short.class) {
					index.remove(node, p.getName());
					node.setProperty(p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
					index.add(node, p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
				} else if (type == int.class) {
					index.remove(node, p.getName());
					node.setProperty(p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
					index.add(node, p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
				} else if (type == long.class) {
					index.remove(node, p.getName());
					node.setProperty(p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
					index.add(node, p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
				} else if (type == float.class) {
					index.remove(node, p.getName());
					node.setProperty(p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
					index.add(node, p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
				} else if (type == double.class) {
					index.remove(node, p.getName());
					node.setProperty(p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
					index.add(node, p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
				} else if (type == Date.class) {
					index.remove(node, p.getName());
					node.setProperty(p.getName(), DateUtil.dateTime2String((Date)p.getReadMethod().invoke(bean, new Object[]{})));
					index.add(node, p.getName(), DateUtil.dateTime2String((Date)p.getReadMethod().invoke(bean, new Object[]{})));
				} else if (type == String.class) {
					index.remove(node, p.getName());
					node.setProperty(p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
					index.add(node, p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
				}else if (type == char.class) {
					index.remove(node, p.getName());
					node.setProperty(p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
					index.add(node, p.getName(), p.getReadMethod().invoke(bean, new Object[]{}));
				}
			}
		}
		
		tx.success();
		
		return node;
	}
	
	@Override
	public Node getSingleNodeByProperty(final String property,final Object value) {
		
		Index<Node> index = this.graphdb.index().forNodes(Neo4jService.NODE_INDEX_NAME_USERS);
		
		IndexHits<Node> indexHits = index.get(property,value);
		
		Node node = indexHits.getSingle();
		
		return node;
	}

}
