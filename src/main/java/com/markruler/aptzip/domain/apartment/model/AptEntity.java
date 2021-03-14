package com.markruler.aptzip.domain.apartment.model;

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
  @Column(name = "code", updatable = false)
  private String code;

  @Column(name = "complex", updatable = false)
  private String complex;

  @Column(name = "province", updatable = false)
  private String province;

  @Column(name = "city", updatable = false)
  private String city;

  @Column(name = "town", updatable = false)
  private String town;

  @Column(name = "village", updatable = false)
  private String village;

  @Column(name = "approval")
  private String approval;

  @Column(name = "building")
  private Integer building;

  @Column(name = "apartment")
  private Integer apartment;
}
