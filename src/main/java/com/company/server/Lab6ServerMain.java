package com.company.server;

import com.company.server.data_base.DatabaseCommunicator;
import com.company.server.processing.collection_manage.CollectionManagement;
import com.company.server.recognition.CommandInvoker;
import com.company.server.connection.ConnectionManager;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class Lab6ServerMain {
    private static final int DEFAULT_PORT = 1221;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //Получение пути к файлу для загрузки
        /*String fileName = System.getenv("INPUT_FILE_PATH");
        if(fileName == null) {
            System.out.println("Переменная среды \"INPUT_FILE_PATH\" пустая. Методы load и save не будут работать(");
        }
        else {
            File file = new File(fileName);
            try {
                file.toPath().toRealPath();
                if (fileName.matches("[/\\\\]dev.*")) {
                    System.out.println("Не могу исполнить данный файл");
                    System.exit(1);
                }
                    //if (fileName.matches("[/\\\\]dev.*")) {
            } catch (IOException e) {
                System.out.println("Не хулигань-_-");
            }
        }
*/
        //Запуск сервера
        int port;
        try {
            port = Integer.valueOf(System.getenv("L6_SERVER_PORT"));
        } catch (NumberFormatException nfe) {
            port = DEFAULT_PORT;
            System.out.println("Установлено значение порта по умолчанию: " + port);
        }

        ConnectionManager connectionManager = new ConnectionManager(port);

        //Запуск обработчика команд на сервере
        CommandInvoker commandInvoker = new CommandInvoker(new CollectionManagement(new PrintStream(connectionManager.getReplier())));

        //Загрузка из файла
        //TODO: Заменить на загрузку из базы данных
        //commandInvoker.execute("load -path " + fileName);
        DatabaseCommunicator DBc = DatabaseCommunicator.getDatabaseCommunicator();
        DBc.connect();

        // блок авториации

        //Сервер лдет команд
        System.out.println("Готов начать работу, уважаемый пекарь");
        connectionManager.start(commandInvoker);
        try {
                commandInvoker.execute("save");
                System.out.println("Сервер завершает работу, коллекция сохранена");
        } catch (Exception e) {
            System.out.println("Сохранение не удалось");
        }

    }
}
