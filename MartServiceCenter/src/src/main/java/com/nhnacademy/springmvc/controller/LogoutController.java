package com.nhnacademy.springmvc.controller;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

  @GetMapping(value = {"/cs/logout"})
  public String Logout(HttpSession session) {
    session.removeAttribute("LOGINID");
    return "redirect:/cs/login";
  }
}
