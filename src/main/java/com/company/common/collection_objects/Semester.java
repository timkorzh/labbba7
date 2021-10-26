package com.company.common.collection_objects;

import java.io.Serializable;

public enum Semester implements Serializable {
    FIRST,
    THIRD,
    SIXTH,
    EIGHTH;

    public static String GetStringValues() {
        StringBuilder Result = new StringBuilder();
        for (int i = 0; i < values().length; i++ ) {
            Result.append(i).append(" - ").append(values()[i]).append(" | ");
        }
        return Result.toString();
    }
}
