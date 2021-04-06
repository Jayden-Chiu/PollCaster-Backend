package com.project.pollcaster.service;

import com.project.pollcaster.entity.Choice;
import com.project.pollcaster.entity.Poll;
import com.project.pollcaster.entity.User;
import com.project.pollcaster.entity.UserDetailsImpl;
import com.project.pollcaster.exception.ResourceNotFoundException;
import com.project.pollcaster.payload.request.PollRequest;
import com.project.pollcaster.payload.response.ChoiceResponse;
import com.project.pollcaster.payload.response.PageResponse;
import com.project.pollcaster.payload.response.PollResponse;
import com.project.pollcaster.payload.response.UserProfile;
import com.project.pollcaster.repository.PollRepository;
import com.project.pollcaster.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PollService {

    @Autowired
    PollRepository pollRepository;

    @Autowired
    UserRepository userRepository;

    public PageResponse<PollResponse> getAllPolls(UserDetailsImpl currentUser, Pageable pageable) {
        Page<Poll> page = pollRepository.findAll(pageable);

        if (page.getNumberOfElements() == 0) {
            return new PageResponse<>(Collections.emptyList(), page.getNumber(),
                    page.getSize(), page.getTotalElements(), page.getTotalPages());
        }

        List<Long> pollIds = page.map(Poll::getId).getContent();

        List<PollResponse> pollResponses = page.map(poll -> {
            PollResponse pollResponse = new PollResponse();
            pollResponse.setId(poll.getId());
            pollResponse.setTitle(poll.getTitle());

            List<ChoiceResponse> choiceResponses = poll.getChoices().stream().map(choice -> {
                ChoiceResponse choiceResponse = new ChoiceResponse();
                choiceResponse.setId(choice.getId());
                choiceResponse.setText(choice.getText());
                choiceResponse.setVoteCount(choice.getVoteCount());

                return choiceResponse;
            }).collect(Collectors.toList());

            pollResponse.setChoices(choiceResponses);

            Long userId = poll.getCreatedBy();
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id",
                    userId));
            pollResponse.setCreatedBy(new UserProfile(user));
            pollResponse.setCreatedAt(poll.getCreatedAt());

            return pollResponse;
        }).getContent();

        return new PageResponse<>(pollResponses, page.getNumber(),
                page.getSize(), page.getTotalElements(), page.getTotalPages());

    }

    public Poll createPoll(PollRequest pollRequest) {
        Poll poll = new Poll();

        poll.setTitle(pollRequest.getTitle());
        pollRequest.getChoices().forEach(choiceRequest -> {
            poll.addChoice(new Choice(choiceRequest.getText()));
        });

        return pollRepository.save(poll);
    }
}
