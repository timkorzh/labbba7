package com.company.server.data_base;

import com.company.server.data_base.db_url.DBurl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseCommunicator {
    private boolean isWorkingDB = false;
    private Connection connection;
    private Statement statement;
    private static DatabaseCommunicator databaseCommunicator;
    private DatabaseCommunicator() {
        this.connection = null;
    }

    public static DatabaseCommunicator getDatabaseCommunicator() {
        if (databaseCommunicator == null) databaseCommunicator = new DatabaseCommunicator();
        return databaseCommunicator;
    }

    public void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DBurl.getDbUrl(), DBurl.getUSER(), DBurl.getPASS());
            statement = connection.createStatement();
            //TODO: заменить на создание моих таблиц(можно сгенерировать запрос в pgAdmin)
            statement.execute("create table if not exists routes\n" +
                    "(\n" +
                    "    routeid            serial not null\n" +
                    "        constraint routes_pkey\n" +
                    "            primary key,\n" +
                    "    routename          varchar(20),\n" +
                    "    creationdate       timestamp,\n" +
                    "    routecoordinatex   integer,\n" +
                    "    routecoordinatey   real,\n" +
                    "    locationfromcoordx integer,\n" +
                    "    locationfromcoordy integer,\n" +
                    "    locationfromcoordz integer,\n" +
                    "    locationtocoordx   integer,\n" +
                    "    locationtocoordy   double precision,\n" +
                    "    locationtocoordz   integer,\n" +
                    "    locationtoname     varchar(20),\n" +
                    "    distance           double precision\n," +
                    "    routecreator       varchar(20)\n" +
                    ")");
            statement.execute("create table if not exists login\n" +
                    "(\n" +
                    "    username     varchar(20) not null\n" +
                    "    constraint login_pkey\n" +
                    "    primary key,\n" +
                    "    userpassword varchar(100)\n" +
                    "    )");
            statement.execute("create sequence if not exists\n" +
                    " public.id_studygroup\n" +
                    " INCREMENT 1\n" +
                    " START 1\n" +
                    " MINVALUE 1\n" +
                    " MAXVALUE 9223372036854775807\n" +
                    " CACHE 1;");
            isWorkingDB = true;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Не установлено соединение с базой данных");
         }
    }

    public Connection getConnection() {
        return connection;
    }
    
    public Statement getStatement() {
        return statement;
    }

    public boolean isWorkingDB() {
        return isWorkingDB;
    }

    public void setWorkingDB(boolean workingDB) {
        isWorkingDB = workingDB;
    }
}
