package com.incarcloud.rooster.mq;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.aliyun.mns.common.ClientException;
import com.aliyun.mns.common.ServiceException;

/**
 * @ClassName: AliMNSMQ
 * @Description: 操作消息队列阿里MNS实现
 * @author Fan Beibei
 * @date 2017年6月4日 下午9:07:20
 *
 */
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
	public MqSendResult post(MQMsg msg) {
		MqSendResult result = null;

		try {
			String msgId = mnsClient.sendMessage("rooster-dev", msg.getData());
			result = new MqSendResult(null, msgId);
		} catch (ClientException ce) {
			s_logger.debug("58",ce.getMessage());
			result = new MqSendResult(
					new MQException("Something wrong with the network connection between client and MNS service."
							+ "Please check your network and DNS availablity."),
					null);
//			ce.printStackTrace();

		} catch (ServiceException se) {
			result = new MqSendResult(new MQException(se.getMessage()), null);
			s_logger.debug("62",se.getErrorCode(),se.getMessage());
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

		for (MQMsg msg : listMsgs) {

			try {
				String msgId = mnsClient.sendMessage("rooster-dev", msg.getData());
				resultList.add(new MqSendResult(null, msgId));
			} catch (ClientException ce) {
				resultList.add(new MqSendResult(
						new MQException("Something wrong with the network connection between client and MNS service."
								+ "Please check your network and DNS availablity."),
						null));
				ce.printStackTrace();
				s_logger.debug("58",ce.getMessage());
			} catch (ServiceException se) {
				s_logger.debug("62",se.getErrorCode(),se.getMessage());
				resultList.add(new MqSendResult(new MQException(se.getErrorCode()), null));
				se.printStackTrace();
				
				
			} catch (Exception e) {
				resultList.add(new MqSendResult(new MQException(e.getMessage()), null));
				e.printStackTrace();
				s_logger.debug(e.getMessage());
			}
		}

		return resultList;
	}
}
