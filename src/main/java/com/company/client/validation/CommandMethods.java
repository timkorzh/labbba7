package com.company.client.validation;

import com.company.common.collection_objects.FormOfEducation;
import com.company.common.collection_objects.Semester;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CommandMethods {


    public int removeById() throws InputMismatchException {
        int RemoveById;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите ID группы для удаления");
        RemoveById = scanner.nextInt();
        return RemoveById;
    }

    public int readFormOfEducation() throws InputMismatchException {
        int FormEducation;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите FormOfEducation группы: " + FormOfEducation.GetStringValues());
        FormEducation = scanner.nextInt();
        return FormEducation;
    }

    public String readExecuteFilePath() {
        Scanner scanner = new Scanner(System.in);
        String ExecuteFilePath = scanner.next();
        Path path = Paths.get(ExecuteFilePath);
        return ExecuteFilePath;
    }
    public int readFilterSem() throws InputMismatchException {
        int Sem;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите semester, по которому хотите отсортировать: " + Semester.GetStringValues());
            Sem = scanner.nextInt();
            return Sem;

    }
}
