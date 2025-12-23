package com.nimblix.SchoolPEPProject.Util;

public class UserIdGeneratorUtil {

    private UserIdGeneratorUtil() {
        // prevent instantiation
    }

    /**
     * Generates business userId like T12, S105, A1
     */
    public static String generateUserId(com.nimblix.SchoolPEPProject.Util.UserType userType, Long dbId) {

        if (dbId == null) {
            throw new IllegalArgumentException("DB id cannot be null");
        }

        return userType.getPrefix() + dbId;
    }

    /**
     * Optional: zero padded IDs (T001, S045)
     */
    public static String generateUserId(com.nimblix.SchoolPEPProject.Util.UserType userType, Long dbId, int padding) {

        if (dbId == null) {
            throw new IllegalArgumentException("DB id cannot be null");
        }

        return userType.getPrefix()
                + String.format("%0" + padding + "d", dbId);
    }
}
