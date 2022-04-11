package org.xjt.blog.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class MyUtils {
    private static String serverPort;

    @Value("${server.port}")
    private String port;

    @PostConstruct
    public void setServerPort(){
        this.serverPort= port;
    }


    public String getUrl() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "http://"+address.getHostAddress() +":"+MyUtils.serverPort;
    }
}