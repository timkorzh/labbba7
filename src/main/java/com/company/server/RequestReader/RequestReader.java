package com.company.server.RequestReader;
import com.company.server.processing.collection_manage.CollectionManagement;
import com.company.server.recognition.CommandInvoker;
import com.company.server.response.Replier;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import java.util.Arrays;
/**
 * Класс для распознавния команды
 */
@Deprecated
public class RequestReader {
    private final CommandInvoker commandInvoker;
    private final CollectionManagement collectionManagement;

    public RequestReader(CollectionManagement collectionManagement, String filePath) {
        this.collectionManagement = collectionManagement;
        this.commandInvoker = new CommandInvoker(collectionManagement);
        commandInvoker.execute("load " + filePath);
    }

    public CommandInvoker getCommandInvoker() {
        return commandInvoker;
    }
/*
    public CollectionManagement getCollectionManagement() {
        return collectionManagement;
    }

    @Deprecated
    public void setCollectionManagement(CollectionManagement collectionManagement) {
        this.collectionManagement.getCollection().addAll(collectionManagement.getCollection());
    }
*/
    public void start(int PORT) throws IOException {
        System.out.println("Готов начать работу, уважаемый пекарь");
        byte[] b = new byte[10];
        SocketAddress a =
                new InetSocketAddress(PORT);
        DatagramSocket s =
                new DatagramSocket(a);
        DatagramPacket i =
                new DatagramPacket(b, b.length);
        Replier replier = new Replier(s);
        collectionManagement.setPrintStream(new PrintStream(replier));

        String command = "";
        while (true) {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                while(!command.endsWith("\n")) {
                    s.receive(i);
                    out.write(b);
                    command = out.toString().replaceAll("\00", "");
                    Arrays.fill(b, (byte) 0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            command = command.substring(0, command.length() - 1);
            if(command.equals("exit")) {
                break;
            }
            if(!command.equals("")) {

                replier.setAddressPort((InetSocketAddress) i.getSocketAddress());
                commandInvoker.execute(command);
                replier.flush();
            }
        }
    }
/*
    public String getFilePath() { return filePath; }
    */
}
