package com.project.pollcaster.payload.request;

import javax.validation.constraints.NotNull;

public class VoteRequest {
    @NotNull
    private Long choice;

    public Long getChoice() {
        return choice;
    }

    public void setChoice(Long choice) {
        this.choice = choice;
    }
}
