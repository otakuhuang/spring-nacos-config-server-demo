package com.otakuhuang.springnacosconfigserverdemo.repository;

import com.otakuhuang.springnacosconfigserverdemo.model.CoffeeOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeOrderRepository extends JpaRepository<CoffeeOrder, Long> {

}
