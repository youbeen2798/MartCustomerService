package com.nhnacademy.springmvc.repository;

import com.nhnacademy.springmvc.domain.Answer;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class AnswerRepositoryImpl implements AnswerRepository{

  private final Map<Long, Answer> answerMap = new HashMap<>();
  private static long id = 0;

  @Override
  public boolean exists(long id) {
    return answerMap.containsKey(id);
  }

  @Override
  public Answer register(long inquiryId, String answerText, String answerTime, String answerCsAdmin) {

    long answerId = ++id;

    Answer answer = Answer.createAnswer(inquiryId,answerText, answerTime, answerCsAdmin);
    answer.setAnswerId(answerId);
    answerMap.put(inquiryId, answer);

    return answer;
  }

  @Override
  public Answer getAnswer(long id) {
    return exists(id) ? answerMap.get(id) : null;
  }

  public Answer getCustomerAnswer(long inquiryId){
    for(Map.Entry<Long, Answer> entry : answerMap.entrySet()){
      if(entry.getValue().getInquiryId() == inquiryId){
        return entry.getValue();
      }
    }
    return null;
  }

}
