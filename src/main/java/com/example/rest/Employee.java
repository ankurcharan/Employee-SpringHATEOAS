package com.example.rest;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/*
@Data is a Lombok annotation to
create all the getters, setters, equals, hash, and toString methods,
based on the fields.
 */

/*
@Entity is a JPA annotation to make this
object ready for storage in a JPA-based data store.
 */
@Data
@Entity
public class Employee {

    // indicate it’s the primary key and automatically populated
    // by the JPA provider.
    private @Id @GeneratedValue Long id;
    private String firstName;
    private String lastName;
//    private String name;
    private String role;

    Employee() {}

    Employee(String firstName, String lastName, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public String getName() {
        return this.firstName + " " + this.lastName;
    }

    public void setName(String name) {
        String[] parts = name.split(" ");
        this.firstName = parts[0];
        this.lastName = parts[1];
    }

    /*
    A "virtual" getter for the old name property, getName() is defined.
    It uses the firstName and lastName fields to produce a value.

    A "virtual" setter for the old name property is also defined, setName().
    It parses an incoming string and stores it into the proper fields.
     */
}
