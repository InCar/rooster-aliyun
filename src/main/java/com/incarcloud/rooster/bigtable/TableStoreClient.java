package com.incarcloud.rooster.bigtable;/**
 * Created by fanbeibei on 2017/7/3.
 */

import com.alicloud.openservices.tablestore.ClientConfiguration;
import com.alicloud.openservices.tablestore.SyncClient;
import com.alicloud.openservices.tablestore.model.AlwaysRetryStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fan Beibei
 * @Description: 阿里云表格存储封装
 * @date 2017/7/3 16:58
 */
public class TableStoreClient {
    private static Logger s_logger = LoggerFactory.getLogger(TableStoreClient.class);

    private SyncClient client;

    public TableStoreClient(String otsEndPoint, String accessKeyId, String accessKeySecret, String otsInstance){
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        // 设置建立连接的超时时间。
//        clientConfiguration.setConnectionTimeoutInMillisecond(connectionTimeout);
        // 设置socket超时时间。
//        clientConfiguration.setSocketTimeoutInMillisecond(socketTimeout);
        // 设置重试策略，若不设置，采用默认的重试策略。
        clientConfiguration.setRetryStrategy(new AlwaysRetryStrategy());

        client = new SyncClient(otsEndPoint, accessKeyId, accessKeySecret, otsInstance, clientConfiguration);
    }













}
