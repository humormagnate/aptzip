package com.example.domain.user;

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
import lombok.ToString;

@Getter
@Builder
@Entity
@ToString
@Table(name = "tb_apt")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class AptEntity {
  @Id
  @Column(name = "apt_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(name = "apt_name")
  private String aptName;
  @Column(name = "apt_province")
  private String aptProvince;
  @Column(name = "apt_city")
  private String aptCity;
  @Column(name = "apt_town")
  private String aptTown;
}