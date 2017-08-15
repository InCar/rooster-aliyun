package com.incarcloud.rooster.bigtable;/**
 * Created by fanbeibei on 2017/7/5.
 */

import com.incarcloud.rooster.datapack.DataPackObject;
import com.incarcloud.rooster.util.DataPackObjectUtils;
import com.incarcloud.rooster.util.RowKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author Fan Beibei
 * @Description: 描述
 * @date 2017/7/5 11:47
 */
public class AliyunBigtable implements IBigTable {
    private static Logger s_logger = LoggerFactory.getLogger(AliyunBigtable.class);
    /**
     * 采集数据表
     */
    private static final String TELEMETRY_TABLE = "telemetry";
    /**
     * 保存vin码的表
     */
    private static final String VIN_TABLE = "vehicle";
    /**
     * 二级索引表
     */
    private static final String SECOND_INDEX_TABLE = "second_index";

    private TableStoreClient client;

    public AliyunBigtable(TableStoreClient client) {
        this.client = client;
    }

    @Override
    public void saveDataPackObject(String rowKey, DataPackObject data, Date recieveTime) throws Exception {
        String secondIndexRowKey = RowKeyUtil.makeDetectionTimeIndexRowKey(DataPackObjectUtils.convertDetectionDateToString(recieveTime),
                data.getVin(), DataPackObjectUtils.getDataType(data));

        client.insert(secondIndexRowKey, rowKey, SECOND_INDEX_TABLE);
        client.insert(rowKey, DataPackObjectUtils.toJson(data), DataPackObjectUtils.getTableName(DataPackObjectUtils.getDataType(data)));
        s_logger.debug("save to tablestore success:" + rowKey);
    }

    @Override
    public void saveVin(String vin) throws Exception {
        client.insert(vin, vin, VIN_TABLE);
        s_logger.debug("save vin  success:" + vin);
    }

    @Override
    public String queryData(String startTimeRowKey, IDataReadable dataReadable) {
        return client.queryData(startTimeRowKey, dataReadable, SECOND_INDEX_TABLE, TELEMETRY_TABLE);
    }

    @Override
    public void close() {
        client.close();
    }
}
