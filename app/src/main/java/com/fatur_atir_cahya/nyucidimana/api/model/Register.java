package com.fatur_atir_cahya.nyucidimana.api.model;

public class Register {

    private String name;
    private String email;
    private String password;
    private String phone_number;
    private String role;

    public Register(String name, String email, String password, String phone_number, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone_number = phone_number;
        this.role = role;
    }

}
