package com.globalbridge.model;

import java.io.Serializable;

public class Participant implements Serializable {
    private String name;
    private String studentId;
    private String major;
    private String language;  // "Korean" 또는 "English"
    private int grade;

    public Participant(String name, String studentId, String major,
                       String language, int grade) {
        this.name = name;
        this.studentId = studentId;
        this.major = major;
        this.language = language;
        this.grade = grade;
    }

    public boolean isMentor() {
        return language.equalsIgnoreCase("Korean");
    }

    // Getters
    public String getName() { return name; }
    public String getStudentId() { return studentId; }
    public String getMajor() { return major; }
    public String getLanguage() { return language; }
    public int getGrade() { return grade; }

    @Override
    public String toString() {
        return name + " (" + studentId + ") - " + language + " - " + major;
    }
}