package com.company.common.commands;

import com.company.server.processing.collection_manage.CollectionManagement;

public class InfoCommand extends AbstractCommand {
    CollectionManagement collectionManagement;

    public InfoCommand(CollectionManagement collectionManagement) {
        this.collectionManagement = collectionManagement;
    }

        @Override
        public String execute(String CommandArgs) {
            return "Вывожу информацию...." + collectionManagement.info() + "и.т.д.";
        }

        @Override
        public String describe() {
        return ("Выводит в стандартный поток вывода информацию о коллекции");
    }
    }
