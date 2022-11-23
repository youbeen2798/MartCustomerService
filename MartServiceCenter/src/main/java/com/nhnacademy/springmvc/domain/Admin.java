package com.nhnacademy.springmvc.domain;

import lombok.Getter;

@Getter
public class Admin extends User { //관리자

  public Admin(String id, String password, String name) {
    super(id, password, name);
  }

  public static Admin create(String id, String password, String name) {
    return new Admin(id, password, name);
  }
}

