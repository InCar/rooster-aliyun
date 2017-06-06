package com.incarcloud.rooster.mq;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.aliyun.mns.common.ClientException;
import com.aliyun.mns.common.ServiceException;
import com.incarcloud.rooster.aliyun.MnsClient;

public class AliMNSMQ implements IBigMQ {
	private static org.slf4j.Logger s_logger = LoggerFactory.getLogger(AliMNSMQ.class);

	private MnsClient mnsClient;

	/**
	 * @param mnsClient
	 */
	public AliMNSMQ(MnsClient mnsClient) {
		this.mnsClient = mnsClient;
	}

	@Override
	public List<MqSendResult> post(List<MQMsg> listMsgs) {

		if (null == listMsgs || 0 == listMsgs.size()) {
			throw new IllegalArgumentException("message list is null");
		}

		List<MqSendResult> resultList = new ArrayList<>(listMsgs.size());

		for (MQMsg msg : listMsgs) {

			try {
				String msgId = mnsClient.sendMessage("rooster-dev", msg.getData());
				resultList.add(new MqSendResult(null, msgId));
			} catch (ClientException ce) {

				resultList.add(new MqSendResult(
						new MQException("Something wrong with the network connection between client and MNS service."
								+ "Please check your network and DNS availablity."),
						null));
//				ce.printStackTrace();
				s_logger.debug("58",ce.getMessage());
			} catch (ServiceException se) {
				resultList.add(new MqSendResult(new MQException(se.getErrorCode()), null));
//				se.printStackTrace();
				s_logger.debug("62",se.getMessage());
			} catch (Exception e) {
				resultList.add(new MqSendResult(new MQException(e.getMessage()), null));
//				e.printStackTrace();
				s_logger.debug(e.getMessage());
			}
		}

		return resultList;
	}
}
