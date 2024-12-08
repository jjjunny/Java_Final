package com.globalbridge.gui;

import com.globalbridge.model.*;
import com.globalbridge.util.DataManager;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class GlobalBridgeProgram extends JFrame {
    private ArrayList<Participant> participants;
    private HashMap<String, Pair> matches;
    private HashMap<String, ArrayList<Activity>> activities;
    private DataManager dataManager;
    private RegistrationPanel registrationPanel;
    private MatchingPanel matchingPanel;
    private ActivityPanel activityPanel;

    public GlobalBridgeProgram() {
        participants = new ArrayList<>();
        matches = new HashMap<>();
        activities = new HashMap<>();
        dataManager = new DataManager();

        setTitle("글로벌 브릿지 - 함께 성장하는 캠퍼스 문화 교류");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeUI();
        loadData();
    }

    private void initializeUI() {
        JTabbedPane tabbedPane = new JTabbedPane();

        registrationPanel = new RegistrationPanel(this);
        matchingPanel = new MatchingPanel(this);
        activityPanel = new ActivityPanel(this);

        tabbedPane.addTab("참가자 등록", registrationPanel);
        tabbedPane.addTab("멘토-멘티 매칭", matchingPanel);
        tabbedPane.addTab("활동 관리", activityPanel);

        add(tabbedPane);
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);
        matchingPanel.updateLists();
        dataManager.saveData(participants, matches, activities);
    }

    public void createMatch(Participant mentor, Participant mentee) {
        String matchId = mentor.getStudentId() + "-" + mentee.getStudentId();
        matches.put(matchId, new Pair(mentor, mentee));
        activityPanel.updatePairSelector();
        dataManager.saveData(participants, matches, activities);
    }

    public void addActivity(String pairId, Activity activity) {
        if (!activities.containsKey(pairId)) {
            activities.put(pairId, new ArrayList<>());
        }
        activities.get(pairId).add(activity);
        dataManager.saveData(participants, matches, activities);
    }

    private void loadData() {
        Object[] data = dataManager.loadData();
        if (data != null) {
            participants = (ArrayList<Participant>) data[0];
            matches = (HashMap<String, Pair>) data[1];
            activities = (HashMap<String, ArrayList<Activity>>) data[2];

            matchingPanel.updateLists();
            activityPanel.updatePairSelector();
        }
    }

    // Getters
    public ArrayList<Participant> getParticipants() { return participants; }
    public HashMap<String, Pair> getMatches() { return matches; }
    public HashMap<String, ArrayList<Activity>> getActivities() { return activities; }
}