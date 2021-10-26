package com.company.server.data_base.AdapterToCollection;

import com.company.common.collection_objects.StudyGroup;
import com.company.server.collection_management_module.*;
import com.company.server.processing.collection_manage.CollectionManagement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdapterToStudyGroupCollection implements BDtoCollectionAdapter{
    private final Statement statementBD;
    private CollectionManagement collectionManagement;
    public AdapterToStudyGroupCollection(Statement statementBD, CollectionManagement collectionManagement ) {
        this.statementBD = statementBD;
        this.collectionManagement = collectionManagement;
    }

    @Override
    public void adapt() throws SQLException {
        ResultSet routeInfo = statementBD.executeQuery("SELECT * FROM studyGroup");
        while (routeInfo.next()) {
            /*LocationFrom locationFrom = new LocationFrom(
                    routeInfo.getInt("locationfromcoordx"),
                    routeInfo.getLong("locationfromcoordy"),
                    routeInfo.getInt("locationfromcoordz"));
            LocationTo locationTo = new LocationTo(
                    routeInfo.getInt("locationtocoordx"),
                    routeInfo.getDouble("locationtocoordy"),
                    routeInfo.getInt("locationtocoordz"),
                    routeInfo.getString("locationtoname"));
            Coordinates coordinates = new Coordinates(
                    routeInfo.getInt("routecoordinatex"),
                    routeInfo.getFloat("routecoordinatey"));
            Route route = new Route(
                    routeInfo.getString("routename"),
                    coordinates,
                    locationFrom,
                    locationTo);
            route.setCreationDate(routeInfo.getDate("creationdate"));
            route.setId(routeInfo.getLong("routeid"));
            route.setRouteCreator(routeInfo.getString("routecreator"));
            routeCollectionManagement.addRoute(route);

             */
        }
    }
}
