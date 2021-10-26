package com.company.server;

import com.company.common.collection_objects.StudyGroupField;
import com.company.server.processing.collection_manage.*;
import com.company.common.collection_objects.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

interface IValidator {
    boolean isValid(String input);

    String errorMessage();
}


class NameValidator implements IValidator {

    @Override
    public boolean isValid(String input) {
        return input.matches("\\w[\\w\\s]*");
    }

    @Override
    public String errorMessage() {
        return "Введите любые символы кроме пустой строки и пробела";
    }
}

class CoordinatesValidator implements IValidator {

    @Override
    public boolean isValid(String input) {

        return input.matches("\\d+; ?[-]?[0-9]*\\.?,?[0-9]+");
    }

    @Override
    public String errorMessage() {
        return "Введите координаты в формате X; Y";
    }
}

class StudentsCountValidator implements IValidator {

    @Override
    public boolean isValid(String input) {
         return input.matches("[1-9]\\d{0,8}");
    }

    @Override
    public String errorMessage() {
        return "Введите число";
    }
}

class FormOfEducationValidator implements IValidator {


    @Override
    public boolean isValid(String input) {
        try {
            Integer.parseInt(input);
            return(Integer.parseInt(input) < FormOfEducation.values().length);
        } catch (NumberFormatException e) {
            return  false;
        }
    }

    @Override
    public String errorMessage() {
        return "Введите одно из указанных чисел, уважаемый пекарь";
    }
}

class SemesterValidator implements IValidator {
    @Override
    public boolean isValid(String input) {
        try {
            Integer.parseInt(input);
            return(Integer.parseInt(input) < Semester.values().length);
        } catch (NumberFormatException e) {
            return  false;
        }
    }

    @Override
    public String errorMessage() {
        return "Введите одно из указанных чисел, уважаемый пекарь";
    }
}

class AdminNameValidator implements IValidator {

    @Override
    public boolean isValid(String input) {
        return input.matches("([A-Za-zА-Яа-я\\-]+)\\s*([A-Za-zА-Яа-я]+)");
    }

    @Override
    public String errorMessage() {
        return "Введите имя в соответствии с форматом, уважаемый пекарь";
    }
}
class AdminPassportValidator implements IValidator {

    @Override
    public boolean isValid(String input) {
        return input.matches("^\\d{4}\\s\\d{6}");
    }

    @Override
    public String errorMessage() {
        return "Введите паспортные данные в соответствии с форматом, уважаемый пекарь";
    }
}
class AdminLocationValidator implements IValidator {

    @Override
    public boolean isValid(String input) {
        return input.matches("\\d+\\.?,?\\d*; ?\\d+;? ?[-]?\\d*\\.?,?\\d*");
    }

    @Override
    public String errorMessage() {
        return "Введите координаты в соответствии с форматом, уважаемый пекарь";
    }
}

/**
 * Класс для чтения введённых объектов
 */
@Deprecated
public class InputDevice {
    //TODO: убрать лишние методы. Перенести их в коллекшМанаджмент

    /**
     * Класс содержащий вопрос и дополнительные параметры вопроса. Неизменяемый класс.
     */
    private static class Question {

        final String question; // формулировка вопроса
        final IValidator validator; // валидатор введённого значения
        final String scriptName; // скрипт-ключ поля, к которому относится вопрос
        final boolean important; // важность вопроса
        final boolean permitNulls; // допустим ли null в качестве значения поля

        private Question(String question, IValidator validator, String scriptName, boolean important, boolean permitNulls) {
            this.question = question;
            this.validator = validator;
            this.scriptName = scriptName;
            this.important = important;
            this.permitNulls = permitNulls;
        }

        /**
         * Ответ на конкретный вопрос. На один вопрос может ссылаться несколько ответов,
         * но каждый ответ ссылается только на один вопрос.
         */
        private class Answer {
            String value;
            boolean answered; // Был ли получен ответ

            Answer() { this.answered = false; }

            Answer(String value) {
                this.value = value;
                this.answered = true;
            }

            /**
             * Возвращает вопрос, к которому относится данный ответ
             * @return вопрос, к которому относится данный ответ
             */
            Question getQuestion() { return Question.this; }
        }
    }

    /**
     * Создает Лист неотвеченных ответов к каждому вопросу
     * @param questions Лист вопросов для которых нужно создать ответы
     * @return Лист с пустыми ответами
     */
    private static ArrayList<Question.Answer> initAnswers(ArrayList<Question> questions) {
        ArrayList<Question.Answer> answers = new ArrayList<>(questions.size());
        for (Question question : questions) {
            answers.add(question.new Answer());
        }
        return answers;
    }

