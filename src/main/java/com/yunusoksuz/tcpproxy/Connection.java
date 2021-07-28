package com.yunusoksuz.tcpproxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * Created by oksuz on 29/10/2017.
 */
public class Connection implements Runnable {

    private final Socket clientSocket;
    private final String remoteIp;
    private final int remotePort;

    private Socket serverSocket = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(Connection.class);

    public Connection(Socket clientSocket, String remoteIp, int remotePort) {
        this.clientSocket = clientSocket;
        this.remoteIp = remoteIp;
        this.remotePort = remotePort;
    }

    @Override
    public void run() {
        LOGGER.info("new connection {}:{}", clientSocket.getInetAddress().getHostName(), clientSocket.getPort());
        try {
            serverSocket = new Socket(remoteIp, remotePort);
        } catch (IOException e) {
            LOGGER.error("IOException!", e);
            return;
        }

        LOGGER.info("Proxy {}:{} <-> {}:{}", clientSocket.getInetAddress().getHostName(), clientSocket.getPort(), serverSocket.getInetAddress().getHostName(), serverSocket.getPort());

        new Thread(new Proxy(clientSocket, serverSocket)).start();
        new Thread(new Proxy(serverSocket, clientSocket)).start();
        new Thread(() -> {
            while (true) {
                if (clientSocket.isClosed()) {
                    LOGGER.info("client socket ({}:{}) closed", clientSocket.getInetAddress().getHostName(), clientSocket.getPort());
                    closeServerConnection();
                    break;
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(1000L);
                } catch (InterruptedException e) {
                    LOGGER.warn(String.format("Thread interrupted for connection connection %s:%s", serverSocket.getInetAddress().getHostName(), serverSocket.getPort()), e);
                }
            }
        }).start();
    }

    private void closeServerConnection() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                LOGGER.info("closing remote host connection {}:{}", serverSocket.getInetAddress().getHostName(), serverSocket.getPort());
                serverSocket.close();
            } catch (IOException e) {
                // Close silently
            }
        }
    }
}
