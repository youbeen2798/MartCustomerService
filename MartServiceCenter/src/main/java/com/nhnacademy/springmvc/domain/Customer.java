package com.nhnacademy.springmvc.domain;

import lombok.Getter;

@Getter
public class Customer extends User{ //고객

  public Customer(String id, String password, String name) {
    super(id, password, name);
  }

  public static Customer create(String id, String password, String name){
    return new Customer(id, password, name);
  }
}