    /**
     * Лист вопросов
     */
    private static ArrayList<Question> quiz = generateQuestions();

    private static ArrayList<Question> generateQuestions() {
        if (quiz != null) return quiz;
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(new Question("Имя группы", new NameValidator(), StudyGroupField.NAME.getScriptName(), true, false));
        questions.add(new Question("Координаты группы в формате X(1); Y(1,0)", new CoordinatesValidator(), StudyGroupField.COORDINATES.getScriptName(), true, false));
        questions.add(new Question("Число студентов в группе", new StudentsCountValidator(), StudyGroupField.STUDENTS_COUNT.getScriptName(), true, false));
        questions.add(new Question("Форма обучения " + FormOfEducation.GetStringValues(), new FormOfEducationValidator(), StudyGroupField.FORM_OF_EDUCATION.getScriptName(), false, true));
        questions.add(new Question("Семестр " + Semester.GetStringValues(), new SemesterValidator(), StudyGroupField.SEMESTER.getScriptName(), true, false));
        questions.add(new Question("Имя админа группы(Фамилия Имя)", new AdminNameValidator(), StudyGroupField.ADMIN_NAME.getScriptName(), true, true));
        questions.add(new Question("Серия и номер паспорта(пример: 1234 123456) админа группы", new AdminPassportValidator(), StudyGroupField.ADMIN_PASSPORT.getScriptName(), false, true));
        questions.add(new Question("Введите координаты в формате X(0,0); Y(0); Z(-1,0)", new AdminLocationValidator(), StudyGroupField.ADMIN_LOCATION.getScriptName(), false, true));
        return questions;
    }

    public static String getScriptName() {

        StringBuilder Keys = new StringBuilder("\n" + "Ключи для ввода данных: ");
        for (int i = 0; i < 8; i++) {
          Keys.append(String.format("%n-%-45s  %-45s", quiz.get(i).scriptName, "[" + quiz.get(i).question + "]"));
        }
        return Keys.toString();
    }

    public static int removeById() throws InputMismatchException {
        int RemoveById;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите ID группы для удаления");
        RemoveById = scanner.nextInt();
        return RemoveById;
    }
    public enum RemoveMode {
        High,
        Low,
        Equals
    }

    public static void remove(CollectionManagement collectionManagement, int RemoveById, RemoveMode Equals) {
        try {
            if (collectionManagement.getCollection().stream().noneMatch(a -> a.getId() == RemoveById)) {
                System.out.println("Ничего не нашёл по этому номеру:((");
                return;
            }
            switch (Equals) {
                case Equals : {collectionManagement.getCollection().removeIf(studyGroup -> RemoveById == studyGroup.getid());
                System.out.println("Группа с id: " + RemoveById + " была успешно удалена! ~~~~~~~~~~~Помянем~~~~~~~~~~");}
                case Low : {collectionManagement.getCollection().removeIf(a -> a.getId() < RemoveById);
                System.out.println("Группа с id ниже, чем: " + RemoveById + " была успешно удалена! ~~~~~~~~~~~Помянем~~~~~~~~~~");}
                case High : {collectionManagement.getCollection().removeIf(a -> a.getId() > RemoveById);
                System.out.println("Группы с id выше, чем: " + RemoveById + " была успешно удалена! ~~~~~~~~~~~Помянем~~~~~~~~~~");}
            }

        } catch (InputMismatchException Ex) {
            System.out.println("Введите число");
        }

    }

    public static int readFormOfEducation() throws InputMismatchException {
        int FormEducation;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите FormOfEducation группы: " + FormOfEducation.GetStringValues());
        FormEducation = scanner.nextInt();
        return FormEducation;
    }

    public static void countFormOfEducation(CollectionManagement collectionManagement, Integer FormEducation) {

        FormOfEducationValidator I = new FormOfEducationValidator();
        if (I.isValid(FormEducation.toString())) {
            long b = collectionManagement.getCollection().stream().filter(a -> a.getFormOfEducation().ordinal() > FormEducation).count();
            System.out.println("Количество элементов, значение поля formOfEducation которых больше заданного: " + b);
        } else {
            System.out.println(I.errorMessage());
        }
    }

