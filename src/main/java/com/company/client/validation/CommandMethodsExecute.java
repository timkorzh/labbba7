package com.company.client.validation;

import com.company.server.processing.collection_manage.CollectionManagement;
import com.company.common.collection_objects.StudyGroup;

import java.util.InputMismatchException;
import java.util.stream.Stream;

public class CommandMethodsExecute {
//TODO: класс должен быть на сервере
    public enum RemoveMode {
        High,
        Low,
        Equals
    }

    public String remove(CollectionManagement collectionManagement, int RemoveById, RemoveMode Equals) {
        try {
            if (collectionManagement.getCollection().stream().noneMatch(a -> a.getId() == RemoveById)) {
                return "Ничего не нашёл по этому номеру:((";
            }
            switch (Equals) {
                case Equals :
                    collectionManagement.getCollection().removeIf(studyGroup -> RemoveById == studyGroup.getid());
                    return "Группа с id: " + RemoveById + " была успешно удалена! ~~~~~~~~~~~Помянем~~~~~~~~~~";
                case Low :
                    collectionManagement.getCollection().removeIf(a -> a.getId() < RemoveById);
                    return "Группа с id ниже, чем: " + RemoveById + " была успешно удалена! ~~~~~~~~~~~Помянем~~~~~~~~~~";
                case High :
                    collectionManagement.getCollection().removeIf(a -> a.getId() > RemoveById);
                    return "Группы с id выше, чем: " + RemoveById + " была успешно удалена! ~~~~~~~~~~~Помянем~~~~~~~~~~";
                default :
                    return "";
            }

        } catch (InputMismatchException Ex) {
            return "Введите число";
        }

    }

    public String countFormOfEducation(CollectionManagement collectionManagement, Integer FormEducation) {

        FormOfEducationValidator I = new FormOfEducationValidator();
        if (I.IsValid(FormEducation.toString())) {
            long b = collectionManagement.getCollection().stream().filter(a -> a.getFormOfEducation().ordinal() > FormEducation).count();
            return "Количество элементов, значение поля formOfEducation которых больше заданного: " + b;
        } else {
            return I.ErrorMessage();
        }
    }

    public String filterBySem(CollectionManagement collectionManagement, Integer Sem) {

        try {
            SemesterValidator I = new SemesterValidator();
            if (I.IsValid(Sem.toString())) {
                long Count = collectionManagement.getCollection().stream().filter(a -> a.getSemesterEnum().ordinal() == Sem).count();

                if( Count == 0) {
                    return "Нет элементов, значения поля semesterEnum которых равно зааднному";
                }
                else {
                    StringBuilder result = new StringBuilder("Элементы, значение поля semesterEnum которых равно заданному:\n");
                    Stream<StudyGroup> b = collectionManagement.getCollection().stream().filter(a -> a.getSemesterEnum().ordinal() == Sem);
                    b.forEach(n -> result.append(n.getId()).append(" ").append(n.getName()).append("\n"));
                    return result.toString();
                }
            } else {
                return I.ErrorMessage();
            }
        } catch (InputMismatchException Ex) {
            return "Введите число";
        }
    }

}
