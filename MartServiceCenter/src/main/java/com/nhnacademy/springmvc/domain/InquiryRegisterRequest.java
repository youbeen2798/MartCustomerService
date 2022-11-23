package com.nhnacademy.springmvc.domain;

import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class InquiryRegisterRequest {

  @Size(min = 2, max = 200)
  private String title;

  private String category;

  @Size(min = 0, max = 40000)
  private String mainText;

  private CommonsMultipartFile file;

}
