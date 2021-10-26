package com.company.server.connection;

import com.company.common.cmdparcel.CommandParcel;
import com.company.server.recognition.CommandInvoker;
import com.company.server.response.Replier;

import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConnectionManager {
    private final int port;
    private RequestBuilder requestBuilder;
    private Replier replier;
    //private RequestReader requestReader;
    private DatagramSocket socket;
    private DatagramPacket packet;

    protected String readFromScanner(Scanner scanner) {
        try {
            return scanner.nextLine();
        }
        catch (NoSuchElementException e) {
            System.out.println("Чтение из сканнера прошло не совсем успешно, пекарб((((");
        }
        return "";
    }

    public ConnectionManager(int port) throws IOException {
        this.port = port;
        requestBuilder = new RequestBuilder();
        byte[] b = new byte[32757];
        SocketAddress a = new InetSocketAddress(port);
        socket = new DatagramSocket(a);
        socket.setSoTimeout(1000);
        packet = new DatagramPacket(b, b.length);
        replier = new Replier(socket);
    }

    public Replier getReplier() { return replier; }

    //Scanner sc = new Scanner(System.in);
    private boolean checkExit() throws IOException {
        return false;
    }

    public void start(CommandInvoker invoker) throws IOException, ClassNotFoundException {
        CommandParcel parcel;
        boolean receiveTimedOut;
        PrintStream out = new PrintStream(replier);
        while (true) {
            try {
      //          String tempstr = readFromScanner(sc);
        //        if(tempstr.equals("save")) {
          //          invoker.execute("save");
            //        System.out.println("коллекция сохранена");
              //  }

            } catch (Exception e) {
                System.out.println("Сохранение не удалось");
            }

            if (checkExit()) return;
            do {
                receiveTimedOut = false;
                try {
                    socket.receive(packet);
                } catch (SocketTimeoutException ste) {
                    if (checkExit()) return; //Todo: Эта проверка должна быть на уровень выше
                    receiveTimedOut = true;
                }
            } while (receiveTimedOut || (parcel = requestBuilder.append(packet)) == null);

            replier.setAddressPort((InetSocketAddress) packet.getSocketAddress());
            out.print(invoker.execute(parcel.getCommand(), parcel.getArgs()));
            replier.flush();
            /*
            Для многопоточности
            someCommandQueue.put(parcel);
            */
        }
    }
}
