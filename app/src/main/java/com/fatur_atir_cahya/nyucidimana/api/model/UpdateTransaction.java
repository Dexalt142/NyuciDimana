package com.fatur_atir_cahya.nyucidimana.api.model;

public class UpdateTransaction {

    private String id;
    private String code;
    private String status;

    public UpdateTransaction(String id, String code, String status) {
        this.id = id;
        this.code = code;
        this.status = status;
    }

}
