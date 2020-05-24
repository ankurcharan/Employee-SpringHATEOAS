package com.example.rest;

import org.springframework.data.jpa.repository.JpaRepository;

interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // extends Spring Data JPA’s JpaRepository,
    // specifying the domain type as "Employee"
    // and the id type as "Long"

    /*
     though empty on the surface, packs a punch given it supports:
        Creating new instances
        Updating existing ones
        Deleting
        Finding (one, all, by simple or complex properties)
     */
}
