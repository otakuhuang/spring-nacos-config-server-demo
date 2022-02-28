package com.otakuhuang.springnacosconfigserverdemo.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.money.Money;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author otaku
 * @version 1.0
 * @date 2022/2/27 23:39
 * @description description
 */
@Getter
@Setter
@ToString
public class NewCoffeeRequest {
    @NotEmpty
    private String name;
    @NotNull
    private Money price;
}
