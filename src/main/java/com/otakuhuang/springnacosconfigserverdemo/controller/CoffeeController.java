package com.otakuhuang.springnacosconfigserverdemo.controller;

import com.otakuhuang.springnacosconfigserverdemo.controller.request.NewCoffeeRequest;
import com.otakuhuang.springnacosconfigserverdemo.model.Coffee;
import com.otakuhuang.springnacosconfigserverdemo.service.CoffeeService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author otaku
 * @version 1.0
 * @date 2022/2/27 23:41
 * @description description
 */
@RestController
@RequestMapping("/coffee")
@Log4j2
public class CoffeeController {
    @Autowired
    private CoffeeService coffeeService;

    @PostMapping(value = "", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Coffee addCoffeeWithoutBindingResult(@Valid NewCoffeeRequest newCoffeeRequest) {
        return coffeeService.saveCoffee(newCoffeeRequest.getName(), newCoffeeRequest.getPrice());
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Coffee addJsonCoffeeWithoutBindingResult(@Valid @RequestBody NewCoffeeRequest newCoffeeRequest) {
        return coffeeService.saveCoffee(newCoffeeRequest.getName(), newCoffeeRequest.getPrice());
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public List<Coffee> batchAddCoffee(@RequestParam("file") MultipartFile file) {
        List<Coffee> coffees = new ArrayList<>();
        if (!file.isEmpty()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
                String str;
                while ((str = reader.readLine()) != null) {
                    String[] arr = StringUtils.split(str, " ");
                    if (arr != null & arr.length == 2) {
                        coffees.add(coffeeService.saveCoffee(arr[0],
                                Money.of(CurrencyUnit.of("CNY"),
                                        NumberUtils.createBigDecimal(arr[1]))));
                    }
                }
            } catch (IOException e) {
                log.error("exception: ", e);
            } finally {
                IOUtils.closeQuietly(reader);
            }
        }
        return coffees;
    }

    @GetMapping(value = "", params = "!name")
    public List<Coffee> getAll() {
        return coffeeService.getAllCoffee();
    }

    @GetMapping("/{id}")
    public Coffee getById(@PathVariable Long id) {
        Coffee coffee = coffeeService.getCoffee(id);
        log.info("Coffee: {}", coffee);
        return coffee;
    }

    @GetMapping(value = "", params = "name")
    public Coffee getByName(@RequestParam String name) {
        return coffeeService.getCoffee(name);
    }
}
