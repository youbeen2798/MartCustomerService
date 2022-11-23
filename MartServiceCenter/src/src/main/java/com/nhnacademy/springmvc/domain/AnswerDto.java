package com.nhnacademy.springmvc.domain;

import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AnswerDto {

  private long answerId; //답변을 구분할 고유 아이디

  private long inquiryId; //문의 고유 번호

  @Size(min = 1, max = 40000)
  private String answerText; //답변 내용

  private String answerTime; //답변 시간

  private String answerCsAdmin; //답변을 달아준 관리자

}
