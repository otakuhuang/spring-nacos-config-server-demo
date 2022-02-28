package com.otakuhuang.springnacosconfigserverdemo.service;

import com.otakuhuang.springnacosconfigserverdemo.model.Coffee;
import com.otakuhuang.springnacosconfigserverdemo.model.CoffeeOrder;
import com.otakuhuang.springnacosconfigserverdemo.model.OrderState;
import com.otakuhuang.springnacosconfigserverdemo.repository.CoffeeOrderRepository;
import com.otakuhuang.springnacosconfigserverdemo.support.OrderProperties;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import lombok.extern.log4j.Log4j2;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.beans.ExceptionListener;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author otaku
 * @version 1.0
 * @date 2022/2/27 23:30
 * @description description
 */
@Service
@Log4j2
@Transactional(rollbackOn = Exception.class)
public class CoffeeOrderService implements MeterBinder {

    @Autowired
    private CoffeeOrderRepository coffeeOrderRepository;
    @Autowired
    private OrderProperties orderProperties;
    private String waiterId = UUID.randomUUID().toString();

    private Counter orderCounter = null;

    public CoffeeOrder get(Long id) {
        return coffeeOrderRepository.getById(id);
    }

    public CoffeeOrder createOrder(String customer, Coffee... coffees) {
        CoffeeOrder order = CoffeeOrder.builder()
                .customer(customer)
                .items(new ArrayList<>(Arrays.asList(coffees)))
                .discount(orderProperties.getDiscount())
                .total(calcTotal(coffees))
                .state(OrderState.INIT)
                .waiter(orderProperties.getWaiterPrefix() + waiterId)
                .build();
        CoffeeOrder saved = coffeeOrderRepository.save(order);
        log.info("New Order: {}", saved);
        orderCounter.increment();
        return saved;
    }

    public boolean updateState(CoffeeOrder order, OrderState state) {
        if (state.compareTo(order.getState()) <= 0) {
            log.warn("Wrong State Order: {}, {}", state, order.getState());
            return false;
        }
        order.setState(state);
        coffeeOrderRepository.save(order);
        log.info("Updated Order: {}", order);
        return true;
    }

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        this.orderCounter = meterRegistry.counter("order.count");
    }

    private Money calcTotal(Coffee... coffees) {
        List<Money> items = Stream.of(coffees)
                .map(Coffee::getPrice)
                .collect(Collectors.toList());
        return Money.total(items).multipliedBy(orderProperties.getDiscount())
                .dividedBy(100, RoundingMode.HALF_UP);
    }
}
