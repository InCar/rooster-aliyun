package com.incarcloud.rooster.mq;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.http.ClientConfiguration;
import com.aliyun.mns.model.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Fan Beibei
 * @ClassName: MNSClient
 * @Description: 操作阿里MNS的客户端
 * @date 2017年6月4日 下午9:07:20
 */
public class MnsClient {
    private static org.slf4j.Logger s_logger = LoggerFactory.getLogger(MnsClient.class);
    /**
     * MNS 客户端(单例模式)
     */
    private MNSClient client;

    /**
     * @param config  对应MNS的3个 配置{
     *  accessKeyId
     *  accessKeySecret
     *  mnsEndpoint
     * }
     */

    public MnsClient(Properties config) {

        String accessId = (String) config.get("accessKeyId");
        String accessKey = (String) config.get("accessKeySecret");
        String endPoint = (String) config.get("mnsEndpoint");


        /*String accessId = config.getAccessKeyId();
        String accessKey = config.getAccessKeySecret();
        String endPoint = config.getMnsEndpoint();*/



        ClientConfiguration clientConfiguration = new ClientConfiguration();
        CloudAccount cloudAccount = new CloudAccount(accessId, accessKey, endPoint, clientConfiguration);
        client = cloudAccount.getMNSClient();

    }

    /**
     * 获取MNS客户端
     *
     * @return 单例模式的MNS客户端
     */
    private MNSClient getMnsClient() {

        return client;
    }

    /**
     * 发送消息，返回消息ID
     *
     * @param queueName      队列名称
     * @param messageContent 消息内容
     * @return 消息ID
     */
    public String sendMessage(String queueName, byte[] messageContent) {
        if (null == queueName || "".equals(queueName.trim()) || null == messageContent) {
            throw new IllegalArgumentException();
        }

        CloudQueue queue = getMnsClient().getQueueRef(queueName);
        Message message = new Message();
        message.setMessageBody(messageContent);
        Message putMsg = queue.putMessage(message);

        s_logger.debug("******************" + putMsg.getMessageId());

        return putMsg.getMessageId();

    }


    /**
     * 批量发送消息
     *
     * @param queueName 队列名称
     * @param listMsgs  消息内容
     * @return
     */
    public List<Message> batchSendMessage(String queueName, List<MQMsg> listMsgs) {

        if (null == queueName || "".equals(queueName.trim()) || null == listMsgs || 0 == listMsgs.size()) {
            throw new IllegalArgumentException();
        }

        List<Message> messageList = new ArrayList<Message>(listMsgs.size());
        for (MQMsg msg : listMsgs) {
            Message message = new Message();
            message.setMessageBody(msg.getData());
            messageList.add(message);
        }

        CloudQueue queue = getMnsClient().getQueueRef(queueName);

        return queue.batchPutMessage(messageList);
    }

}
