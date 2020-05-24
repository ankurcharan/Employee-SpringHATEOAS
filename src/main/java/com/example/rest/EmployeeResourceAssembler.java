package com.example.rest;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.stereotype.Component;

@Component
class EmployeeModelAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>> {

    public EntityModel<Employee> toModel(Employee employee) {

        return new EntityModel<>(
                employee,
                linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).all()).withRel("employees")
        );
    }
}


/*
This simple interface has one method: toModel().
It is based on converting a non-resource object (Employee)
into a resource-based object (EntityModel<Employee>).

And by applying Spring Framework’s @Component,
this component will be automatically created when the app starts.
 */