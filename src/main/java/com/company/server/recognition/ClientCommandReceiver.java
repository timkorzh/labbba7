package com.company.server.recognition;
import com.company.server.RequestReader.RequestReader;

@Deprecated
public class ClientCommandReceiver {
    RequestReader requestReader;
//TODO: ?

    public ClientCommandReceiver(RequestReader requestReader) {
        this.requestReader = requestReader;
    }
/*
//Реализация метода перенесена в HelpCommand
    public void help() {
        HashMap<String, AbstractCommand> hashMap = requestReader.getCommandInvoker().getHashMap();
        System.out.printf("%-45s %-45s %n", "ИМЯ КОМАНДЫ", "ОПИСАНИЕ");
        for (String commandName : hashMap.keySet()) {
            System.out.printf("%-45s %-45s %n", commandName, requestReader.getCommandInvoker().getHashMap().get(commandName).describe());

        }
    }
*//*
//Реализация метода перенесена в SaveCommand
    public void save() {
        if (requestReader.getFilePath() != null && !requestReader.getFilePath().matches("[/\\\\]dev.*")) {
            XMLParser xmlParser = new XMLParser(requestReader.getFilePath());
            xmlParser.saveCollection(requestReader.getCollectionManagement());
        }
    }
*/
    public void exit() {

    }
}
