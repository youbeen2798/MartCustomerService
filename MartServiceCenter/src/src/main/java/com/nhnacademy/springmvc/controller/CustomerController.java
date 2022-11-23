package com.nhnacademy.springmvc.controller;

import com.nhnacademy.springmvc.domain.Customer;
import com.nhnacademy.springmvc.domain.Inquiry;
import com.nhnacademy.springmvc.domain.InquiryRegisterRequest;
import com.nhnacademy.springmvc.domain.User;
import com.nhnacademy.springmvc.exception.UserNotFoundException;
import com.nhnacademy.springmvc.exception.ValidationFailedException;
import com.nhnacademy.springmvc.repository.AnswerRepository;
import com.nhnacademy.springmvc.repository.InquiryRepository;
import com.nhnacademy.springmvc.repository.UserRepository;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import jdk.jfr.Description;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/cs")
@Controller
public class CustomerController {
  private final UserRepository userRepository;
  private final InquiryRepository inquiryRepository;
  private final AnswerRepository answerRepository;

  public CustomerController(UserRepository userRepository, InquiryRepository inquiryRepository,
      AnswerRepository answerRepository) {
    this.userRepository = userRepository;
    this.inquiryRepository = inquiryRepository;
    this.answerRepository = answerRepository;
  }

  @ModelAttribute("customer")
  public Customer getCustomer(@PathVariable("loginId") String loginId){
    User user = new Customer(loginId, "1234", "고객");
    if(Objects.isNull(user)){
      throw new UserNotFoundException();
    }
    return (Customer) user;
  }

  @GetMapping("/{loginId}")
  @Description("고객 본인이 문의했던 목록들 보기")
  public String viewMyInquiries(@ModelAttribute Customer customer, HttpServletRequest httpServletRequest, Model model) {
    List<Inquiry> inquiryList = inquiryRepository.getMyInquiryList(customer.getId());

    //카테고리 버튼을 눌렀다면 걸러내기
      if (!Objects.isNull(httpServletRequest.getParameter("category"))) {
      String category = httpServletRequest.getParameter("category");
      inquiryList = inquiryList.stream().filter(inquiry -> inquiry.getCategory().equals(category)).collect(Collectors.toList());
    }
    model.addAttribute("loginId", customer.getId());
    model.addAttribute("inquiryList", inquiryList);
    return "myInquiries";
  }

  @GetMapping(value = "/{loginId}/inquiry/{inquiryId}/detail")
  @Description("제목 버튼을 눌러 고객 본인이 문의했던 목록을 자세히 보기")
  public String viewMyInquiry(@PathVariable("inquiryId") long inquiryId, Model model, @PathVariable("loginId") String loginId){
    Inquiry inquiry = inquiryRepository.getInquiry(inquiryId);

    if(inquiry.isBoolanswer()){
      model.addAttribute("answer", answerRepository.getCustomerAnswer(inquiryId));
    }
    model.addAttribute("loginId", loginId);
    model.addAttribute("inquiry", inquiry);
    return "detailedInquiry";
  }

  @GetMapping("/inquiry/{loginId}")
  @Description("고객이 문의하는 폼으로 이동하기")
  public String viewInquiryForm(@PathVariable("loginId") String loginId, Model model){
    model.addAttribute("loginId", loginId);
    return "inquiryForm";
  }

  @PostMapping("/inquiry/{loginId}")
  @Description("고객이 작성한 문의 등록하기")
  public String doInquiry(@ModelAttribute Customer customer,
      @Valid @ModelAttribute InquiryRegisterRequest inquiryRegisterRequest, BindingResult bindingResult, Model model){
    if(bindingResult.hasErrors()){
      throw new ValidationFailedException(bindingResult);
    }
    //현재 시간 구하기(문의 시간)
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    inquiryRepository.register(customer.getId(),inquiryRegisterRequest.getTitle(), inquiryRegisterRequest.getCategory(),
        inquiryRegisterRequest.getMainText(), formatter.format(new Date()), inquiryRegisterRequest.getFile());

    model.addAttribute("inquiry", inquiryRegisterRequest);
    //문의 성공했다면 방금 문의한 내역 보여주는 페이지로 이동
    return "inquirySuccess";
  }

  @ExceptionHandler(UserNotFoundException.class)
  public String handleCustomerNotFoundException(){
    return "redirect:/cs/login";
  }

}
