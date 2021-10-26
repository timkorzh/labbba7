package com.company.common.commands;

import com.company.server.recognition.CommandInvoker;
import com.company.client.validation.CommandMethods;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExecuteCommand extends AbstractCommand {
//TODO: Эта команда, наверное, серверу не нужна
    CommandInvoker commandInvoker;

    @Override
    public String execute(String stingArgs, Object objArgs) {
        return execute(objArgs.toString());
    }
    public ExecuteCommand(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
    }


        @Override
        public String execute(String CommandArgs) {
            boolean finished = false;
            String result = "";
            CommandMethods device = new CommandMethods();
            Path REF = null;
            String filePath = null;
            if (CommandArgs == null) {
                System.out.println("Введите путь к файлу");
                CommandArgs = device.readExecuteFilePath();
            } else {
                Pattern p = Pattern.compile("-path (.+?)( -|$)");
                Matcher m = p.matcher(CommandArgs);
                m.find();
                filePath = m.group(1);
            }

                if (filePath != null) {
                    Pattern b = Pattern.compile("[/\\\\]dev.*");
                    Matcher n = b.matcher(filePath);
                    if (n.find()) {
                        return "Не могу исполнить данный файл";

                    }
                    REF = Paths.get(filePath);
                    if (!((new File(REF.toString())).exists())) {
                        System.out.println("Не нашёл такой файл, пекарб((");
                        finished=true;
                    }
                }
                else {
                    result = "Ожидался путь к файлу";
                    finished = true;
                }

            if (!finished) {
                try {
                    Scanner FileScanner = new Scanner(REF);
                    FileScanner.useDelimiter(System.getProperty("line.separator"));
                    while (FileScanner.hasNext()) {
                        commandInvoker.execute(FileScanner.nextLine());
                    }
                }
                catch (IOException e) {
                    System.out.println("Мне жаль, что так вышло((((");
                }
            } else {
                System.out.println(result);
            }
           return result;
        }

        @Override
        public String describe() {
        return ("Считывает и исполняет команды из файла. Введите -path и путь к файлу");
    }
}
