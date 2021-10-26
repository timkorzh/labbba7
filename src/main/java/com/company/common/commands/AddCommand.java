package com.company.common.commands;

import com.company.server.processing.collection_manage.CollectionManagement;
import com.company.common.collection_objects.StudyGroup;
import com.company.server.InputDevice;

public class AddCommand extends AbstractCommand {
    private final CollectionManagement collectionManagement;

    public AddCommand(CollectionManagement collectionManagement) {
        this.collectionManagement = collectionManagement;
    }

    @Override
    public String execute(String commandArgs) {

        if (commandArgs == null) {
            collectionManagement.add();
        }
        else {
            collectionManagement.add(commandArgs);
        }

        return "";

    }

    @Override
    public String execute(String strArgs, Object commandArgs) {
        if (commandArgs == null)
            throw new NullPointerException("AddCommand expects to receive a StudyGroup object. " +
                    "Received null.");

        if (commandArgs instanceof StudyGroup) {
            StudyGroup newGroup = (StudyGroup) commandArgs;
            newGroup.setId(StudyGroup.getGroupCounter());
            return collectionManagement.add(newGroup);
        }
        //TODO: либо научить сервер обрабатывать эксепшн подобного вида, либо заменить на простую отправку сообщения клиенту
        else throw new IllegalArgumentException("AddCommand expects to receive a StudyGroup object. " +
                "Type of received object: " + commandArgs.getClass());
    }

    @Override
    public String describe() {
        return ("Добавляет новый элемент в коллекцию." + InputDevice.getScriptName() + "\n Чтобы выйти из команды в режиме построчного ввода, напишите: exitcmd");
    }
}
