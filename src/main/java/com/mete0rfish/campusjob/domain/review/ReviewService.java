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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public ReviewResponse createReview(String userEmail, CreateReviewRequest request) {
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new CustomException(ErrorCode.COMPANY_NOT_FOUND));

        Review review = Review.builder()
                .member(member)
                .company(company)
                .certificates(request.getCertificates())
                .age(request.getAge())
                .seekPeriod(request.getSeekPeriod())
                .tip(request.getTip())
                .build();

        Review savedReview = reviewRepository.save(review);
        return ReviewResponse.from(savedReview);
    }

    @Transactional(readOnly = true)
    public ReviewResponse getReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        return ReviewResponse.from(review);
    }

    @Transactional
    public ReviewResponse updateReview(Long reviewId, UpdateReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        // TODO: Add authorization check here (e.g., check if the user owns the review)

        review.update(request.getCertificates(), request.getAge(), request.getSeekPeriod(), request.getTip());

        return ReviewResponse.from(review);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        // TODO: Add authorization check here

        reviewRepository.delete(review);
    }
}
