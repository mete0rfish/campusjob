
package com.mete0rfish.campusjob.domain.review;

import com.mete0rfish.campusjob.domain.company.Company;
import com.mete0rfish.campusjob.domain.company.CompanyRepository;
import com.mete0rfish.campusjob.domain.member.Member;
import com.mete0rfish.campusjob.domain.member.MemberRepository;
import com.mete0rfish.campusjob.domain.review.dto.CreateReviewRequest;
import com.mete0rfish.campusjob.domain.review.dto.ReviewResponse;
import com.mete0rfish.campusjob.domain.review.dto.UpdateReviewRequest;
import com.mete0rfish.campusjob.support.exception.CustomException;
import com.mete0rfish.campusjob.support.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CompanyRepository companyRepository;

    private Member member;
    private Company company;
    private Review review;

    @BeforeEach
    void setUp() {
        member = Member.builder().id(1L).email("test@test.com").build();
        company = Company.builder().id(1L).companyName("Test Company").build();
        review = Review.builder()
                .id(1L)
                .member(member)
                .company(company)
                .certificates(List.of("cert1"))
                .age(25)
                .seekPeriod("3 months")
                .tip("Good tip")
                .build();
    }

    @Nested
    @DisplayName("리뷰 생성")
    class CreateReviewTest {
        @Test
        @DisplayName("성공")
        void createReview_success() {
            // given
            CreateReviewRequest request = new CreateReviewRequest();
            request.setCompanyId(company.getId());
            request.setCertificates(List.of("cert1"));
            request.setAge(25);
            request.setSeekPeriod("3 months");
            request.setTip("Good tip");

            given(memberRepository.findByEmail(member.getEmail())).willReturn(Optional.of(member));
            given(companyRepository.findById(company.getId())).willReturn(Optional.of(company));
            given(reviewRepository.save(any(Review.class))).willReturn(review);

            // when
            ReviewResponse response = reviewService.createReview(member.getEmail(), request);

            // then
            assertThat(response.getId()).isEqualTo(review.getId());
            assertThat(response.getCertificates()).isEqualTo(request.getCertificates());
            verify(reviewRepository, times(1)).save(any(Review.class));
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 사용자")
        void createReview_fail_memberNotFound() {
            // given
            CreateReviewRequest request = new CreateReviewRequest();
            given(memberRepository.findByEmail(any())).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> reviewService.createReview("nonexistent@test.com", request))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.MEMBER_NOT_FOUND);
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 회사")
        void createReview_fail_companyNotFound() {
            // given
            CreateReviewRequest request = new CreateReviewRequest();
            request.setCompanyId(999L);
            given(memberRepository.findByEmail(member.getEmail())).willReturn(Optional.of(member));
            given(companyRepository.findById(request.getCompanyId())).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> reviewService.createReview(member.getEmail(), request))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.COMPANY_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("리뷰 조회")
    class GetReviewTest {
        @Test
        @DisplayName("성공")
        void getReview_success() {
            // given
            given(reviewRepository.findById(review.getId())).willReturn(Optional.of(review));

            // when
            ReviewResponse response = reviewService.getReview(review.getId());

            // then
            assertThat(response.getId()).isEqualTo(review.getId());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 리뷰")
        void getReview_fail_reviewNotFound() {
            // given
            Long reviewId = 999L;
            given(reviewRepository.findById(reviewId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> reviewService.getReview(reviewId))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.REVIEW_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("리뷰 수정")
    class UpdateReviewTest {
        @Test
        @DisplayName("성공")
        void updateReview_success() {
            // given
            UpdateReviewRequest request = new UpdateReviewRequest();
            request.setCertificates(List.of("updated-cert"));
            request.setAge(30);
            request.setSeekPeriod("6 months");
            request.setTip("Updated tip");

            given(reviewRepository.findById(review.getId())).willReturn(Optional.of(review));

            // when
            ReviewResponse response = reviewService.updateReview(review.getId(), request);

            // then
            assertThat(response.getCertificates()).isEqualTo(request.getCertificates());
            assertThat(response.getAge()).isEqualTo(request.getAge());
            assertThat(response.getSeekPeriod()).isEqualTo(request.getSeekPeriod());
            assertThat(response.getTip()).isEqualTo(request.getTip());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 리뷰")
        void updateReview_fail_reviewNotFound() {
            // given
            Long reviewId = 999L;
            UpdateReviewRequest request = new UpdateReviewRequest();
            given(reviewRepository.findById(reviewId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> reviewService.updateReview(reviewId, request))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.REVIEW_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("리뷰 삭제")
    class DeleteReviewTest {
        @Test
        @DisplayName("성공")
        void deleteReview_success() {
            // given
            given(reviewRepository.findById(review.getId())).willReturn(Optional.of(review));

            // when
            reviewService.deleteReview(review.getId());

            // then
            verify(reviewRepository, times(1)).delete(review);
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 리뷰")
        void deleteReview_fail_reviewNotFound() {
            // given
            Long reviewId = 999L;
            given(reviewRepository.findById(reviewId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> reviewService.deleteReview(reviewId))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.REVIEW_NOT_FOUND);
        }
    }
}
