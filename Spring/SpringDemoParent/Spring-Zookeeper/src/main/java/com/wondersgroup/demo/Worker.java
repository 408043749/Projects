package com.wondersgroup.demo;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Worker implements Watcher {
	private static final Logger LOG = LoggerFactory.getLogger(Worker.class);

	@Override
	public void process(WatchedEvent event) {
		// TODO Auto-generated method stub

	}

}
