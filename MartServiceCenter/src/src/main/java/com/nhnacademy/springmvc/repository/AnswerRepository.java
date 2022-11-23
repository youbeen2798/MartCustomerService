package com.nhnacademy.springmvc.repository;

import com.nhnacademy.springmvc.domain.Answer;

public interface AnswerRepository {

  boolean exists(long id);

  Answer register(long inquiryId, String answerText, String answerTime, String answerCsAdmin);

  Answer getAnswer(long id);

  Answer getCustomerAnswer(long inquiryId);
}
