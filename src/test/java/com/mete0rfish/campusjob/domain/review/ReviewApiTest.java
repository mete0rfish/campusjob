
package com.mete0rfish.campusjob.domain.review;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mete0rfish.campusjob.domain.review.dto.CreateReviewRequest;
import com.mete0rfish.campusjob.domain.review.dto.ReviewResponse;
import com.mete0rfish.campusjob.domain.review.dto.UpdateReviewRequest;
import com.mete0rfish.campusjob.support.exception.CustomException;
import com.mete0rfish.campusjob.support.exception.ErrorCode;
import com.mete0rfish.campusjob.support.security.CustomUserDetailsService;
import com.mete0rfish.campusjob.support.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import com.mete0rfish.campusjob.support.config.SecurityConfig;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewApi.class)
@Import(SecurityConfig.class)
class ReviewApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    private ReviewResponse reviewResponse;
    private String token;

    @BeforeEach
    void setUp() {
        reviewResponse = ReviewResponse.builder()
                .id(1L)
                .certificates(List.of("cert1"))
                .age(25)
                .seekPeriod("3 months")
                .tip("Good tip")
                .build();
        token = "test-token";

        given(jwtUtil.isExpired(token)).willReturn(false);
        given(jwtUtil.getEmail(token)).willReturn("test@test.com");
        given(jwtUtil.getRole(token)).willReturn("USER");
    }

    @Nested
    @DisplayName("리뷰 생성 API")
    class CreateReviewApiTest {
        @Test
        @DisplayName("성공")
        void createReview_success() throws Exception {
            // given
            CreateReviewRequest request = new CreateReviewRequest();
            request.setCompanyId(1L);
            request.setCertificates(List.of("cert1"));
            request.setAge(25);
            request.setSeekPeriod("3 months");
            request.setTip("Good tip");

            given(reviewService.createReview(eq("test@test.com"), any(CreateReviewRequest.class))).willReturn(reviewResponse);

            // when & then
            mockMvc.perform(post("/api/reviews")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "/api/reviews/" + reviewResponse.getId()))
                    .andExpect(jsonPath("$.id").value(reviewResponse.getId()));
        }
    }

    @Nested
    @DisplayName("리뷰 조회 API")
    class GetReviewApiTest {
        @Test
        @DisplayName("성공")
        void getReview_success() throws Exception {
            // given
            Long reviewId = 1L;
            given(reviewService.getReview(reviewId)).willReturn(reviewResponse);

            // when & then
            mockMvc.perform(get("/api/reviews/{reviewId}", reviewId)
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(reviewResponse.getId()))
                    .andExpect(jsonPath("$.tip").value(reviewResponse.getTip()));
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 리뷰")
        void getReview_fail_reviewNotFound() throws Exception {
            // given
            Long reviewId = 999L;
            given(reviewService.getReview(reviewId)).willThrow(new CustomException(ErrorCode.REVIEW_NOT_FOUND));

            // when & then
            mockMvc.perform(get("/api/reviews/{reviewId}", reviewId)
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(ErrorCode.REVIEW_NOT_FOUND.getCode()));
        }
    }

    @Nested
    @DisplayName("리뷰 수정 API")
    class UpdateReviewApiTest {
        @Test
        @DisplayName("성공")
        void updateReview_success() throws Exception {
            // given
            Long reviewId = 1L;
            UpdateReviewRequest request = new UpdateReviewRequest();
            request.setTip("Updated Tip");

            ReviewResponse updatedResponse = ReviewResponse.builder().id(reviewId).tip("Updated Tip").build();
            given(reviewService.updateReview(eq(reviewId), any(UpdateReviewRequest.class))).willReturn(updatedResponse);

            // when & then
            mockMvc.perform(put("/api/reviews/{reviewId}", reviewId)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(reviewId))
                    .andExpect(jsonPath("$.tip").value("Updated Tip"));
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 리뷰")
        void updateReview_fail_reviewNotFound() throws Exception {
            // given
            Long reviewId = 999L;
            UpdateReviewRequest request = new UpdateReviewRequest();
            given(reviewService.updateReview(eq(reviewId), any(UpdateReviewRequest.class)))
                    .willThrow(new CustomException(ErrorCode.REVIEW_NOT_FOUND));

            // when & then
            mockMvc.perform(put("/api/reviews/{reviewId}", reviewId)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(ErrorCode.REVIEW_NOT_FOUND.getCode()));
        }
    }

    @Nested
    @DisplayName("리뷰 삭제 API")
    class DeleteReviewApiTest {
        @Test
        @DisplayName("성공")
        void deleteReview_success() throws Exception {
            // given
            Long reviewId = 1L;
            doNothing().when(reviewService).deleteReview(reviewId);

            // when & then
            mockMvc.perform(delete("/api/reviews/{reviewId}", reviewId)
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 리뷰")
        void deleteReview_fail_reviewNotFound() throws Exception {
            // given
            Long reviewId = 999L;
            doThrow(new CustomException(ErrorCode.REVIEW_NOT_FOUND)).when(reviewService).deleteReview(reviewId);

            // when & then
            mockMvc.perform(delete("/api/reviews/{reviewId}", reviewId)
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isNotFound())
                    .andDo(print())
                    .andExpect(jsonPath("$.code").value(ErrorCode.REVIEW_NOT_FOUND.getCode()));
        }
    }
}
