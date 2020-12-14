package com.fatur_atir_cahya.nyucidimana.api.model;

public class CreateTransaction {

    private String weight;
    private String price;

    public CreateTransaction(String weight, String price) {
        this.weight = weight;
        this.price = price;
    }

}