    public static Path readExecuteFilePath() {

        Scanner scanner = new Scanner(System.in);
        String ExecuteFilePath = scanner.next();
        Path path = Paths.get(ExecuteFilePath);
        if (!((new File(path.toString())).exists())) {
            System.out.println("Не нашёл такой файл, пекарб((");
            return null;
        } else {
            return path;
        }


    }
    public static int readFilterSem() {
        int Sem;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите semester, по которому хотите отсортировать: " + Semester.GetStringValues());
        Sem = scanner.nextInt();
        return Sem;

    }
    public static void filterBySem(CollectionManagement collectionManagement, Integer Sem) {

        try {
            SemesterValidator I = new SemesterValidator();
            if (I.isValid(Sem.toString())) {
               long Count = collectionManagement.getCollection().stream().filter(a -> a.getSemesterEnum().ordinal() == Sem).count();

                if( Count == 0) {
                    System.out.println("Нет элементов, значения поля semesterEnum которых равно зааднному");
                }
                else {
                    Stream<StudyGroup> b = collectionManagement.getCollection().stream().filter(a -> a.getSemesterEnum().ordinal() == Sem);
                    b.forEach(n -> System.out.println("Элементы, значение поля semesterEnum которых равно заданному: " + n.getId() + " " + n.getName()));
                }
            } else {
                System.out.println(I.errorMessage());
            }
        } catch (InputMismatchException Ex) {
            System.out.println("Введите число");
        }
    }

    /**
     * Интерфейс для преобразования строки в значение поля и присвоения этого значения
     * полю объекта. Для каждого поля создаётся своя реализация интерфейса.
     */
    private interface StudyGroupFieldParser {
        /**
         * Преобразует строку в значение поля и присваивает это значения нужному полю объекта
         * @param studyGroup Объект полю, которого необходимо присвоить значение
         * @param value Значение в строковом представлении
         */
        void parseValue(StudyGroup studyGroup, String value);
    }

    /**
     * Таблица парсеров для всех полей. Ключи -- скрипт-ключи полей, значения -- соответсвующие парсеры.
     */
    private final static HashMap<String, StudyGroupFieldParser> fieldParsers = fillFieldParsers();

    private static HashMap<String, StudyGroupFieldParser> fillFieldParsers() {
        HashMap<String, StudyGroupFieldParser> map = new HashMap<>();
        map.put(StudyGroupField.NAME.getScriptName(), StudyGroup::setName);
        map.put(StudyGroupField.COORDINATES.getScriptName(),
                (studyGroup, value) -> studyGroup.getCoordinates().parseString(value));
        map.put(StudyGroupField.STUDENTS_COUNT.getScriptName(),
                (studyGroup, value) -> studyGroup.setStudentsCount(Integer.parseInt(value)));
        map.put(StudyGroupField.FORM_OF_EDUCATION.getScriptName(),
                (studyGroup, value) -> studyGroup.setFormOfEducation
                        ((value == null) ? null : FormOfEducation.values()[Integer.parseInt(value)]));
        map.put(StudyGroupField.SEMESTER.getScriptName(),
                (studyGroup, value) -> studyGroup.setSemesterEnum(Semester.values()[Integer.parseInt(value)]));
        map.put(StudyGroupField.ADMIN_NAME.getScriptName(),
                (studyGroup, value) -> {
            if (value == null)
                studyGroup.setGroupAdmin(null);
            else if (studyGroup.getGroupAdmin() == null)
                studyGroup.setGroupAdmin(new Person(value));
            else studyGroup.getGroupAdmin().setName(value);
        });
        map.put(StudyGroupField.ADMIN_PASSPORT.getScriptName(),
                (studyGroup, value) -> studyGroup.getGroupAdmin().setPassportID(value));
        map.put(StudyGroupField.ADMIN_LOCATION.getScriptName(),
                (studyGroup, value) -> studyGroup.getGroupAdmin().
                        setLocation((value == null) ? null : new Location(value)));
        return map;
    }

    /**
     * Выводит все ответы из списка
     * @param answers список ответов, которые надо вывести
     */
    private static void printQuizAnswers(ArrayList<Question.Answer> answers) {
        for (Question.Answer answer : answers) {
            System.out.println(answer.value);
        }
    }

    /**
     * Заполняет все поля объекта, для которых есть отвеченный ответ в списке
     * @param studyGroup - объект, поля которого надо заполнить
     * @param answers - список ответов
     * @return строка состоящая из скрипт-ключей полей, заполненных в результате
     * выполнения этого метода
     */
    private static String assignQuizAnswers(StudyGroup studyGroup, ArrayList<Question.Answer> answers) {
        StringBuilder keysBuilder = new StringBuilder();
        for (Question.Answer answer : answers) {
            String key = answer.getQuestion().scriptName;
            if (answer.answered) {
                fieldParsers.get(key).parseValue(studyGroup, answer.value);
                keysBuilder.append(" ").append(key);
            }
        }
        return keysBuilder.toString().trim();
    }

