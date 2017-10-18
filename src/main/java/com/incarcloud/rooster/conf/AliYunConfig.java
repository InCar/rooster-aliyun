package com.incarcloud.rooster.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AliYunConfig {
	private String _accountId;
	private String _accessKeyId;
	private String _accessKeySecret;

	public String getAccountId() {
		return _accountId;
	}

	@Value("${rooster.aliyun.accountId}")
	public void setAccountId(String value) {
		_accountId = value;
	}

	public String getAccessKeyId() {
		return _accessKeyId;
	}

	@Value("${rooster.aliyun.accessKeyId}")
	public void setAccessKeyId(String value) {
		_accessKeyId = value;
	}

	public String getAccessKeySecret() {
		return _accessKeySecret;
	}

	@Value("${rooster.aliyun.accessKeySecret}")
	public void setAccessKeySecret(String value) {
		_accessKeySecret = value;
	}

	// TODO: MNS OTS config
	private String _mnsEndpoint;
	private String _mnsQueue;
	private String _mnsTopic;
	/**
	 * gather项目中为堆积报文的mq
	 * pipe项目中为取报文的mq
	 */
	private String queueName;
	/**
	 * 国标推送mq
	 */
	private String gbQueueName;
	/**
	 * 地标推送mq
	 */
	private String dbQueueName;

	private int maxConnectionsPerRoute;
	private int maxConnections;

	// OTS
	private String otsInstance;
	private String otsEndpoint;

	public String getOtsEndpoint() {
		return otsEndpoint;
	}

	@Value("${rooster.aliyun.OTS.endpoint}")
	public void setOtsEndpoint(String otsEndpoint) {
		this.otsEndpoint = otsEndpoint;
	}

	public String getOtsInstance() {
		return otsInstance;
	}

	@Value("${rooster.aliyun.OTS.instance}")
	public void setOtsInstance(String otsInstance) {
		this.otsInstance = otsInstance;
	}

	public String getMnsEndpoint() {
		return _mnsEndpoint;
	}

	@Value("${rooster.aliyun.MNS.endpoint}")
	public void setMnsEndpoint(String mnsEndpoint) {
		this._mnsEndpoint = mnsEndpoint;
	}

	public String getMnsQueue() {
		return _mnsQueue;
	}

	@Value("${rooster.aliyun.MNS.queue}")
	public void setMnsQueue(String _mnsQueue) {
		this._mnsQueue = _mnsQueue;
	}

	public String getMnsTopic() {
		return _mnsTopic;
	}

	@Value("${rooster.aliyun.MNS.topic}")
	public void setMnsTopic(String _mnsTopic) {
		this._mnsTopic = _mnsTopic;
	}

	public int getMaxConnectionsPerRoute() {
		return maxConnectionsPerRoute;
	}

	@Value("${rooster.aliyun.MNS.maxConnectionsPerRoute}")
	public void setMaxConnectionsPerRoute(int maxConnectionsPerRoute) {
		this.maxConnectionsPerRoute = maxConnectionsPerRoute;
	}

	public int getMaxConnections() {
		return maxConnections;
	}

	@Value("${rooster.aliyun.MNS.maxConnections}")
	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	@Value("${rooster.aliyun.MNS.queue}")
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getQueueName() {
		return queueName;
	}

	/**
	 * @return the gbQueueName
	 */
	public String getGbQueueName() {
		return gbQueueName;
	}
	

	@Value("${rooster.aliyun.MNS.gbQueue}")
	public void setGbQueueName(String gbQueueName) {
		this.gbQueueName = gbQueueName;
	}

	public String getDbQueueName() {
		return dbQueueName;
	}
	
	@Value("${rooster.aliyun.MNS.dbQueue}")
	public void setDbQueueName(String dbQueueName) {
		this.dbQueueName = dbQueueName;
	}
	
	

}
