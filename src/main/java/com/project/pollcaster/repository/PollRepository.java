package com.project.pollcaster.repository;

import com.project.pollcaster.entity.Poll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {
    Optional<Poll> findById(Long id);

    Page<Poll> findByCreatedBy(Long id, Pageable pageable);


}
