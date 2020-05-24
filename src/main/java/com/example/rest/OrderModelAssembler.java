package com.example.rest;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static  org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static  org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

/*
Before building the OrderModelAssembler, let’s discuss what needs to happen.
You are modeling the flow of states between Status.IN_PROGRESS, Status.COMPLETED, and Status.CANCELLED.
A natural thing when serving up such data to clients is to let the clients
make the decision on what it can do based on this payload.
 */

@Component
class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {

	@Override
	public EntityModel<Order> toModel(Order order) {

		// Unconditional links to single-item resource and aggregate root
		EntityModel<Order> orderModel = new EntityModel<>(
											order,
											linkTo(methodOn(OrderController.class).one(order.getId())).withSelfRel(),
											linkTo(methodOn(OrderController.class).all()).withRel("orders")
									);

		// Conditional links based on state of the order
		if(order.getStatus() == Status.IN_PROGRESS) {

			orderModel.add(linkTo(methodOn(OrderController.class).cancel(order.getId())).withRel("cancel"));
			orderModel.add(linkTo(methodOn(OrderController.class).complete(order.getId())).withRel("complete"));
		}

		return orderModel;
	}
}
