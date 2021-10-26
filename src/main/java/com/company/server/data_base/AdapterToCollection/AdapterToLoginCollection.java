package com.company.server.data_base.AdapterToCollection;

import com.company.server.collection_management_module.LoginCollectionManagement;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdapterToLoginCollection implements BDtoCollectionAdapter{
    private final Statement statementBD;
    private final LoginCollectionManagement loginCollectionManagement;
    public AdapterToLoginCollection(Statement statementBD, LoginCollectionManagement loginCollectionManagement) {
        this.statementBD = statementBD;
        this.loginCollectionManagement = loginCollectionManagement;
    }

    @Override
    public void adapt() throws SQLException {
        ResultSet userInfo = statementBD.executeQuery("SELECT * FROM login");
        while (userInfo.next()) {
            loginCollectionManagement.register(userInfo.getString(1),userInfo.getString(2));
        }
    }
}