    /**
     * Проверка строкового значения на валидаторе и запись его в ответ. Если значением
     * является пустая строка и вопрос допускает null, то в ответ будет запиан null
     * @param answer - ответ, в который нужно записать значение
     * @param value - строковое значение, которое надо записать
     * @return правда, если строковое значение прошло проверку и было записано. Ложь, если
     * проверка не была пройдена
     */
    private static boolean acceptAnswer(Question.Answer answer, String value) {
        IValidator validator = answer.getQuestion().validator;
        if (validator != null && !validator.isValid(value)) {
            return false;
        } else {
            if (value.equals("") && answer.getQuestion().permitNulls)
                answer.value = null;
            else
                answer.value = value;
            answer.answered = true;
            return true;
        }
    }

    /**
     * Изменение полей Группы из стандартного потока ввода.
     * @param studyGroup - Группа, значения полей которой надо изменить
     * @param skipQuestionExpr - Выражение, при вводе которого поле не будет изменено. Если
     *                         выражение null, то все поля должны обязательно заполняться.
     * @return строка состоящая из скрипт-ключей полей, заполненных в результате
     * выполнения этого метода
     */
    private static String fillGroupFromSysIn(StudyGroup studyGroup, String skipQuestionExpr) {
        ArrayList<Question.Answer> answers = initAnswers(quiz);
        Scanner scanner = new Scanner(System.in);
        boolean groupAdminIsNull = studyGroup.getGroupAdmin() == null;

        for (int i = 0; i < answers.size(); i++) {
            Question.Answer answer = answers.get(i);
            String scriptName = answer.getQuestion().scriptName;
            //Если нет админа группы, то пропускаем его поля
            if ((scriptName.equals(StudyGroupField.ADMIN_LOCATION.getScriptName()) ||
                    scriptName.equals(StudyGroupField.ADMIN_PASSPORT.getScriptName())) &&
                            groupAdminIsNull)
                continue;

            boolean ResultOK = false;
            //до тех пор пока не ввел правильное
            while (!ResultOK) {
                System.out.println("(" + (i + 1) + "/" + answers.size()+ ")" + quiz.get(i));
                String name = scanner.nextLine();
                //Проверяем надо ли что-то менять
                if (skipQuestionExpr != null && name.equals(skipQuestionExpr)) {
                    ResultOK = true;
                }
                else {
                    if (name.equals("exitcmd")) {
                        System.out.println("Выходим из команды ввода, пекарь");
                        return "";
                    }
                    else {
                        ResultOK = acceptAnswer(answer, name); //Проверка ответа на валидаторе и запись его в answer
                        if (!ResultOK)
                            System.out.println(answer.getQuestion().validator.errorMessage());
                    }
                }
            }
            if (answer.getQuestion().scriptName.equals(StudyGroupField.ADMIN_NAME.getScriptName()) && answer.answered) {
                groupAdminIsNull = answer.value == null; //Если только что было введено имя админа и его значение null, то админа нет
            }
        }

        printQuizAnswers(answers);

        return assignQuizAnswers(studyGroup, answers); //Заполняем значения полей Группы полученными ответами
    }

