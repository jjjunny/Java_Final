package com.globalbridge.model;

import java.io.Serializable;

public class Pair implements Serializable {
    private Participant mentor;
    private Participant mentee;

    public Pair(Participant mentor, Participant mentee) {
        if (!mentor.isMentor()) {
            throw new IllegalArgumentException("멘토는 Korean 언어 사용자여야 합니다.");
        }
        if (mentee.isMentor()) {
            throw new IllegalArgumentException("멘티는 English 언어 사용자여야 합니다.");
        }

        this.mentor = mentor;
        this.mentee = mentee;
    }

    public Participant getMentor() { return mentor; }
    public Participant getMentee() { return mentee; }

    @Override
    public String toString() {
        return String.format("멘토: %s (Korean) - 멘티: %s (English)",
                mentor.getName(), mentee.getName());
    }
}