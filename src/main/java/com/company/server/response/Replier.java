package com.company.server.response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * Класс для управления ответами сервера
 */
public class Replier extends OutputStream {
    private ByteArrayOutputStream bArrStream;
    private DatagramSocket socket;
    private InetSocketAddress socketAddress;

    public Replier(DatagramSocket socket, InetSocketAddress socketAddress) {
        this(socket);
        this.socketAddress = socketAddress;
    }

    public Replier(DatagramSocket socket) {
        this.socket = socket;
        bArrStream = new ByteArrayOutputStream();
    }

    public void setAddressPort(InetSocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    @Override
    public void write(int b) throws IOException {
        bArrStream.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        bArrStream.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        bArrStream.write("\04".getBytes());
        DatagramPacket packet = new DatagramPacket(bArrStream.toByteArray(),
                bArrStream.size(), socketAddress);
        socket.send(packet);
        System.out.println(bArrStream.toString().substring(0, bArrStream.toString().length() - 1));
        bArrStream.reset();
    }
}
