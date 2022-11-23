package com.nhnacademy.springmvc.repository;

import com.nhnacademy.springmvc.domain.Inquiry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Component
public class InquiryRepositoryImpl implements InquiryRepository{

  private final Map<Long, Inquiry> inquiryMap = new HashMap<>();
  private static long id = 0;

  @Override
  public boolean exists(long id) {
    //해당 문의 목록이 있는지 없는지 확인
    return inquiryMap.containsKey(id);
  }

  @Override
  public Inquiry register(String customerId, String title, String category, String mainText, String inquiryTime, CommonsMultipartFile file) {
    //고객아이디 제목 분류 본문 작성일시 파일

    long inquiryId = ++id;

    Inquiry inquiry = Inquiry.createInquiry(inquiryId, customerId, title, category, mainText, inquiryTime,null, file, "N", false);
    inquiry.setId(inquiryId);
    inquiryMap.put(inquiryId, inquiry);

    return inquiry;
  }

  @Override
  public Inquiry getInquiry(long id) {
    return exists(id) ? inquiryMap.get(id) : null;
  }

  @Override
  public List<Inquiry> getNotCommentedInquires() { //답변 안 달린 문의만 리턴
    List<Inquiry> notCommentedInquires = new ArrayList<>();

    for (Entry<Long, Inquiry> entrySet : inquiryMap.entrySet()) {
      if(!entrySet.getValue().isBoolanswer()){
        notCommentedInquires.add(entrySet.getValue());
      }
    }
    return notCommentedInquires;
  }
  @Override
  public List<Inquiry> getMyInquiryList(String customerId) {
    List<Inquiry> myInquiryList = new ArrayList<>();

    for(Entry<Long, Inquiry> entrySet : inquiryMap.entrySet()){
      if(entrySet.getValue().getCustomerId().equals(customerId)){
        myInquiryList.add(entrySet.getValue());
      }
    }
    ComparatorByInquiryTime comp = new ComparatorByInquiryTime();
    Collections.sort(myInquiryList,comp);
    return myInquiryList;
  }

  class ComparatorByInquiryTime implements Comparator<Inquiry> { //답변 시간이 늦은 것부터 정렬

    @Override
    public int compare(Inquiry o1, Inquiry o2) {
      String firstValue = o1.getInquiryTime();
      String secondValue = o2.getInquiryTime();

      if(firstValue.compareTo(secondValue) > 0){
        return -1;
      }
      else if(firstValue.compareTo(secondValue) < 0){
        return 1;
      }
      else{
        return 0;
      }
    }
  }
}
