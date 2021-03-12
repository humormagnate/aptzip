package com.markruler.aptzip.domain.apartment;

import java.io.Serializable;

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
@Table(name = "TB_APT")
@Getter
@ToString
@EqualsAndHashCode(of = "code")
@NoArgsConstructor
@AllArgsConstructor
public class AptEntity implements Serializable {
  private static final long serialVersionUID = -9153138836152397538L;

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
