package com.wondersgroup.demo;

import java.io.IOException;
import java.util.Random;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.ConnectionLossException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class Master implements Watcher {
	private String hostPort;

	private ZooKeeper zk;

	static boolean isLeader;

	String serverId = Integer.toHexString(new Random().nextInt());
	
	public Master(String hostPort) {
		super();
		this.hostPort = hostPort;
	}

	void startZK() throws IOException, KeeperException, InterruptedException {
		zk = new ZooKeeper(hostPort, 15000, this);
	}

	void stopZK() throws InterruptedException {
		zk.close();
	}

	void runForMaster() throws KeeperException, InterruptedException {
		while(true){
			try{
				zk.create("/master", serverId.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
				isLeader = true;
				break;
			}catch(NodeExistsException e){
				System.out.println("master node exist");
				isLeader = false;
				break;
			}catch (ConnectionLossException e) {
				
			}
			
			if(checkMaster())break;
		}

	}
	
	boolean checkMaster() throws KeeperException, InterruptedException{
		System.out.println("checkMaster...");
		while(true){
			try {
				Stat stat = new Stat();
				byte[] data = zk.getData("/master", false, stat);
				isLeader = new String(data).equals(serverId);
				return true;
			}catch(NodeExistsException e){
				System.out.println("master node exist");
				e.printStackTrace();
				return false;
			}catch (ConnectionLossException e) {
				
			}
			
		}
		
	}

	@Override
	public void process(WatchedEvent event) {
		System.out.println(event);
		System.out.println("do watcher evnet...");
	}

	public static void main(String[] args) throws IOException, Exception {
		Master m = new Master("127.0.0.1:2181");
		m.startZK();
		m.runForMaster();
		if(isLeader){
			Thread.sleep(60000);
			System.out.println("i'm the leader");
		}else{
			System.out.println("Someone else is the leader");
		}

		m.stopZK();
	}

}
