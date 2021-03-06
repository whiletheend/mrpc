package com.kongzhong.mrpc.springboot.config;

import com.kongzhong.mrpc.enums.TransportEnum;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * mrpc服务端配置
 *
 * @author biezhi
 *         2017/5/13
 */
@ConfigurationProperties("mrpc.server")
@Data
@ToString
public class RpcServerProperties {

    // 服务绑定ip:port
    private String address;

    // 服务器权重
    private int weight;

    // 外网弹性ip:port，不清楚不用填写
    private String elasticIp;

    // 服务端传输协议，默认tcp
    private String transport = TransportEnum.TCP.name();

    // 服务所属appId
    private String appId = "default";

    // 服务名称
    private String appName;

    // 服务负责人
    private String owner;

    // 负责人邮箱
    private String ownerEmail;

    // 业务线程池前缀
    private String poolName = "mrpc-server";

    private String serialize = "kyro";

    private String test;

}