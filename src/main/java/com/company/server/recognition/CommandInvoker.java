package com.company.server.recognition;

import com.company.server.processing.collection_manage.CollectionManagement;
import com.company.common.commands.*;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс для вызова команды
 */
public class CommandInvoker {
    private final HashMap<String, AbstractCommand> hashMap;

    public CommandInvoker(CollectionManagement collectionManagement) {
        this.hashMap = new HashMap<>();
        history = new ArrayDeque<>(7);
        //ClientCommandReceiver clientCommandReceiver = new ClientCommandReceiver(collectionManagement);
        FileCommands fileCommands = new FileCommands(collectionManagement);
        register("help", new HelpCommand(this));
        register("clear", new ClearCommand(collectionManagement));
        register("show", new ShowCommand(collectionManagement));
        register("save", fileCommands.getSaveCommand());
        register("add", new AddCommand(collectionManagement));
        register("history", new HistoryCommand(this));
        register("update", new UpdateCommand(collectionManagement));
        register("min_by_students_count", new MinByStudentsCountCommand(collectionManagement));
        register("remove_by_id", new RemoveByIdCommand(collectionManagement));
        register("load", fileCommands.getLoadCommand());
        register("remove_lower", new RemoveLowerCommand(collectionManagement));
        register("remove_higher", new RemoveHigherCommand(collectionManagement));
        register("count_greater_than_form_of_education", new CountGreaterCommand(collectionManagement));
        register("filter_by_semester_enum", new FilterBySemCommand(collectionManagement));
        register("info", new InfoCommand(collectionManagement));
        register("execute_script", new ExecuteCommand(this));
    }

    public void register(String commandName, AbstractCommand command) {
        hashMap.put(commandName, command);
    }

    public ArrayDeque<String> history;

    public HashMap<String, AbstractCommand> getHashMap() {
        return hashMap;
    }

    public String execute(String inputString) {
        return execute(inputString, null);
    }

    public String execute(String inputString, Object args) {
        String commandName, commandArgs;
        if (!inputString.isEmpty()) {
            commandName ="";
            try {
                commandName = inputString.split("\\s")[0];

            } catch (ArrayIndexOutOfBoundsException ignored) {

            }
            Pattern p = Pattern.compile("\\s(-.*)");
            Matcher m = p.matcher(inputString);
            if (m.find()) {

                commandArgs = m.group(1);
            } else {
                commandArgs = null;
            }

            if (hashMap.containsKey(commandName)) {
                String result = hashMap.get(commandName).execute(commandArgs, args);
                if(commandName.equals("add") || commandName.equals("update") || commandName.equals("clear") || commandName.equals("remove_by_id") || commandName.equals("remove_lower") || commandName.equals("remove_higher")) {
                    this.execute("save");
                    System.out.println("Автоматическое сохранение успешно");
                }
                history.push(commandName);
                if (history.size() > 7) {
                    history.removeLast();
                }
                return result;
            }
        }
            return "Пекарь, " + inputString + " - незарегистрированная команда. Похудей пальчики или напиши help.";
    }
}
