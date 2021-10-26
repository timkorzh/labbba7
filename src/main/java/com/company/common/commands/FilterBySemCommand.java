package com.company.common.commands;

import com.company.client.validation.CommandMethodsExecute;
import com.company.server.processing.collection_manage.CollectionManagement;

public class FilterBySemCommand extends AbstractCommand {

    CollectionManagement collectionManagement;
    public FilterBySemCommand(CollectionManagement collectionManagement) {
        this.collectionManagement = collectionManagement;
        }
        @Override
        public String execute(String CommandArgs) {
            return "Укажите аргументы для фильтрации";
    }
    @Override
    public String execute(String strArgs, Object CommandArgs) {
        try {
            CommandMethodsExecute methodsExecute = new CommandMethodsExecute();
            int FBS = Integer.parseInt(CommandArgs.toString());
            return methodsExecute.filterBySem(collectionManagement, FBS);
        } catch (Exception e) {
            return e.getLocalizedMessage();
        }
    }

        @Override
        public String describe() {
            return ("Выводит элементы, значение поля semesterEnum которых равно заданному");
        }
    }
