package com.markruler.aptzip.domain.apartment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class AptRequestDto {
  private String code;
  private String complex;
  private String province;
  private String city;
  private String town;
  private String village;
  private String approval;
  private Integer building;
  private Integer apartment;

  public AptEntity toEntity() {
    // @formatter:off
    return new AptEntity(
      this.code,
      this.complex,
      this.province,
      this.city,
      this.town,
      this.village,
      this.approval,
      this.building,
      this.apartment
    );
    // @formatter:on
  }
}
