package com.mete0rfish.campusjob.domain.review;

import com.mete0rfish.campusjob.domain.review.dto.CreateReviewRequest;
import com.mete0rfish.campusjob.domain.review.dto.ReviewResponse;
import com.mete0rfish.campusjob.domain.review.dto.UpdateReviewRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

// TODO API URL이 프론트랑 다름
@Slf4j
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewApi {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<Page<ReviewResponse>> getReviews(Pageable pageable) {
        Page<ReviewResponse> response = reviewService.getReviews(pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CreateReviewRequest request) {
        ReviewResponse response = reviewService.createReview(userDetails.getUsername(), request);
        log.info("ReviewApi: createReview: {}", response.toString());
        return ResponseEntity.created(URI.create("/reviews/" + response.getId())).body(response);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable Long reviewId) {
        ReviewResponse response = reviewService.getReview(reviewId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long reviewId,
            @RequestBody UpdateReviewRequest request) {
        // TODO: Add authorization check to ensure the user can update this review
        ReviewResponse response = reviewService.updateReview(reviewId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long reviewId) {
        reviewService.deleteReview(userDetails.getUsername(), reviewId);
        return ResponseEntity.noContent().build();
    }
}
