package com.nhnacademy.springmvc.controller;

import com.nhnacademy.springmvc.domain.Answer;
import com.nhnacademy.springmvc.domain.AnswerDto;
import com.nhnacademy.springmvc.domain.Inquiry;
import com.nhnacademy.springmvc.repository.AnswerRepository;
import com.nhnacademy.springmvc.repository.InquiryRepository;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import jdk.jfr.Description;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@RequestMapping("/cs/admin")
@Controller
public class AdminController {

  private final InquiryRepository inquiryRepository;
  private final AnswerRepository answerRepository;

  public AdminController(InquiryRepository inquiryRepository, AnswerRepository answerRepository) {
    this.inquiryRepository = inquiryRepository;
    this.answerRepository = answerRepository;
  }

  @GetMapping
  @Description("아직 답변되지 않은 문의 목록 페이지")
  public String viewInquiries(Model model) {
    List<Inquiry> inquiries = inquiryRepository.getNotCommentedInquires();
    model.addAttribute("notCommentedInquires", inquiries);
    return "adminPage";
  }

  @GetMapping(value = "answer/{inquiryId}")
  @Description("관리자가 제목 버튼을 눌러 상세 문의내용 및 답변할 수 있는 페이지로 이동")
  public String viewInquiryAnswer(Model model, @PathVariable("inquiryId") long id) {
    Inquiry inquiry = inquiryRepository.getInquiry(id); //문의 목록
    model.addAttribute("inquiry", inquiry);
    return "adminAnswerPage";
  }

  @PostMapping(value = "/answer/{inquiryId}")
  @Description("관리자가 답변을 마친 후")
  public String doInquiryAnswer(@Valid @ModelAttribute AnswerDto answerDto,
      BindingResult bindingResult,
      Model model,
      @PathVariable("inquiryId") long inquiryId,
      @SessionAttribute(value = "LOGINID", required = false) String loginId) {

    if (bindingResult.hasErrors()) {
      return "redirect:/cs/admin";
    }

    //답변 시간 구하기
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    Answer answer = answerRepository.register(inquiryId,
        answerDto.getAnswerText(),
        formatter.format(new Date()),
        loginId);

    Inquiry inquiry = inquiryRepository.getInquiry(inquiryId);
    inquiry.setBoolanswer(true); //답변 했다고 가정

    model.addAttribute("inquiry", inquiry);
    model.addAttribute("answer", answer);
    return "detailedAnswer"; //방금 답변한 문의 내용 페이지로 이동
  }
}
