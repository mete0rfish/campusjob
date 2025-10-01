package com.mete0rfish.campusjob.domain.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query(value = "SELECT r FROM Review r JOIN FETCH r.member",
            countQuery = "SELECT count(r) FROM Review r")
    Page<Review> findAllWithMember(Pageable pageable);
}