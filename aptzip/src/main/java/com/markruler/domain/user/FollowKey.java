// package com.markruler.domain.user;


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
//   private AptzipUserEntity from;
  
//   @ManyToOne(fetch = FetchType.LAZY)
//   @JoinColumn(name = "user_id", insertable = false, updatable = false)
//   private AptzipUserEntity to;
	
// }