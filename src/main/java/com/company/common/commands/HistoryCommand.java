package com.company.common.commands;

import com.company.server.recognition.CommandInvoker;

public class HistoryCommand extends AbstractCommand {
    CommandInvoker commandInvoker;

    public HistoryCommand(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

        @Override
            public String execute(String CommandArgs) {
            return "Последние команды:" + commandInvoker.history.toString();
        }

        @Override
            public String describe () {
        return ("Выводит историю команд");
        }
}