package com.company.common.collection_objects;

/**
 * Перечисление объектов взаимодействия с конкретными полями Группы.
 */
public enum StudyGroupField {
    NAME("gName", (src, dst) -> dst.setName(src.getName())),
    COORDINATES("gCoords", (src, dst) -> {
        dst.getCoordinates().setX(src.getCoordinates().getX().toString());
        dst.getCoordinates().setY(src.getCoordinates().getY());
    } ),
    STUDENTS_COUNT("stCount", (src, dst) ->
            dst.setStudentsCount(src.getStudentsCount())),
    FORM_OF_EDUCATION("foedu", (src, dst) ->
            dst.setFormOfEducation(src.getFormOfEducation())),
    SEMESTER("sem", (src, dst) ->
            dst.setSemesterEnum(src.getSemesterEnum())),
    ADMIN_NAME("aName", (src, dst) ->
            dst.getGroupAdmin().setName(src.getGroupAdmin().getName())),
    ADMIN_PASSPORT("passport", (src, dst) ->
            dst.getGroupAdmin().setPassportID(src.getGroupAdmin().getPassportID())),
    ADMIN_LOCATION("aCoords", (src, dst) ->
            dst.getGroupAdmin().setLocation(new Location(src.getGroupAdmin().getLocation())));

    private final String scriptName;
    private final DataCopier dataCopier;

    private interface DataCopier {
        void copyData(StudyGroup srcGroup, StudyGroup dstGroup);
    }

    StudyGroupField(String scriptName, DataCopier dataCopier) {
        this.scriptName = scriptName;
        this.dataCopier = dataCopier;
    }

    /**
     * Возвращает ключ, относящийся к данному полю
     * @return ключ, относящийся к данному полю
     */
    public String getScriptName() { return scriptName; }

    /**
     * Возвращает соответствующий объект StudyGroupField по указанному ключу
     * @param scriptName - ключ объекта
     * @return - характеристика поля Группы (объект StudyGroupField)
     * @throws IllegalArgumentException если указанный ключ не относится ни к одному из
     * полей Группы.
     */
    public static StudyGroupField getField(String scriptName)
                                          throws IllegalArgumentException {
        for (StudyGroupField field : StudyGroupField.values()) {
            if  (field.getScriptName().equals(scriptName))
                return field;
        }
        throw new IllegalArgumentException("scriptName " + scriptName +
                " не соответствует ни одному из полей StudyGroup");
    }

    /**
     * Копирует значение соответствующего поля одной Группы в это же поле второй Группы.
     * @param srcGroup - Группа, значение поля которой будет скопировано.
     * @param dstGroup - Группа, значение поля которой будет изменено.
     */
    public void copyValue(StudyGroup srcGroup, StudyGroup dstGroup) {
        dataCopier.copyData(srcGroup, dstGroup);
    }

    /**
     * Копирует значение соответствующих полей одной Группы в эти же поля второй Группы по
     * ключам поля.
     * @param srcGroup - Группа, значения полей которой будут скопированы.
     * @param dstGroup - Группа, значения полей которой будут изменены.
     * @param scriptNames - ключи полей, который должны быть скопированы.
     */
    public static void copyValue(StudyGroup srcGroup, StudyGroup dstGroup, String scriptNames) {
        for (String scriptName : scriptNames.split(" ")) {
            try {
                getField(scriptName).copyValue(srcGroup, dstGroup);
            } catch (IllegalArgumentException ignore) {}
        }
    }
}
