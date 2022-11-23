package com.nhnacademy.springmvc.domain;

import lombok.Getter;
@Getter
public class User{

  private final String id;
  private final String password;
  private final String name;

  public static User create(String id, String password, String name){
    return new User(id, password, name);
  }

  public User(String id, String password, String name) {
    this.id = id;
    this.password = password;
    this.name = name;
  }

}
