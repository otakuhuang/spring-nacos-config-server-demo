package com.otakuhuang.springnacosconfigserverdemo.service;

import com.otakuhuang.springnacosconfigserverdemo.model.Coffee;
import com.otakuhuang.springnacosconfigserverdemo.repository.CoffeeRepository;
import lombok.extern.log4j.Log4j2;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author otaku
 * @version 1.0
 * @date 2022/2/27 23:26
 * @description description
 */
@CacheConfig(cacheNames = "CoffeeName")
@Log4j2
@Service
public class CoffeeService {
    @Autowired
    private CoffeeRepository coffeeRepository;

    public Coffee saveCoffee(String name, Money price) {
        return coffeeRepository.save(Coffee.builder().name(name).price(price).build());
    }

    @Cacheable
    public List<Coffee> getAllCoffee() {
        return coffeeRepository.findAll(Sort.by("id"));
    }

    public Coffee getCoffee(Long id) {
        return coffeeRepository.getById(id);
    }

    public long getCoffeeCount() {
        return coffeeRepository.count();
    }

    public Coffee getCoffee(String name) {
        return coffeeRepository.findByName(name);
    }

    public List<Coffee> getCoffeeByName(List<String> names) {
        return coffeeRepository.findByNameInOrderById(names);
    }
}
