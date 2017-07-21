package com.kongzhong.mrpc.client;

import com.kongzhong.mrpc.exception.TimeoutException;
import com.kongzhong.mrpc.model.RpcRequest;
import com.kongzhong.mrpc.model.RpcResponse;
import com.kongzhong.mrpc.serialize.jackson.JacksonSerialize;
import com.kongzhong.mrpc.utils.ReflectUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * RPC客户端回调
 *
 * @author biezhi
 * 2017/4/29
 */
@Slf4j
public class RpcCallbackFuture {

    private RpcRequest     request   = null;
    private RpcResponse    response  = null;
    private CountDownLatch latch     = new CountDownLatch(1);
    private long           beginTime = System.currentTimeMillis();

    public RpcCallbackFuture(RpcRequest request) {
        this.request = request;
    }

    public Object get(int milliseconds) throws Exception {
        if (latch.await(milliseconds, TimeUnit.MILLISECONDS)) {
            if (null != response) {
                if (response.getSuccess()) {
                    return response.getResult();
                } else {
                    Class<?>  expType   = ReflectUtils.from(response.getReturnType());
                    Exception exception = (Exception) JacksonSerialize.parseObject(response.getException(), expType);
                    throw exception;
                }
            }
        } else {
            long waitTime = System.currentTimeMillis() - beginTime;
            if (waitTime > milliseconds) {
                String msg = String.format("[Request %s.%s()] timeout", request.getClassName(), request.getMethodName());
                log.warn("{}.{}() timeout", request.getClassName(), request.getMethodName());
                log.warn("RequestId: {}", request.getRequestId());
                log.warn("waitTime : {}ms", waitTime);
                throw new TimeoutException(msg);
            }
        }
        return null;
    }

    public void done(RpcResponse response) {
        this.response = response;
        latch.countDown();
    }

}