package com.company.server.data_base;

import com.company.server.collection_management_module.LoginCollectionManagement;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBWorker {
    private final LoginCollectionManagement loginCollectionManagement;
    public DBWorker(LoginCollectionManagement loginCollectionManagement) {
        this.loginCollectionManagement = loginCollectionManagement;
    }
    public synchronized void register(String userName, String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                final String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            DatabaseCommunicator.getDatabaseCommunicator().getStatement().execute("INSERT INTO login VALUES" + " (" + "'" + userName + "'"+ "," + "'" + hexString.toString() + "'"+ ")");
            loginCollectionManagement.register(userName, password);
        } catch (SQLException e) {
            System.out.println("Wrong SQL query in Registration");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
//TODO: заменить его добавление в базу данных на свою
    /*public synchronized void addRoute(Route route) throws SQLException{
        PreparedStatement preparedStatement = DatabaseCommunicator.getDatabaseCommunicator().getConnection().prepareStatement("INSERT INTO routes VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        preparedStatement.setInt(1, route.getId().intValue());
        preparedStatement.setString(2,route.getName());
        preparedStatement.setDate(3, new Date(route.getCreationDate().getTime()));
        preparedStatement.setInt(4,route.getCoordinates().getX());
        preparedStatement.setFloat(5,route.getCoordinates().getY());
        preparedStatement.setInt(6,route.getFrom().getX());
        preparedStatement.setInt(7,route.getFrom().getY().intValue());
        preparedStatement.setInt(8,route.getFrom().getZ());
        preparedStatement.setInt(9,Long.valueOf(route.getTo().getX()).intValue());
        preparedStatement.setDouble(10,route.getTo().getY());
        preparedStatement.setInt(11,route.getTo().getZ());
        preparedStatement.setString(12,route.getTo().getName());
        preparedStatement.setDouble(13,route.getDistance());
        preparedStatement.setString(14,route.getRouteCreator());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
    public synchronized void addRouteWithId(Route route) throws SQLException {
        ResultSet resultSet = DatabaseCommunicator.getDatabaseCommunicator().getStatement().executeQuery("SELECT nextval('routes_routeid_seq')");
        resultSet.next();
        route.setId(resultSet.getLong(1));
        addRoute(route);
        resultSet.close();
    }

    public synchronized void updateRoute(Route route) throws SQLException {
        PreparedStatement preparedStatement = DatabaseCommunicator.getDatabaseCommunicator().getConnection().prepareStatement(
                "UPDATE routes SET " +
                        "routename = ?," +
                        "creationdate = ?," +
                        "routecoordinatex = ?," +
                        "routecoordinatey = ?," +
                        "locationfromcoordx = ?," +
                        "locationfromcoordy = ?," +
                        "locationfromcoordz = ?," +
                        "locationtocoordx = ?," +
                        "locationtocoordy = ?," +
                        "locationtocoordz = ?," +
                        "locationtoname = ?," +
                        "distance = ?," +
                        "routecreator = ?" +
                        "WHERE routeid = ?"
        );
        preparedStatement.setString(1,route.getName());
        preparedStatement.setDate(2, new Date(route.getCreationDate().getTime()));
        preparedStatement.setInt(3, route.getCoordinates().getX());
        preparedStatement.setFloat(4,route.getCoordinates().getY());
        preparedStatement.setInt(5,route.getFrom().getX());
        preparedStatement.setInt(6,route.getFrom().getY().intValue());
        preparedStatement.setInt(7,route.getFrom().getZ());
        preparedStatement.setInt(8,Long.valueOf(route.getTo().getX()).intValue());
        preparedStatement.setDouble(9,route.getTo().getY());
        preparedStatement.setInt(10,route.getTo().getZ());
        preparedStatement.setString(11,route.getTo().getName());
        preparedStatement.setDouble(12,route.getDistance());
        preparedStatement.setString(13,route.getRouteCreator());
        preparedStatement.setInt(14, route.getId().intValue());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public synchronized void removeById(Long id) throws SQLException{
        DatabaseCommunicator.getDatabaseCommunicator().getStatement().execute("DELETE FROM routes WHERE routeid = " + id);
    }

     */
}
