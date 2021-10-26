package com.company.client.Client;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import com.company.common.cmdparcel.CommandParcel;

public class CommandSender {
    public CommandSender(InetAddress addr, int port) throws IOException {
        this.socketAddress = new InetSocketAddress(addr, port);
        this.datagramChannel = (DatagramChannel)ConnectionSetter.getDatagramChannel(this.socketAddress).configureBlocking(false);
    }

    private SocketAddress socketAddress;

    public SocketAddress getSocketAddress() {
        return socketAddress;
    }

    private DatagramChannel datagramChannel;

   public void send(String command, SocketAddress a) throws IOException {
        send(new CommandParcel(command), a);
    }

    public void send(String command, Serializable args, SocketAddress a) throws IOException {
        send(new CommandParcel(command, args), a);
    }

    public void send(CommandParcel commandParcel, SocketAddress a) throws IOException {

        ByteBuffer bBuf;
        byte[] bArr;
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ObjectOutputStream objOut = new ObjectOutputStream(byteOut)) {
            objOut.writeObject(commandParcel);
            bArr = byteOut.toByteArray();
        }

        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ObjectOutputStream objOut = new ObjectOutputStream(byteOut)) {
            objOut.writeObject(bArr.length);
            bBuf = ByteBuffer.wrap(byteOut.toByteArray(), 0, byteOut.size());
        }
        datagramChannel.send(bBuf, a);
        //System.out.println("Отправка 1: " + commandParcel.getCommand());
        for(int i = 0; i < bArr.length; i += 32757) {
            int length = Math.min(32757, bArr.length - i);
            bBuf = ByteBuffer.wrap(bArr, i, length);
            datagramChannel.send(bBuf, a);
            //System.out.println("Отправка 2: " + i);
        }
    }

    public String receive() throws PortUnreachableException, IOException {
        StringBuilder answer = new StringBuilder();

        ByteBuffer f = ByteBuffer.allocate(32757);
        SocketAddress s ;

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            s=datagramChannel.receive(f);
            //System.out.println("Получение 1: " + answer.toString());

            while(!answer.toString().endsWith("\04")) {
                for (int i = 0; i < 10 && s == null; i++) {
                    try {
                        Thread.sleep(1000);
                        s = datagramChannel.receive(f);
                        //System.out.println("Получение 2: " + i);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(s == null) {
                    System.out.println("no datagram was immediately available");
                    return null;
                }
                out.write(f.array());
                answer.append(out.toString().replaceAll("\00", ""));
                f.clear();

            }
        } catch (PortUnreachableException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (answer==null){
           return null;
       } else {
           return answer.deleteCharAt(answer.length() - 1).toString();
       }


    }
}