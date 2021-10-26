package com.company.server.connection;

import com.company.common.cmdparcel.CommandParcel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * Класс для приёма запросов от разных клиентов
 */
public class RequestBuilder {
    private HashMap<SocketAddress, ByteBuffer> requestMap;
   // private byte[] buf;
   // private ByteArrayInputStream bArrIn;
    //private ObjectInputStream objIn;

    public RequestBuilder() throws IOException {
        this.requestMap = new HashMap<>();
        //buf = new byte[10];
        //bArrIn = new ByteArrayInputStream(buf);
       // objIn = new ObjectInputStream(bArrIn);
    }

    public CommandParcel append(DatagramPacket packet) throws IOException, ClassNotFoundException {
        //System.arraycopy(packet.getData(), 0, buf, 0,
          //                   packet.getData().length);

        SocketAddress socketAddress = packet.getSocketAddress();
        ByteBuffer bBuf = requestMap.get(socketAddress);
        if (bBuf == null) {
            byte[] data = packet.getData();
            try (ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(data))) {
                bBuf = ByteBuffer.allocate((Integer) objIn.readObject());
            }
            requestMap.put(socketAddress, bBuf);
        } else {
            bBuf.put(packet.getData(), 0, Math.min(packet.getLength(), bBuf.remaining()));
        }

        if (!bBuf.hasRemaining()) {
            //System.arraycopy(bBuf.array(), 0, buf, 0, bBuf.capacity());
            try (ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(bBuf.array()))) {
                CommandParcel cmdParsel = (CommandParcel) objIn.readObject();
            requestMap.remove(socketAddress);
            return cmdParsel;
            }
        }
        return null;
    }
}
