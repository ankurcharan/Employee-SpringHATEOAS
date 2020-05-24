package com.example.rest;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "CUSTOMER_ORDER")
class Order {

    private @Id @GeneratedValue Long id;

    private String description;
    private Status status;

    Order() {}

    Order(String description, Status status) {

        this.description = description;
        this.status = status;
    }
}


/*
The class requires a JPA @Table annotation changing the
table’s name to CUSTOMER_ORDER because
ORDER is not a valid name for table.

 */