    /**
     * Изменение полей Группы из передаваемого строкового аргумента. Аргумент строится по
     * шаблону: "-ключзначение -ключзначение" и т.д. Ключи определены в StudyGroupField.
     * Нераспознанные ключи игнорируются.
     * @param studyGroup - Группа, значения полей которой надо изменить
     * @param commandArgs - строковый аргумент состоящий из ключей и значений, построенный
     *                    по описанному выше шаблону
     * @param importantQuestions - обязательно ли в строковом аргументе должны быть
     *                           указаны важные поля. При инициализации новой группы
     *                           устанавливается истина, при изменении полей уже
     *                           существующей группы - ложь. Если установлена истина и
     *                           среди значений аргумента нет значения поля, для которого
     *                           Question.important == true, то все поля Группы останутся
     *                           без изменений и метод вернёт false.
     * @return результат выполнения метода. Если значения полей были изменены, результат -
     * true. Если выполнение метода было прервано без изменения полей Группы, будет
     * возвращено значение false.
     */
    private static boolean fillGroupFromFile(StudyGroup studyGroup, String commandArgs, boolean importantQuestions) {
        //TODO: убедиться, что во всех случаях, когда выполнение метода прерывается без
        // изменения полей Группы, возвращается false.
        ArrayList<Question.Answer> answers = initAnswers(quiz);
        boolean groupAdminIsNull = studyGroup.getGroupAdmin() == null;
        boolean importantQuestionMistake = false;

        for(Question.Answer answer : answers) {
            String scriptName = answer.getQuestion().scriptName;
            //Если нет админа группы, то пропускаем его поля
            if ((scriptName.equals(StudyGroupField.ADMIN_LOCATION.getScriptName()) ||
                    scriptName.equals(StudyGroupField.ADMIN_PASSPORT.getScriptName())) &&
                    groupAdminIsNull)
                continue;

            Pattern p = Pattern.compile("-" + answer.getQuestion().scriptName + "(.*?)( -|$)");
            Matcher m = p.matcher(commandArgs);
            if (m.find()) {
                String foundAnswer = m.group(1).trim();
                //Проверка ответа на валидаторе и запись его в answer
                if (!acceptAnswer(answer, foundAnswer) && answer.getQuestion().important) {
                    importantQuestionMistake = true;
                }
            }
            else {
                if(answer.getQuestion().important) {
                    importantQuestionMistake = true;
                }
            }

            if (answer.getQuestion().scriptName.equals(StudyGroupField.ADMIN_NAME.getScriptName()) && answer.answered) {
                groupAdminIsNull = answer.value == null; //Если только что было введено имя админа и его значение null, то админа нет
            }
        }
        if (importantQuestionMistake && importantQuestions) {
            System.out.println("Не получен(Ы) ответы на важные вопросы, идите лесом");
            return false;
        }

        printQuizAnswers(answers);

        assignQuizAnswers(studyGroup, answers); //Заполняем значения полей Группы полученными ответами
        return true;
    }

    /**
     * Изменение полей Группы из стандартного потока ввода. Чтобы оставить поле неизменным,
     * на соответствующий вопрос вводится строка "N".
     * @param studyGroup - Группа, значения полей которой надо изменить
     * @return строка состоящая из скрипт-ключей полей, заполненных в результате
     *      * выполнения этого метода
     */
    public static String edit(StudyGroup studyGroup) {
        return fillGroupFromSysIn(studyGroup, "N");
    }

    /**
     * Изменение полей Группы из передаваемого строкового аргумента. Аргумент строится по
     * шаблону: "-ключзначение -ключзначение" и т.д. Ключи определены в StudyGroupField.
     * Нераспознанные ключи игнорируются.
     * @param studyGroup - Группа, значения полей которой надо изменить
     * @param commandArgs - строковый аргумент состоящий из ключей и значений, построенный
     *                    по описанному выше шаблону
     */
    public static void editFromFile(StudyGroup studyGroup, String commandArgs) {
        fillGroupFromFile(studyGroup, commandArgs, false);
    }

    /**
     * Задание новой Группы из стандартного потока ввода. Все поля должны быть заполнены.
     * @return Группа с полями, считанными из стандартного потока ввода. Если ввод Группы
     * был прерван будет возвращено значение null.
     */
    public static StudyGroup input() {
        StudyGroup studyGroup = new StudyGroup();
        studyGroup.setGroupAdmin(null);
        if (fillGroupFromSysIn(studyGroup, null).equals(""))
            return null;
        return studyGroup;
    }

    /**
     * Задание новой Группы из передаваемого строкового аргумента.
     * Значения полей для которых Question.important == true должны быть указаны в
     * аргументе. Аргумент строится по шаблону: "-ключзначение -ключзначение" и т.д.
     * Ключи определены в StudyGroupField. Нераспознанные ключи игнорируются.
     * @param commandArgs - строковый аргумент состоящий из ключей и значений, построенный
     *                    по описанному выше шаблону
     * @return Группа с полями, считанными из переданного строкового аргумента. Если в
     * аргументе не указаны все необходимые поля, или группа не была проинициализирована
     * по другой причине, возвращается значение null.
     */
    public static StudyGroup inputFromFile(String commandArgs) {
        StudyGroup studyGroup = new StudyGroup();
        if (fillGroupFromFile(studyGroup, commandArgs, true))
            return studyGroup;
        return null;
    }

    public static void main(String[] args) {
        System.out.println(FormOfEducation.valueOf(null));
    }
}
 