// package com.markruler.aptzip.domain.user;


// import java.io.Serializable;

// import javax.persistence.Embeddable;
// import javax.persistence.FetchType;
// import javax.persistence.JoinColumn;
// import javax.persistence.ManyToOne;

// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// @Embeddable
// public class FollowKey implements Serializable {
//   private static final long serialVersionUID = 1L;

//   @ManyToOne(fetch = FetchType.LAZY)
//   @JoinColumn(name = "user_id", insertable = false, updatable = false)
//   private UserAccountEntity from;

//   @ManyToOne(fetch = FetchType.LAZY)
//   @JoinColumn(name = "user_id", insertable = false, updatable = false)
//   private UserAccountEntity to;

// }