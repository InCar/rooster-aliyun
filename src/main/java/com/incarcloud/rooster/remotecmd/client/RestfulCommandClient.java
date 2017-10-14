package com.incarcloud.rooster.remotecmd.client;/**
 * Created by fanbeibei on 2017/7/19.
 */

import com.google.gson.Gson;
import com.incarcloud.rooster.gather.cmd.CommandServerRespCode;
import com.incarcloud.rooster.gather.cmd.CommandType;
import com.incarcloud.rooster.gather.cmd.ReqContent;
import com.incarcloud.rooster.gather.cmd.RespContent;
import com.incarcloud.rooster.gather.cmd.client.AbstractCommandClient;
import com.incarcloud.rooster.remotecmd.util.HttpClientUtil;
import com.incarcloud.rooster.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * @author Fan Beibei
 * @Description: restful客户端
 * @date 2017/7/19 17:02
 */
public abstract class RestfulCommandClient extends AbstractCommandClient {
    private static Logger s_logger = LoggerFactory.getLogger(RestfulCommandClient.class);

    /**
     * @param url     服务端地址
     * @param vin     车辆vin码
     * @param command 指令类型
     * @throws IOException 请求超时设备网络问题
     */
    @Override
    public RespContent sendCommand(String url, String vin, CommandType command, Object[] args) throws IOException {
        if (StringUtil.isBlank(url) || StringUtil.isBlank(vin) || null == command) {
            throw new IllegalArgumentException();
        }


        ReqContent req = new ReqContent(command, vin, args);

        Gson gson = new Gson();
        String result = null;
        RespContent resp = null;
        try {
            result = HttpClientUtil.postJson(url, gson.toJson(req),"UTF-8",30000);
            resp = gson.fromJson(result, RespContent.class);
        } catch (SocketTimeoutException e) {
            resp = new RespContent(CommandServerRespCode.REQ_TIMEOUT, "请求超时");
        }

        return resp;
    }


}
