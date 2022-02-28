package com.otakuhuang.springnacosconfigserverdemo.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author otaku
 * @version 1.0
 * @date 2022/2/27 23:38
 * @description description
 */
@Getter
@Setter
@ToString
public class NewOrderRequest {
    private String customer;
    private List<String> items;
}
