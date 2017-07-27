package com.incarcloud.rooster.remotecmd.device;/**
 * Created by fanbeibei on 2017/7/26.
 */

import com.incarcloud.rooster.bigtable.TableStoreClient;
import com.incarcloud.rooster.gather.cmd.device.DeviceConnectionRemoteRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alicloud.openservices.tablestore.model.*;

/**
 * @author Fan Beibei
 * @Description: 依赖阿里云OTS实现的注册器
 * @date 2017/7/26 22:19
 */
public class AliyunDeviceConnectionRemoteRegister implements DeviceConnectionRemoteRegister {

    private static Logger s_logger = LoggerFactory.getLogger(AliyunDeviceConnectionRemoteRegister.class);

    private static final String TABLE_NAME = "register_center";
    private static final String PK_COLUMN_NAME = "key";
    private static final PrimaryKeyType PK_COLUMN_TYPE = PrimaryKeyType.STRING;
    private static final String DATA_COLUMN_NAME = "data";

    private TableStoreClient client;
    public AliyunDeviceConnectionRemoteRegister(TableStoreClient client){
        if(null == client){
            throw new IllegalArgumentException();
        }
        this.client = client;
        initTable(client);
    }


    protected static synchronized void initTable(TableStoreClient client){
        if(!client.existTable(TABLE_NAME)){
            client.createTable(TABLE_NAME,PK_COLUMN_NAME,PK_COLUMN_TYPE);
        }

    }

    @Override
    public void registerConnection(String vin, String cmdServerUrl) {
        try {
            client.insert(vin, cmdServerUrl, TABLE_NAME);
        }catch (Exception e){
            e.printStackTrace();
            s_logger.error("registerConnection failed ,vin="+vin+",cmdServerUrl"+cmdServerUrl+"   "+e.getMessage());
        }
    }

    @Override
    public void removeConnection(String vin) {
        try {

            client.deleteByPk(vin, TABLE_NAME);
        }catch (Exception e){
            e.printStackTrace();
            s_logger.error("removeConnection failed,vin="+vin+"  "+ e.getMessage());
        }
    }
}
