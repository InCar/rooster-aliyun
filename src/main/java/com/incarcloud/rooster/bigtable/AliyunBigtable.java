package com.incarcloud.rooster.bigtable;/**
 * Created by fanbeibei on 2017/7/5.
 */

import com.incarcloud.rooster.datapack.DataPackObject;
import com.incarcloud.rooster.util.DataPackObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fan Beibei
 * @Description: 描述
 * @date 2017/7/5 11:47
 */
public class AliyunBigtable implements IBigTable {
    private static Logger s_logger = LoggerFactory.getLogger(AliyunBigtable.class);

    private TableStoreClient client;
    public AliyunBigtable(TableStoreClient client){
        this.client = client;
    }

    public void save(String rowKey, DataPackObject data, String tableName) throws Exception {

        client.insert(rowKey, DataPackObjectUtils.toJson(data),tableName);
        s_logger.debug("save to tablestore success:"+rowKey);
    }

    @Override
    public void close() {
        client.close();
    }
}
