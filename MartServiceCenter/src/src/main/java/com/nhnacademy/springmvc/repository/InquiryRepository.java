package com.nhnacademy.springmvc.repository;

import com.nhnacademy.springmvc.domain.Inquiry;
import java.util.List;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public interface InquiryRepository {

  boolean exists(long id);

  Inquiry register(String customerId,
      String text,
      String category,
      String mainText,
      String inquiryTime,
      CommonsMultipartFile file);

  Inquiry getInquiry(long id);

  List<Inquiry> getMyInquiryList(String id);

  List<Inquiry> getNotCommentedInquires();
}

