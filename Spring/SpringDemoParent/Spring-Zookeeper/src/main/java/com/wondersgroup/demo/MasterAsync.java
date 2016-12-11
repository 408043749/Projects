package com.wondersgroup.demo;

import java.io.IOException;
import java.util.Random;

import org.apache.zookeeper.AsyncCallback.DataCallback;
import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.KeeperException.ConnectionLossException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.LoggerFactory;

/**
 * master 异步获取管理权限
 * @author panlinjiang
 *
 */
public class MasterAsync implements Watcher {
	private String hostPort;

	static ZooKeeper zk;

	static boolean isLeader;

	static String serverId = Integer.toHexString(new Random().nextInt());
	
	static StringCallback masterCreateCallback = new StringCallback() {
		
		@Override
		public void processResult(int rc, String path, Object ctx, String name) {
			switch (Code.get(rc)) {
			case CONNECTIONLOSS:
				checkMaster();
				return;
			case OK:
				isLeader = true;
				break;
			default:
				isLeader = false;
			}
			
			System.out.println("i'm "+ (isLeader?"":"not ")+ "the leader");
		}
	}; 
	
	static StringCallback createParentCallback = new StringCallback() {
		
		@Override
		public void processResult(int rc, String path, Object ctx, String name) {
			switch (Code.get(rc)) {
			case CONNECTIONLOSS:
				createParent(path, (byte[])ctx);
				break;
			case OK:
				System.out.println("Parent created");
				break;
			case NODEEXISTS:
				System.out.println("Parent already registered: "+path);
				break;
			default:
				System.out.println("Something went wrong: "+KeeperException.create(Code.get(rc),path));
			}
			
		}
	}; 
	
	static DataCallback masterCheckCallback = new DataCallback() {
		
		@Override
		public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
			switch(Code.get(rc)){
			case CONNECTIONLOSS:
				checkMaster();
				return;
			case NONODE:
				runForMaster();
				return;
			}
			
		}
	};

	public MasterAsync(String hostPort) {
		super();
		this.hostPort = hostPort;
	}

	void startZK() throws IOException, KeeperException, InterruptedException {
		zk = new ZooKeeper(hostPort, 15000, this);
	}

	void stopZK() throws InterruptedException {
		zk.close();
	}

	static void runForMaster() {
		zk.create("/master", serverId.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, masterCreateCallback, null);
		bootstrap();

	}
	
	static void checkMaster(){
		System.out.println("checkMaster...");
		zk.getData("/master", false, masterCheckCallback, null);
	}
	
	public static void bootstrap(){
		createParent("/workers",new byte[0]);
		createParent("/assign",new byte[0]);
		createParent("/tasks",new byte[0]);
		createParent("/status",new byte[0]);
	}
	
	static void createParent(String path,byte[] data){
		zk.create(path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT,createParentCallback,data);
	}

	@Override
	public void process(WatchedEvent event) {
		System.out.println(event);
		System.out.println("do watcher evnet...");
	}

	public static void main(String[] args) throws IOException, Exception {
		MasterAsync m = new MasterAsync("127.0.0.1:2181");
		m.startZK();
		runForMaster();
		Thread.sleep(60000);

		m.stopZK();
	}

}
