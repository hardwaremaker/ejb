package com.lp.server.util.logger;

import java.io.Serializable;
import java.util.UUID;

public abstract class LogEventDto<T extends LogEventPayload> implements Serializable {
	private static final long serialVersionUID = 2576478296204543533L;

	private long timestamp ;
	private UUID uuid ;
	private String tag ;
	private int stackDepth ;
	private T payload ;
	private long threadId ;
	private String threadName ;
	
	protected LogEventDto() {
		setTimestamp(System.currentTimeMillis()) ;
		setThreadInfo(Thread.currentThread()) ; 
		calculateStackDepth() ;
	}

	protected void calculateStackDepth() {
		stackDepth = Thread.currentThread().getStackTrace().length;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	protected void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public UUID getUuid() {
		return uuid;
	}
	protected void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	public String getTag() {
		return tag;
	}
	protected void setTag(String tag) {
		this.tag = tag;
	}

	public int getStackDepth() {
		return stackDepth ;
	}
	
	public T getPayload() {
		return payload ;
	}

	protected void setPayLoad(T payload) {
		this.payload = payload ;
	}
	
	public String asString() {
		return payload == null ? "<no payload>" : payload.asString() ;
	}

	public long getThreadId() {
		return threadId;
	}

	protected void setThreadId(long threadId) {
		this.threadId = threadId;
	}

	public String getThreadName() {
		return threadName;
	}

	protected void setThreadName(String threadName) {
		this.threadName = threadName;
	}
	
	protected void setThreadInfo(Thread thread) {
		setThreadId(thread.getId()) ;
		setThreadName(thread.getName()) ;
	}
}
