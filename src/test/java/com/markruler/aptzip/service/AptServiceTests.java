package com.markruler.aptzip.service;

import java.util.Optional;

import com.markruler.aptzip.domain.apartment.model.AptEntity;
import com.markruler.aptzip.domain.apartment.model.AptRequestDto;
import com.markruler.aptzip.domain.apartment.repository.AptRepository;
import com.markruler.aptzip.domain.apartment.service.AptService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AptServiceTests {

  @InjectMocks
  private AptService service;

  @Mock
  private AptRepository repository;

  @Test
  @DisplayName("특정 게시물의 댓글 엔터티를 조회합니다")
  void testFindByCode() {
    // given
    AptEntity apt = AptRequestDto.builder().code("TEST01APT").complex("테스트아파트").build().toEntity();

    // mocking
    BDDMockito.given(repository.findByCode("TEST01APT")).willReturn(Optional.of(apt));

    // when
    AptEntity returnedApartment = service.findByCode(apt.getCode());

    // then
    Assertions.assertEquals("TEST01APT", returnedApartment.getCode());
    Assertions.assertNotEquals("TEST02APT", returnedApartment.getCode());
    Assertions.assertEquals("테스트아파트", returnedApartment.getComplex());
  }
}
