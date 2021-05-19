package com.project.pollcaster.service;

import com.project.pollcaster.entity.*;
import com.project.pollcaster.exception.BadRequestException;
import com.project.pollcaster.exception.ResourceNotFoundException;
import com.project.pollcaster.payload.request.PollRequest;
import com.project.pollcaster.payload.request.VoteRequest;
import com.project.pollcaster.payload.response.*;
import com.project.pollcaster.repository.PollRepository;
import com.project.pollcaster.repository.UserRepository;
import com.project.pollcaster.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PollService {

    @Autowired
    PollRepository pollRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VoteRepository voteRepository;

    public PageResponse<PollResponse> getAllPolls(UserDetailsImpl currentUser, Pageable pageable) {
        Page<Poll> page = pollRepository.findAll(pageable);

        if (page.getNumberOfElements() == 0) {
            return new PageResponse<>(Collections.emptyList(), page.getNumber(),
                    page.getSize(), page.getTotalElements(), page.getTotalPages());
        }

        // map polls to poll responses
        List<PollResponse> pollResponses = page.map(poll -> {
            return mapPollToPollResponse(poll, currentUser, mapPollToChoiceResponses(poll));
        }).getContent();

        return new PageResponse<>(pollResponses, page.getNumber(),
                page.getSize(), page.getTotalElements(), page.getTotalPages());

    }

    public PageResponse<PollResponse> getAllPollsByUser(UserDetailsImpl currentUser, Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userId));

        Page<Poll> page = pollRepository.findByCreatedBy(userId, pageable);

        if (page.getNumberOfElements() == 0) {
            return new PageResponse<>(Collections.emptyList(), page.getNumber(),
                    page.getSize(), page.getTotalElements(), page.getTotalPages());
        }

        List<PollResponse> pollResponses = page.map(poll -> {
            return mapPollToPollResponse(poll, currentUser, mapPollToChoiceResponses(poll));
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

    public PollResponse getPollById(Long pollId, UserDetailsImpl currentUser) {
        Poll poll = pollRepository.findById(pollId).orElseThrow(
                () -> new ResourceNotFoundException("Poll", "id", pollId));

        return mapPollToPollResponse(poll, currentUser, mapPollToChoiceResponses(poll));

    }

    public PollResponse mapPollToPollResponse(Poll poll, UserDetailsImpl currentUser,
                                              List<ChoiceResponse> choiceResponses) {
        PollResponse pollResponse = new PollResponse();
        pollResponse.setId(poll.getId());
        pollResponse.setTitle(poll.getTitle());

        pollResponse.setChoices(choiceResponses);

        long totalVotes = 0;
        for (int i = 0; i < choiceResponses.size(); i++) {
            totalVotes += choiceResponses.get(i).getVoteCount();
        }
        pollResponse.setTotalVotes(totalVotes);

        Long userId = poll.getCreatedBy();
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id",
                userId));
        pollResponse.setCreatedBy(new UserProfile(user));
        pollResponse.setCreatedAt(poll.getCreatedAt());

        Vote userVote = null;

        if (currentUser != null) {
            userVote = voteRepository.findByUserIdAndPollId(currentUser.getId(), poll.getId());
        }

        if (userVote != null) {
            pollResponse.setSelectedChoice(userVote.getChoice().getId());
        }

        return pollResponse;
    }

    public List<ChoiceResponse> mapPollToChoiceResponses(Poll poll) {
        return poll.getChoices().stream().map(choice -> {
            ChoiceResponse choiceResponse = new ChoiceResponse();
            choiceResponse.setId(choice.getId());
            choiceResponse.setText(choice.getText());
            choiceResponse.setVoteCount(choice.getVotes().size());

            return choiceResponse;
        }).collect(Collectors.toList());
    }

    public PollResponse castVote(Long pollId, VoteRequest voteRequest, UserDetailsImpl currentUser) {
        Poll poll = pollRepository.findById(pollId).orElseThrow(
                () -> new ResourceNotFoundException("Poll", "id", pollId));

        User user = userRepository.getOne(currentUser.getId());

        Choice selectedChoice = poll.getChoices().stream()
                .filter(choice -> choice.getId().equals(voteRequest.getChoice()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Choice", "id", voteRequest.getChoice()));

        Vote vote = new Vote();
        vote.setPoll(poll);
        vote.setUser(user);
        vote.setChoice(selectedChoice);

        try {
            vote = voteRepository.save(vote);
            selectedChoice.getVotes().add(vote);
        } catch (Exception ex) {
            throw new BadRequestException("Sorry! You have already cast your vote in this poll");
        }

        return mapPollToPollResponse(poll, currentUser, mapPollToChoiceResponses(poll));
    }

    public ResponseEntity<?> deletePoll(Long pollId, UserDetailsImpl currentUser) {
        Poll poll = pollRepository.findById(pollId).orElseThrow(
                () -> new ResourceNotFoundException("Poll", "id", pollId));

        if (poll.getCreatedBy() != currentUser.getId()) {
            return new ResponseEntity(
                    new ApiResponse(false, "Unauthorized"),
                    HttpStatus.UNAUTHORIZED
            );
        }

        pollRepository.delete(poll);

        return new ResponseEntity(
                new ApiResponse(true, "Successfully deleted poll " + pollId),
                HttpStatus.OK
        );
    }
}
