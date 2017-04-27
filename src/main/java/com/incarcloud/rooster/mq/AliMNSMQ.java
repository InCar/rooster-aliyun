package com.incarcloud.rooster.mq;

import org.jdeferred.Promise;

import java.util.List;

public class AliMNSMQ implements IBigMQ {
    @Override
    public Promise<Object, List<MQException>, Object> post(List<MQMsg> listMsgs) {
        return null;
    }
}
