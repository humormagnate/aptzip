package com.markruler.aptzip.domain.user.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.markruler.aptzip.domain.apartment.model.AptEntity;
import com.markruler.aptzip.domain.apartment.model.AptRequestDto;
import com.markruler.aptzip.domain.board.model.BoardEntity;
import com.markruler.aptzip.domain.board.repository.BoardRepository;
import com.markruler.aptzip.domain.comment.model.CommentEntity;
import com.markruler.aptzip.domain.comment.repository.CommentRepository;
import com.markruler.aptzip.domain.user.model.UserAccountEntity;
import com.markruler.aptzip.domain.user.model.UserAccountRequestDto;
import com.markruler.aptzip.domain.user.model.UserFollowEntity;
import com.markruler.aptzip.domain.user.model.UserFollowRequestDto;
import com.markruler.aptzip.domain.user.model.UserRole;
import com.markruler.aptzip.domain.user.repository.FollowRepository;
import com.markruler.aptzip.domain.user.repository.UserJpaRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Service
@Transactional
@RequiredArgsConstructor
public class UserAccountService implements UserDetailsService {
  Logger log = LoggerFactory.getLogger(UserAccountService.class);

  private final PasswordEncoder passwordEncoder;
  private final UserJpaRepository userJpaRepository;
  private final FollowRepository followRepository;
  private final BoardRepository boardRepository;
  private final CommentRepository commentRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<UserAccountEntity> userEntityWrapper = userJpaRepository.findByUsername(username);
    if (userEntityWrapper.isEmpty()) {
      throw new UsernameNotFoundException("Not found " + username);
    }

    UserAccountEntity user = userEntityWrapper.get();
    if (!user.isEnabled()) {
      throw new AuthenticationCredentialsNotFoundException("This account requires email verification or disabled.");
    }

    UserAccountRequestDto urd = UserAccountRequestDto.of(user);
    log.debug("user: {}", urd);

    if (user.getRole() != null && user.getRole().equals(UserRole.USER.name())) {
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
    return userJpaRepository.findAllByAptAndRole(apt, UserRole.ADMIN.name());
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
    log.debug("user: {}", user);
    UserAccountEntity following = UserAccountRequestDto.builder().id(id).build().toEntity();
    UserAccountEntity follower = user.toEntity();
    UserFollowEntity relationship = followRepository.findByFollowingAndFollower(following, follower);

    if (relationship != null) {
      return relationship;
    }
    return followRepository
        .save(UserFollowRequestDto.builder().following(following).follower(follower).build().toEntity());
  }

  public void deleteFollow(Long id, UserAccountRequestDto user) {
    log.debug("user: {}", user);
    UserAccountEntity following = UserAccountRequestDto.builder().id(id).build().toEntity();
    UserAccountEntity follower = user.toEntity();
    UserFollowEntity relationship = followRepository.findByFollowingAndFollower(following, follower);
    followRepository.delete(relationship);
  }
}
