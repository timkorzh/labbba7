package com.company.common.commands;

import com.company.server.processing.collection_manage.CollectionManagement;
import com.company.common.collection_objects.StudyGroup;

import java.util.LinkedHashSet;

public class ShowCommand extends AbstractCommand {
    private final CollectionManagement collectionManagement;
    private LinkedHashSet<StudyGroup> collection;
    public ShowCommand(CollectionManagement collectionManagement) {
        this.collectionManagement = collectionManagement;
    }

    @Override
    public String execute(String CommandArgs) {
        StringBuilder showStr = new StringBuilder();
        for (StudyGroup studyGroup : collectionManagement.getCollection()) {
            showStr.append("\n\n" + studyGroup.toString());
        }
        return showStr.toString();
        }

    @Override
    public String describe() {
        return ("Выводит в стандартный поток вывода все элементы коллекции в строковом представлении");
    }
}
