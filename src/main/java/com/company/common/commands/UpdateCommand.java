package com.company.common.commands;

import com.company.common.collection_objects.StudyGroup;
import com.company.server.processing.collection_manage.CollectionManagement;
import com.company.server.InputDevice;

public class UpdateCommand extends AbstractCommand {
    private final CollectionManagement collectionManagement;

    public UpdateCommand(CollectionManagement collectionManagement) {
        this.collectionManagement = collectionManagement;
    }
    @Override
    public String execute(String commandArgs) {
        return "";
    }
    @Override
    public String execute(String strArgs, Object CommandArgs) {
        if (CommandArgs != null) {
            StudyGroup group = (StudyGroup) CommandArgs;
            int GroupId = group.getId();
           if (GroupId > 0) {
                if (collectionManagement.getCollection().removeIf(studyGroup -> studyGroup.getId() == GroupId)) {
                    collectionManagement.add(group);
                } else {
                    return "Ничего не нашёл по этому номеру((";
                }
            }
            else {
                return "Параметр id не найден";

            }
        }
        else {
            collectionManagement.edit();
        }
        return "";
    }

    @Override
    public String describe() {
        return ("Обновляет значение элемента коллекции, id которого равен заданному. Введите после названия команды номер группы для редактирования при помощи ключа: (-id [id])" + InputDevice.getScriptName());
    }
}
