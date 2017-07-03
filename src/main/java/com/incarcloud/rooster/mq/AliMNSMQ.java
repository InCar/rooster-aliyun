package com.incarcloud.rooster.mq;

import java.util.ArrayList;
import java.util.List;

import com.aliyun.mns.model.Message;
import org.slf4j.LoggerFactory;

import com.aliyun.mns.common.ClientException;
import com.aliyun.mns.common.ServiceException;

/**
 * @author Fan Beibei
 * @ClassName: AliMNSMQ
 * @Description: 操作消息队列阿里MNS实现
 * @date 2017年6月4日 下午9:07:20
 */
public class AliMNSMQ implements IBigMQ {
    private static org.slf4j.Logger s_logger = LoggerFactory.getLogger(AliMNSMQ.class);


    private String queueName;

    private MnsClient mnsClient;

    /**
     * @param mnsClient
     */
    public AliMNSMQ(String queueName,MnsClient mnsClient) {
        this.queueName = queueName;
        this.mnsClient = mnsClient;
    }


    @Override
    public MqSendResult post(MQMsg msg) {
        MqSendResult result = null;

        try {
            String msgId = mnsClient.sendMessage(queueName, msg);
            if (null != msgId) {
                result = new MqSendResult(null, msgId);
            } else {
                result = new MqSendResult(new MQException("no message Id"), null);
            }

        } catch (ClientException ce) {
            s_logger.debug("58", ce.getMessage());
            result = new MqSendResult(
                    new MQException("Something wrong with the network connection between client and MNS service."
                            + "Please check your network and DNS availablity."),
                    null);
//			ce.printStackTrace();

        } catch (ServiceException se) {
            result = new MqSendResult(new MQException(se.getMessage()), null);
            s_logger.debug("62", se.getErrorCode(), se.getMessage());
//			se.printStackTrace();

        } catch (Exception e) {
            result = new MqSendResult(new MQException(e.getMessage()), null);
//			e.printStackTrace();
            s_logger.debug(e.getMessage());
        }


        return result;
    }

    @Override
    public List<MqSendResult> post(List<MQMsg> listMsgs) {
        if (null == listMsgs || 0 == listMsgs.size()) {
            throw new IllegalArgumentException("message list is null");
        }

        List<MqSendResult> resultList = new ArrayList<>(listMsgs.size());


        try {
            List<Message> messageList = mnsClient.batchSendMessage(queueName, listMsgs);
            for (Message msg : messageList) {
                MqSendResult result = null;
                if (null != msg.getMessageId()) {
                    result = new MqSendResult(null, msg.getMessageId());
                } else {
                    result = new MqSendResult(new MQException("no message Id"), null);
                }

                resultList.add(result);
            }


        } catch (ClientException ce) {
            s_logger.debug("58", ce.getMessage());

            MqSendResult result = new MqSendResult(
                    new MQException("Something wrong with the network connection between client and MNS service."
                            + "Please check your network and DNS availablity."),
                    null);

            for (int i = 0; i < listMsgs.size(); i++) {
                resultList.add(result);
            }


        } catch (ServiceException se) {
            s_logger.debug("62", se.getErrorCode(), se.getMessage());
            MqSendResult result = new MqSendResult(new MQException(se.getMessage()), null);
            for (int i = 0; i < listMsgs.size(); i++) {
                resultList.add(result);
            }

        } catch (Exception e) {

            s_logger.debug(e.getMessage());
            MqSendResult result = new MqSendResult(new MQException(e.getMessage()), null);
            for (int i = 0; i < listMsgs.size(); i++) {
                resultList.add(result);
            }
        }

        return resultList;
    }


    @Override
    public List<MQMsg> batchReceive(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException();
        }

        return mnsClient.batchReceiveMessage(queueName,size);
    }
}
