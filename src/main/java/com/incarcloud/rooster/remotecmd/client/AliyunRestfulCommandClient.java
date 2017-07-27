package com.incarcloud.rooster.remotecmd.client;/**
 * Created by fanbeibei on 2017/7/26.
 */

import com.alicloud.openservices.tablestore.model.*;
import com.incarcloud.rooster.bigtable.TableStoreClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fan Beibei
 * @Description: 依赖阿里云OTS实现的远程命令客户端
 * @date 2017/7/26 22:17
 */
public class AliyunRestfulCommandClient extends RestfulCommandClient {

    private static Logger s_logger = LoggerFactory.getLogger(AliyunRestfulCommandClient.class);

    private static final String TABLE_NAME = "register_center";
    private static final String PK_COLUMN_NAME = "key";
    private static final String DATA_COLUMN_NAME = "data";

    private TableStoreClient client;
    public AliyunRestfulCommandClient(TableStoreClient client){
        if(null == client){
            throw new IllegalArgumentException();
        }
        this.client = client;

    }

    @Override
    protected String getServerUrl(String vin) {

        try {
            Row row = client.queryByPk(vin, TABLE_NAME);
            if(null != row){
                Column  data = row.getLatestColumn(DATA_COLUMN_NAME);
                return  data.getValue().asString();
            }
        }catch (Exception e){
            e.printStackTrace();
            s_logger.error("getServerUrl error,vin="+vin+" "+e.getMessage());
        }


        return null;
    }
}
