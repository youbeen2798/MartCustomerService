package com.nhnacademy.springmvc.controller;

import com.nhnacademy.springmvc.exception.UserNotFoundException;
import com.nhnacademy.springmvc.repository.UserRepository;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping("/cs/login")
public class LoginController {

  private final UserRepository userRepository;

  public LoginController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping
  public String login(Model model, @SessionAttribute(value = "LOGINID", required = false)
  String session) {
    if (session == null) { //로그인을 아직 하지 않았다면
      return "loginForm";
    } else if (session.equals("admin")) { //관리자라면 관리자 페이지로 이동
      return "redirect:/cs/admin";
    }
    //로그인을 마쳤다면
    model.addAttribute("id", session);
    return "loginSuccess";
  }

  @PostMapping
  public String doLogin(@RequestParam("id") String loginId,
      @RequestParam("pwd") String loginPwd, HttpServletRequest request, ModelMap modelMap) {
    HttpSession session = request.getSession();
    if (userRepository.matches(loginId, loginPwd)) {
      session.setAttribute("LOGINID", loginId);
      modelMap.put("loginId", loginId);
      if (loginId.equals("admin") && loginPwd.equals("1234")) {
        return "redirect:/cs/admin";
      }
      return "customerMainPage";
    } else { //로그인 실패하면
      throw new UserNotFoundException();
    }
  }

  @ExceptionHandler(UserNotFoundException.class)
  public String handleCustomerNotFoundException() {
    return "redirect:/cs/login";
  }
}
