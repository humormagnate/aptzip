package com.markruler.aptzip.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.markruler.aptzip.domain.apartment.AptEntity;
import com.markruler.aptzip.domain.apartment.AptRequestDto;
import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.domain.board.CommentEntity;
import com.markruler.aptzip.domain.user.AptzipRoleEntity;
import com.markruler.aptzip.domain.user.UserAccountEntity;
import com.markruler.aptzip.domain.user.UserAccountRequestDto;
import com.markruler.aptzip.domain.user.UserFollowEntity;
import com.markruler.aptzip.domain.user.UserFollowRequestDto;
import com.markruler.aptzip.domain.user.UserRole;
import com.markruler.aptzip.persistence.board.BoardRepository;
import com.markruler.aptzip.persistence.board.CommentRepository;
import com.markruler.aptzip.persistence.user.FollowRepository;
import com.markruler.aptzip.persistence.user.UserJpaRepository;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserAccountService implements UserDetailsService {

  private final PasswordEncoder passwordEncoder;
  private final UserJpaRepository userJpaRepository;
  private final FollowRepository followRepository;
  private final BoardRepository boardRepository;
  private final CommentRepository commentRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    log.debug("username: {}", username);
    Optional<UserAccountEntity> userEntityWrapper = userJpaRepository.findByUsername(username);
    if (userEntityWrapper.isEmpty()) {
      throw new UsernameNotFoundException("Not found " + username);
    }

    UserAccountEntity user = userEntityWrapper.get();
    if (!user.isEnabled()) {
      throw new AuthenticationCredentialsNotFoundException("This account requires email verification or disabled.");
    }
    log.debug("user: {}", user);

    // TODO: Entity to DTO
    UserAccountRequestDto urd = new UserAccountRequestDto();
    urd.setId(user.getId());
    urd.setEmail(user.getEmail());
    urd.setUsername(user.getUsername());
    urd.setPassword(user.getPassword());
    urd.setIntroduction(user.getIntroduction());
    urd.setSignupDate(user.getSignupDate());
    urd.setReported(user.getReported());
    urd.setRole(UserRole.USER);
    urd.setApt(user.getApt());
    urd.setFollowing(user.getFollowing());
    urd.setFollower(user.getFollower());

    if (user.getRole() != null && user.getRole().getRole().equals(UserRole.USER.name())) {
      urd.setRole(UserRole.USER);
    } else {
      urd.setRole(UserRole.ADMIN);
    }
    log.debug("urd: {}", urd);
    return urd;
  }

  public void readUserPropertyById(Long id, Model model) {
    List<BoardEntity> boards = boardRepository.findByUserIdOrderByIdDesc(id);
    List<CommentEntity> comments = commentRepository.findByUserIdOrderByIdDesc(id);
    List<UserFollowEntity> followings = followRepository.findAllByFollowing(id);
    List<UserFollowEntity> followers = followRepository.findAllByFollower(id);

    UserAccountEntity user = findById(id).orElse(new UserAccountEntity());

    // @formatter:off
    model
      .addAttribute("boards", boards)
      .addAttribute("comments", comments)
      .addAttribute("infouser", user)
      .addAttribute("followings", followings)
      .addAttribute("followers", followers);
    // @formatter:on
  }

  @Transactional(rollbackFor = Exception.class)
  public UserAccountEntity save(UserAccountRequestDto user, String aptCode) {
    Optional<UserAccountEntity> existingUser = userJpaRepository.findByEmailIgnoreCase(user.getEmail());
    if (existingUser.isPresent()) {
      return null;
    }
    log.info("The email address not found");

    user.setApt(AptRequestDto.builder().code(aptCode).build().toEntity());
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole(UserRole.USER);
    user.setEnabled(true);
    return userJpaRepository.save(user.toEntity());
  }

  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @Transactional(readOnly = true)
  public List<UserAccountRequestDto> findAll() {
    return userJpaRepository.findAll().stream().map(UserAccountRequestDto::new).collect(Collectors.toList());
  }

  public Optional<UserAccountEntity> findById(Long id) {
    // return userJpaRepository.findById(id).map(UserAccountRequestDto::new);
    return userJpaRepository.findById(id);
  }

  public Optional<UserAccountEntity> findByEmailIgnoreCase(String email) {
    return userJpaRepository.findByEmailIgnoreCase(email);
  }

  public List<UserAccountEntity> listAdminsByApt(UserAccountRequestDto user) {
    AptEntity apt = AptRequestDto.builder().code(user.getApt().getCode()).build().toEntity();
    return userJpaRepository.findAllByAptAndRole(apt, new AptzipRoleEntity("ADMIN"));
  }

  public void disabledUser(Long id) {
    userJpaRepository.disabledUserById(id);
  }

  public void enabledUser(Long id) {
    userJpaRepository.enabledUserById(id);
  }

  public void updatePassword(UserAccountRequestDto user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userJpaRepository.updatePasswordById(user.getPassword(), user.getId());
  }

  public UserFollowEntity createFollow(Long id, UserAccountRequestDto user) {
    UserAccountEntity following = UserAccountEntity.builder().id(id).build();
    UserAccountEntity follower = user.toEntity();
    UserFollowEntity relationship = followRepository.findByFollowingAndFollower(following, follower);

    if (relationship != null) {
      return relationship;
    }
    return followRepository
        .save(UserFollowRequestDto.builder().following(following).follower(follower).build().toEntity());
  }

  public void deleteFollow(Long id, UserAccountRequestDto user) {
    UserAccountEntity following = UserAccountEntity.builder().id(id).build();
    UserAccountEntity follower = user.toEntity();
    UserFollowEntity relationship = followRepository.findByFollowingAndFollower(following, follower);
    followRepository.delete(relationship);
  }
}
