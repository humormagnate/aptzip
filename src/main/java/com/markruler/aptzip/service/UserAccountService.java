package com.markruler.aptzip.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.domain.board.CommentEntity;
import com.markruler.aptzip.domain.apartment.AptEntity;
import com.markruler.aptzip.domain.user.AptzipRoleEntity;
import com.markruler.aptzip.domain.user.AptzipUserEntity;
import com.markruler.aptzip.domain.user.UserFollowEntity;
import com.markruler.aptzip.domain.user.UserRequestDto;
import com.markruler.aptzip.domain.user.UserResponseDto;
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
@Service
public class UserAccountService implements UserDetailsService {

  private final PasswordEncoder passwordEncoder;
  private final UserJpaRepository userJpaRepository;
  private final FollowRepository followRepository;
  // private final FollowQueryRepository followQuery;
  private final BoardRepository boardRepository;
  private final CommentRepository commentRepository;

  boolean enabled = true;
  boolean accountNonExpired = true;
  boolean credentialsNonExpired = true;
  boolean accountNonLocked = true;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    log.debug(
        "=============================== UserService-loadUserByUsername start =====================================");
    log.debug("username : {}", username);

    Optional<AptzipUserEntity> userEntityWrapper = userJpaRepository.findByUsername(username);
    AptzipUserEntity user = userEntityWrapper.get();

    log.debug(
        "=============================== UserService-loadUserByUsername get =====================================");
    log.debug("user : {}", user);

    if (user == null) {
      throw new UsernameNotFoundException("Not found " + username);
    }

    if (!user.isEnabled()) {
      throw new AuthenticationCredentialsNotFoundException(
          "This account requires email verification or disabled.");
    }

    // TODO: Make a builder pattern
    UserResponseDto urd = new UserResponseDto(
    // @formatter:off
      user.getId(),
      user.getUsername(),
      user.getPassword(),
      enabled,
      accountNonExpired,
      credentialsNonExpired,
      accountNonLocked,
      UserRole.USER.getGrantedAuthorities(),
      user.getEmail(),
      user.getIntroduction(),
      user.getSignupDate(),
      user.getReported(),
      UserRole.USER,
      UserRole.USER.getPrivileges(),
      user.getApt(),
      user.getFollowing(),
      user.getFollower()
    );
    // @formatter:on
    if (user.getRole() != null && user.getRole().getRole().equals(UserRole.USER.name())) {
      urd.setRole(UserRole.USER);
      urd.setPrivilege(UserRole.USER.getPrivileges());
    } else {
      urd.setRole(UserRole.ADMIN);
      urd.setPrivilege(UserRole.ADMIN.getPrivileges());
    }
    return urd;
  }

  public void readUserInfoById(Long id, Model model) {
    List<BoardEntity> boards = boardRepository.findByUserIdOrderByIdDesc(id);
    List<CommentEntity> comments = commentRepository.findByUserIdOrderByIdDesc(id);
    List<UserFollowEntity> followings = followRepository.findAllByFollowing(id);
    List<UserFollowEntity> followers = followRepository.findAllByFollower(id);

    AptzipUserEntity user = findById(id);

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
  public void save(AptzipUserEntity user) {
    userJpaRepository.save(user);
  }

  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @Transactional(readOnly = true)
  public List<UserRequestDto> findAll() {
    // @formatter:off
    return userJpaRepository
            .findAll()
            .stream()
            .map(UserRequestDto::new)
            .collect(Collectors.toList());
    // @formatter:on
  }

  public AptzipUserEntity findById(Long id) {
    return userJpaRepository.findById(id).orElse(new AptzipUserEntity());
  }

  public List<AptzipUserEntity> listAdminsByAPT(UserResponseDto principal) {
    AptEntity apt = AptEntity.builder().id(principal.getApt().getId()).build();
    return userJpaRepository.findAllByAptAndRole(apt, new AptzipRoleEntity("ADMIN"));
  }

  public void disabledUser(Long id) {
    userJpaRepository.disabledUserById(id);
  }

  public void updatePassword(UserRequestDto user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userJpaRepository.updatePasswordById(user.getPassword(), user.getId());
  }

  public String createFollow(Long id, UserResponseDto principal) {
    AptzipUserEntity following = AptzipUserEntity.builder().id(id).build();
    AptzipUserEntity follower = principal.toEntity();
    UserFollowEntity relationship =
        followRepository.findByFollowingAndFollower(following, follower);

    if (relationship != null) {
      followRepository.delete(relationship);
      return "delete";
    } else {
      followRepository
          .save(UserFollowEntity.builder().following(following).follower(follower).build());
      return "save";
    }
  }
}
