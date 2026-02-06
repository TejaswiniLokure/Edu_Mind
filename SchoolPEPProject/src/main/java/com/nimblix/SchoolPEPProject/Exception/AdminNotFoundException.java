package com.nimblix.SchoolPEPProject.Exception;

public class AdminNotFoundException extends RuntimeException{

    public AdminNotFoundException(String s){
        super("Admin not found");
    }

}
