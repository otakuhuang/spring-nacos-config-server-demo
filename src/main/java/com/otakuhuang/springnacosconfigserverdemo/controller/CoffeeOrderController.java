package com.otakuhuang.springnacosconfigserverdemo.controller;

import com.otakuhuang.springnacosconfigserverdemo.controller.request.NewOrderRequest;
import com.otakuhuang.springnacosconfigserverdemo.model.Coffee;
import com.otakuhuang.springnacosconfigserverdemo.model.CoffeeOrder;
import com.otakuhuang.springnacosconfigserverdemo.service.CoffeeOrderService;
import com.otakuhuang.springnacosconfigserverdemo.service.CoffeeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author otaku
 * @version 1.0
 * @date 2022/2/27 23:56
 * @description description
 */
@RestController
@RequestMapping("/order")
@Log4j2
public class CoffeeOrderController {
    @Autowired
    private CoffeeOrderService coffeeOrderService;
    @Autowired
    private CoffeeService coffeeService;

    @GetMapping("/{id}")
    public CoffeeOrder getOrder(@PathVariable("id") Long id) {
        CoffeeOrder order = coffeeOrderService.get(id);
        log.info("Get Order: {}", order);
        return order;
    }

    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CoffeeOrder create(@RequestBody NewOrderRequest newOrderRequest) {
        log.info("Receive New Order: {}", newOrderRequest);
        Coffee[] coffees = coffeeService.getCoffeeByName(newOrderRequest.getItems())
                .toArray(new Coffee[]{});
        return coffeeOrderService.createOrder(newOrderRequest.getCustomer(), coffees);
    }
}
