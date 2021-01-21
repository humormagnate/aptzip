package com.markruler.aptzip.domain.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@Entity
@ToString
@Table(name = "tb_apt")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class AptEntity {
  @Id
  @NonNull
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "apt_name")
  private String aptName;
  @Column(name = "apt_province")
  private String aptProvince;
  @Column(name = "apt_city")
  private String aptCity;
  @Column(name = "apt_town")
  private String aptTown;
}