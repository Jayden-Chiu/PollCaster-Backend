package com.project.pollcaster.controller;

import com.project.pollcaster.entity.Poll;
import com.project.pollcaster.entity.UserDetailsImpl;
import com.project.pollcaster.payload.request.PollRequest;
import com.project.pollcaster.payload.response.ApiResponse;
import com.project.pollcaster.payload.response.PageResponse;
import com.project.pollcaster.payload.response.PollResponse;
import com.project.pollcaster.repository.PollRepository;
import com.project.pollcaster.security.CurrentUser;
import com.project.pollcaster.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/polls")
public class PollController {

    @Autowired
    PollRepository pollRepository;

    @Autowired
    PollService pollService;


    // get all polls
    @GetMapping
    public PageResponse<PollResponse> getAllPolls(@CurrentUser UserDetailsImpl currentUser, Pageable pageable) {
        return pollService.getAllPolls(currentUser, pageable);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createPoll(@Valid @RequestBody PollRequest pollRequest) {
        Poll poll = pollService.createPoll(pollRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{pollId}")
                .buildAndExpand(poll.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Poll Created Successfully"));
    }
}
