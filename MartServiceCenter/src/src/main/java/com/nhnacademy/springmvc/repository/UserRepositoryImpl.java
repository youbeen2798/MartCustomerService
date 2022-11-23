package com.nhnacademy.springmvc.repository;

import com.nhnacademy.springmvc.domain.User;
import com.nhnacademy.springmvc.exception.CustomerAlreadyExistsException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
  private final Map<String, User> userMap = new HashMap<>();

  @Override
  public boolean exists(String id){
    return userMap.containsKey(id);
  }

  @Override
  public boolean matches(String id, String password) {
    //아이디와 비밀번호가 일치하는 고객이 있다면 true, 없다면 false
    return Optional.ofNullable(getUser(id))
        .map(customer -> customer.getPassword().equals(password))
        .orElse(false);
  }

  @Override
  public User getUser(String id) {
    return exists(id) ? userMap.get(id) : null;
  }

  @Override
  public User addUser(String id, String password, String name) {
    if(exists(id)){ //해당 아이디를 가진 고객이 이미 존재한다면
      throw new CustomerAlreadyExistsException();
    }

    User user = User.create(id, password, name);
    userMap.put(id, user);

    return user;
  }
}
