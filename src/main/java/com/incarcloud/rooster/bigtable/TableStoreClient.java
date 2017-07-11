package com.incarcloud.rooster.bigtable;/**
 * Created by fanbeibei on 2017/7/3.
 */

import com.alicloud.openservices.tablestore.ClientConfiguration;
import com.alicloud.openservices.tablestore.SyncClient;
import com.alicloud.openservices.tablestore.model.*;
import com.incarcloud.rooster.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Fan Beibei
 * @Description: 阿里云表格存储封装
 * @date 2017/7/3 16:58
 */
public class TableStoreClient {
    private static Logger s_logger = LoggerFactory.getLogger(TableStoreClient.class);

    private static final String PK_COLUMN_NAME = "key";
    private static final String DATA_COLUMN_NAME = "value";

    private SyncClient client;

    public TableStoreClient(String otsEndPoint, String accessKeyId, String accessKeySecret, String otsInstance) {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        // 设置建立连接的超时时间。
//        clientConfiguration.setConnectionTimeoutInMillisecond(connectionTimeout);
        // 设置socket超时时间。
//        clientConfiguration.setSocketTimeoutInMillisecond(socketTimeout);
        // 设置重试策略，若不设置，采用默认的重试策略。
        clientConfiguration.setRetryStrategy(new AlwaysRetryStrategy());

        client = new SyncClient(otsEndPoint, accessKeyId, accessKeySecret, otsInstance, clientConfiguration);
    }

    /**
     * 释放资源
     */
    public void close(){
        client.shutdown();
    }


    /**
     * 插入数据
     *
     * @param pkValue   主键值
     * @param value     数据
     * @param tableName 表格名称
     * @throws Exception
     */
    public void insert(String pkValue, String value, String tableName) throws Exception {
        if (StringUtil.isBlank(pkValue)) {
            throw new IllegalArgumentException("param error");
        }

        // 构造主键
        PrimaryKeyBuilder pkBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();

        pkBuilder.addPrimaryKeyColumn(PK_COLUMN_NAME, PrimaryKeyValue.fromString(pkValue));
        PrimaryKey primaryKey = pkBuilder.build();
        RowPutChange rowPutChange = new RowPutChange(tableName, primaryKey);

        // 这里设置返回类型为RT_PK，意思是在返回结果中包含PK列的值。如果不设置ReturnType，默认不返回。
        rowPutChange.setReturnType(ReturnType.RT_PK);


        // 加入属性列，消息内容
        rowPutChange
                .addColumn(new Column(DATA_COLUMN_NAME, ColumnValue.fromString(value)));

        // 写数据到TableStore
        client.putRow(new PutRowRequest(rowPutChange));
        // 如果没有抛出异常，则说明执行成功
        System.out.println("Put row succeeded.");
    }


    /**
     * 更新数据
     *
     * @param pkValue   主键值
     * @param value     数据
     * @param tableName 表格名称
     * @throws Exception
     */
    public void update(String pkValue, String value, String tableName) {
        // 构造主键
        PrimaryKeyBuilder primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        primaryKeyBuilder.addPrimaryKeyColumn(PK_COLUMN_NAME, PrimaryKeyValue.fromString(pkValue));
        PrimaryKey primaryKey = primaryKeyBuilder.build();

        RowUpdateChange rowChange = new RowUpdateChange(tableName);
        rowChange.setPrimaryKey(primaryKey);
        rowChange.put(new Column(DATA_COLUMN_NAME, ColumnValue.fromString(value)));

        UpdateRowRequest request = new UpdateRowRequest();
        request.setRowChange(rowChange);
        // 调用UpdateRow接口执行
        client.updateRow(request);

    }


    /**
     * 刪除数据
     *
     * @param pkValue   主键值
     * @param tableName 表格名称
     * @throws Exception
     */
    public void deleteByPk(String pkValue, String tableName) throws Exception {
        // 构造主键
        PrimaryKeyBuilder primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        primaryKeyBuilder.addPrimaryKeyColumn(PK_COLUMN_NAME, PrimaryKeyValue.fromString(pkValue));
        PrimaryKey primaryKey = primaryKeyBuilder.build();

        RowDeleteChange rowChange = new RowDeleteChange(tableName, primaryKey);
        // 构造请求
        DeleteRowRequest request = new DeleteRowRequest();
        request.setRowChange(rowChange);
        // 调用DeleteRow接口执行删除
        client.deleteRow(request);
        // 如果没有抛出异常，则表示成功

    }

    /**
     * 根據主鍵查詢
     *
     * @param pkValue   主键值
     * @param tableName 表格名称
     * @throws Exception
     */

    public Row queryByPk(String pkValue, String tableName) throws Exception {
        if (StringUtil.isBlank(pkValue)) {
            throw new IllegalArgumentException("param error");
        }
        // 构造主键
        PrimaryKeyBuilder primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        primaryKeyBuilder.addPrimaryKeyColumn(PK_COLUMN_NAME, PrimaryKeyValue.fromString(pkValue));
        PrimaryKey primaryKey = primaryKeyBuilder.build();
        // 读一行
        SingleRowQueryCriteria criteria = new SingleRowQueryCriteria(tableName, primaryKey);
        // 设置读取最新版本
        criteria.setMaxVersions(1);

        criteria.addColumnsToGet(DATA_COLUMN_NAME);

        GetRowResponse getRowResponse = client.getRow(new GetRowRequest(criteria));
        return getRowResponse.getRow();

    }


    /**
     * 根据主键批量获取
     * @param pkValueList 主键
     * @param tableName 表名
     * @return
     * @throws Exception
     */
    public List<BatchGetRowResponse.RowResult> batchGetByIds(List<String> pkValueList, String tableName) throws Exception {
        MultiRowQueryCriteria multiRowQueryCriteria = new MultiRowQueryCriteria(tableName);
        // 加入10个要读取的行
        for (String pk : pkValueList) {
            PrimaryKeyBuilder primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
            primaryKeyBuilder.addPrimaryKeyColumn(PK_COLUMN_NAME, PrimaryKeyValue.fromString(pk));
            PrimaryKey primaryKey = primaryKeyBuilder.build();
            multiRowQueryCriteria.addRow(primaryKey);
        }
        // 添加条件
        multiRowQueryCriteria.setMaxVersions(1);
        multiRowQueryCriteria.addColumnsToGet(DATA_COLUMN_NAME);
        BatchGetRowRequest batchGetRowRequest = new BatchGetRowRequest();
        // batchGetRow支持读取多个表的数据, 一个multiRowQueryCriteria对应一个表的查询条件, 可以添加多个multiRowQueryCriteria.
        batchGetRowRequest.addMultiRowQueryCriteria(multiRowQueryCriteria);
        BatchGetRowResponse batchGetRowResponse = client.batchGetRow(batchGetRowRequest);
        System.out.println("是否全部成功:" + batchGetRowResponse.isAllSucceed());
        return batchGetRowResponse.getSucceedRows();

    }


}
