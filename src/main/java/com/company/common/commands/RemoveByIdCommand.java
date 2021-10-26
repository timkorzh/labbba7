package com.company.common.commands;

import com.company.client.validation.CommandMethodsExecute;
import com.company.server.processing.collection_manage.CollectionManagement;
import com.company.client.validation.CommandMethods;

import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoveByIdCommand extends AbstractCommand {
    private CollectionManagement collectionManagement;
    public RemoveByIdCommand(CollectionManagement collectionManagement) {
        this.collectionManagement = collectionManagement;
    }

    @Override
    public String execute(String strArgs, Object commandArgs) {
    return execute("-id " + commandArgs.toString());
    }

    @Override
    public String execute(String CommandArgs) {
        //InputDevice device = new InputDevice();
        CommandMethods device = new CommandMethods();
        CommandMethodsExecute methodsExecute = new CommandMethodsExecute();
        int RBI;
        if(CommandArgs == null) {
            try {
                    RBI = device.removeById();
            } catch (InputMismatchException Ex) {
                System.out.println("Введите число");
                return "Введите число";
            }
        }
        else {
            Pattern p = Pattern.compile("-id (\\d+?)( -|$)");
            Matcher m = p.matcher(CommandArgs);
            if (m.find()) {
                RBI = Integer.parseInt(m.group(1));
            }
            else {
                System.out.println("Ожидалось число");
                return "Ожидалось число";
            }
        }

        return methodsExecute.remove(collectionManagement, RBI, CommandMethodsExecute.RemoveMode.Equals);
    }

    @Override
    public String describe() {
        return ("Удаляет элемент из коллекции по его id. Введите после названия команды номер группы для редактирования при помощи ключа: (-id [id])");
    }
}
