package com.yunusoksuz.tcpproxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * Created by oksuz on 29/10/2017.
 */
public class AppMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppMain.class);

    public static void main(String args[]) throws UnknownHostException {

        Objects.requireNonNull(System.getProperty("remoteHost"), "remoteHost cannot be null. use -DremoteHost=server.tld");
        Objects.requireNonNull(System.getProperty("remotePort"), "remotePort cannot be null. use -DremotePort=5000");
        Objects.requireNonNull(System.getProperty("bindHost"), "bindHost cannot be null. use -DbindHost=0.0.0.0");
        Objects.requireNonNull(System.getProperty("port"), "port cannot be null. use -Dport=50001");

        String remoteHost = System.getProperty("remoteHost");
        int remotePort = Integer.parseInt(System.getProperty("remotePort"));
        InetAddress bindAddr = InetAddress.getByName(System.getProperty("remotePort"));
        int port = Integer.parseInt(System.getProperty("port"));

        LOGGER.info("Starting proxy server on port {} for remote {}:{}", port, remoteHost, remotePort);

        TcpIpProxy tcpIpProxy = new TcpIpProxy(remoteHost, remotePort, bindAddr, port);
        tcpIpProxy.listen();
    }


}
