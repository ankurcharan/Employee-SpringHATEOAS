package com.example.rest;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
class EmployeeController {

    private final EmployeeRepository repository;
    private final EmployeeModelAssembler assembler;

//    EmployeeController(EmployeeRepository repository) {
//        this.repository = repository;
//    }
    EmployeeController(EmployeeRepository repository, EmployeeModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    // Aggregate root

    /* RPC */
//    @GetMapping("/employees")
//    List<Employee> all() {
//        return repository.findAll();
//    }

    /* REST */
    @GetMapping("/employees")
    CollectionModel<EntityModel<Employee>> all() {

//        List<EntityModel<Employee>> employees = repository.findAll().stream()
//                                                    .map(employee -> new EntityModel<>(
//                                                            employee,
//                                                            linkTo(methodOn(EmployeeController.class).one(employee.getId()))
//                                                            .withSelfRel(),
//
//                                                            linkTo(methodOn(EmployeeController.class).all()).withRel("employees")
//                                                    ))
//                                                    .collect(Collectors.toList());
//
//        return new CollectionModel<>(
//                employees,
//                linkTo(methodOn(EmployeeController.class).all()).withSelfRel()
//        );

        List<EntityModel<Employee>> employees = repository.findAll().stream()
                                                    .map(assembler::toModel)
                                                    .collect(Collectors.toList());

        return new CollectionModel<>(
                employees,
                linkTo(methodOn(EmployeeController.class).all()).withSelfRel()
        );
    }

    /*
    CollectionModel<> is another Spring HATEOAS container aimed at encapsulating collections.
    It, too, also lets you include links.
    **Don’t let that first statement slip by.**

    Since we’re talking REST, it should encapsulate collections of employee resources.
    That’s why you fetch all the employees,
    but then transform them into a list of EntityModel<Employee> objects.
    (Thanks Java 8 Stream API!)
     */
    /*
    For this aggregate root, which serves up a collection of employee resources,
    there is a top-level "self" link.
    The "collection" is listed underneath the "_embedded" section. This is how HAL represents collections.
     */

    /* TO HANDLE OLD API (name)*/
//    @PostMapping("/employees")
//    Employee newEmployee(@RequestBody Employee newEmployee) {
//        return repository.save(newEmployee);
//    }

    /* TO HANDLE OLD+NEW (firstName, lastName) */
    @PostMapping("/employees")
    ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) throws URISyntaxException {

        // The new Employee object is saved as before.
        // But the resulting object is wrapped using the EmployeeModelAssembler.
        EntityModel<Employee> entityModel = assembler.toModel(repository.save(newEmployee));

        // Spring MVC’s ResponseEntity is used to create an HTTP 201 Created status message.
        // This type of response typically includes a Location response header,
        // and we use the URI derived from the model’s self-related link.
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
        // Additionally, return the resource-based version of the saved object.
    }

    // Single item

    /* RPC */
//    @GetMapping("/employees/{id}")
//    Employee one(@PathVariable Long id) {
//
//        return repository.findById(id)
//                .orElseThrow(() -> new EmployeeNotFoundException(id));
//    }

    /* REST */
    @GetMapping("/employees/{id}")
    EntityModel<Employee> one(@PathVariable Long id) {

        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

//        return new EntityModel<>(employee,
//                    linkTo(methodOn(EmployeeController.class).one(id))
//                            .withSelfRel(),
//
//                    linkTo(methodOn(EmployeeController.class).all())
//                            .withRel("employees")
//                );

        // instead of creating the EntityModel<Employee> instance here, you delegate it to the assembler
        return assembler.toModel(employee);
    }

    /*
    The return type of the method has changed from
    "Employee" to "EntityModel<Employee>".
    EntityModel<T> is a generic container from Spring HATEOAS
    that includes not only the data but a collection of links.

    linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel()
    asks that Spring HATEOAS build a link to the
    EmployeeController 's one() method,
    and flag it as a self link.

    linkTo(methodOn(EmployeeController.class).all()).withRel("employees")
    asks Spring HATEOAS to build a link to the aggregate root, all(),
    and call it "employees".
     */

    /*
    One of Spring HATEOAS’s core types is Link.
    It includes a URI and a rel (relation).
    Links are what empower the web.
    Before the World Wide Web, other document systems would render information or links,
    but it was the linking of documents WITH data that stitched the web together.
     */

    /*
    This entire document is formatted using HAL.
    HAL is a lightweight mediatype that allows encoding not just
    data but also hypermedia controls, alerting consumers to other parts
    of the API they can navigate toward.
    In this case, there is a "self" link (kind of like a this statement in code)
    along with a link back to the aggregate root.
     */

    /* OLD name */
//    @PutMapping("/employees/{id}")
//    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
//
//        return repository.findById(id)
//                .map(employee -> {
//                    employee.setName(newEmployee.getName());
//                    employee.setRole(newEmployee.getRole());
//
//                    return repository.save(employee);
//                })
//                .orElseGet(() -> {
//                    newEmployee.setId(id);
//                    return repository.save(newEmployee);
//                });
//    }

    /* NEW firstName+lastName */
    @PutMapping("/employees/{id}")
    ResponseEntity<?> replaceEmployee(@ResponseBody Employee newEmployee, @PathVariable Long id) throws URISyntaxException {

        Employee updatedEmployee = repository.findById(id)
                                    .map(employee -> {
                                        employee.setName(newEmployee.getName());
                                        employee.setRole(newEmployee.getRole());

                                        return repository.save(employee);
                                    })
                                    .orElseGet(() -> {
                                        newEmployee.setId(id);

                                        return repository.save(newEmployee);
                                    });

        EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

//    @DeleteMapping("/employees/{id}")
//    void deleteEmployee(@PathVariable Long id) {
//        repository.deleteById(id);
//    }

    @DeleteMapping("/employees/{id}")
    ResponseEntity<?> deleteEmployees(@PathVariable Long id) {

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

/*
@RestController indicates that the data returned by each method will be
written straight into the response body instead of rendering a template.
 */

/*
An EmployeeRepository is injected by constructor into the controller.
 */

/*
routes for each operations
(@GetMapping, @PostMapping, @PutMapping and @DeleteMapping),
corresponding to HTTP
(GET,          POST,        PUT, and        DELETE)
 */

/*
EmployeeNotFoundException is an exception
used to indicate when an employee is looked up but not found.
 */