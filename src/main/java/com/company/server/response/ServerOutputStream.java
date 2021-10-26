package com.company.server.response;

import java.io.*;

/**
 * Класс для отправки сообщений на клиент
 */
//TODO: ?
@Deprecated
public class ServerOutputStream extends OutputStream {
    Replier replier;
    byte[] buf;

    public ServerOutputStream(Replier replier) {
        //TODO
        this.replier = replier;
    }

    @Override
    public void write(int b) throws IOException {

    }
}
