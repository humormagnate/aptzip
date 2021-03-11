package com.markruler.aptzip.domain.apartment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Entity
@Table(name = "tb_apt")
@Getter
@ToString
@EqualsAndHashCode(of = "code")
@NoArgsConstructor
@AllArgsConstructor
public class AptEntity {
  @Id
  @NonNull
  private String code;

  @Column(name = "complex")
  private String complex;

  @Column(name = "province")
  private String province;

  @Column(name = "city")
  private String city;

  @Column(name = "town")
  private String town;

  @Column(name = "village")
  private String village;

  @Column(name = "approval")
  private String approval;

  @Column(name = "building")
  private Integer building;

  @Column(name = "apartment")
  private Integer apartment;
}
