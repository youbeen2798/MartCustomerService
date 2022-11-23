package com.nhnacademy.springmvc.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


@Getter
@Setter
public class Inquiry {

  private long id;
  private String customerId;
  private String title;
  private String category;
  private String mainText;
  private String inquiryTime;
  private CommonsMultipartFile file;
  private boolean boolanswer;

  public static Inquiry createInquiry(long id, String customerId, String title, String category, String mainText, String inquiryTime, String answerText, CommonsMultipartFile file, String answer, boolean boolanswer){
    return new Inquiry(id, customerId, title, category, mainText, null, inquiryTime, null,  file, answer, boolanswer);
  }

  public Inquiry(long id, String customerId, String title, String category, String mainText, String answerTime, String inquiryTime, String answerText, CommonsMultipartFile file, String answer, boolean boolanswer){
    this.id = id;
    this.customerId = customerId;
    this.title = title;
    this.category = category;
    this.mainText = mainText;
    this.inquiryTime = inquiryTime;
    this.file = file;
    this.boolanswer = boolanswer;
  }

}
