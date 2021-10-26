package com.company.common.cmdparcel;

import java.io.*;

public class CommandParcel implements Serializable {
    private String command;
    private Serializable args;

    public CommandParcel(String command, Serializable args) {
        this.command = command;
        this.args = args;
    }

    public CommandParcel(String command) { this.command = command; }

    public String getCommand() { return command; }

    public Object getArgs() { return args; }
}
