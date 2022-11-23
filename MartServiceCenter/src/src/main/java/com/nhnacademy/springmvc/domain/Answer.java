package com.nhnacademy.springmvc.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Answer {

  private long answerId; //답변을 구분할 고유 아이디
  private long inquiryId; //문의 고유 번호
  private String answerText; //답변 내용
  private String answerTime; //답변 시간
  private String answerCsAdmin; //답변을 달아준 관리자

  public Answer(long inquiryId, String answerText, String answerTime, String answerCsAdmin) {
    this.inquiryId = inquiryId;
    this.answerText = answerText;
    this.answerTime = answerTime;
    this.answerCsAdmin = answerCsAdmin;
  }

  public static Answer createAnswer(long inquiryId,
      String answerText,
      String answerTime,
      String answerCsAdmin) {
    return new Answer(inquiryId, answerText, answerTime, answerCsAdmin);
  }
}
