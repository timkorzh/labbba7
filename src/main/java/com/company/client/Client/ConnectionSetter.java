package com.company.client.Client;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;

public class ConnectionSetter {

    public static DatagramChannel getDatagramChannel(SocketAddress a) throws IOException {
        DatagramChannel s =
                DatagramChannel.open();
        s.connect(a);
        return s;
    }
}