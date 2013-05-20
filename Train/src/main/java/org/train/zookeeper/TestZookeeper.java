package org.train.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class TestZookeeper {

	public static final int CLIENT_PORT = 2181;

	public static final int CONNECTION_TIMEOUT = 5000;

	public static void main(String[] args) throws Exception {
		ZooKeeper zk = new ZooKeeper("localhost:" + CLIENT_PORT, CONNECTION_TIMEOUT, new Watcher() {
			// ������б��������¼�
			public void process(WatchedEvent event) {
				System.out.println("�Ѿ�������" + event.getType() + "�¼���");
			}
		});

		// ����һ��Ŀ¼�ڵ�
		zk.create("/testRootPath", "testRootData".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}
}
