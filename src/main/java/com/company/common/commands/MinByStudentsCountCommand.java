package com.company.common.commands;

import com.company.common.collection_objects.StudyGroup;
import com.company.server.processing.collection_manage.CollectionManagement;

import java.util.Comparator;
import java.util.Optional;

public class MinByStudentsCountCommand extends AbstractCommand {
    CollectionManagement collectionManagement;
    public MinByStudentsCountCommand(CollectionManagement collectionManagement) {
        this.collectionManagement = collectionManagement;
    }
    @Override
    public String execute(String CommandArgs) {

        if(collectionManagement.getCollection().size() != 0 && collectionManagement.getCollection() != null) {

            Optional<StudyGroup> MINGroup = collectionManagement.getCollection().stream().min(Comparator.comparingInt(StudyGroup::getStudentsCount));

            StringBuilder result = new StringBuilder();

            MINGroup.ifPresent(studyGroup -> result.append("Группа с минимальным количеством студентов: " + studyGroup.getId()));

            return result.toString();
        }
        else {
            return "Не могу найти группу с минимальным количсетвом студентов, так как групп нет. Мне очень жаль(";
        }
    }
    @Override
    public String describe() {
        return ("Выводит любой объект из коллекции, значение поля studentsCount которого является минимальным");
    }
}
