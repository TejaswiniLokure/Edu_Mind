package com.nimblix.SchoolPEPProject.Util;

public enum UserType {
    ADMIN("A"),
    TEACHER("T"),
    STUDENT("S"),
    PARENT("P");

    private final String prefix;

    UserType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
