package com.globalbridge.gui;

import com.globalbridge.model.Participant;
import com.globalbridge.model.Pair;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MatchingPanel extends JPanel {
    private GlobalBridgeProgram mainProgram;
    private DefaultListModel<Participant> mentorModel;
    private DefaultListModel<Participant> menteeModel;
    private JList<Participant> mentorList;
    private JList<Participant> menteeList;
    private JTextArea matchingResultArea;

    public MatchingPanel(GlobalBridgeProgram mainProgram) {
        this.mainProgram = mainProgram;
        setLayout(new BorderLayout(10, 10));
        initComponents();
    }

    private void initComponents() {
        // 리스트 모델 초기화
        mentorModel = new DefaultListModel<>();
        menteeModel = new DefaultListModel<>();

        // 리스트 생성
        mentorList = new JList<>(mentorModel);
        menteeList = new JList<>(menteeModel);

        // 결과 표시 영역
        matchingResultArea = new JTextArea(10, 40);
        matchingResultArea.setEditable(false);

        // 패널 구성
        JPanel listPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        listPanel.add(createListPanel("멘토 목록 (Korean)", mentorList));
        listPanel.add(createListPanel("멘티 목록 (English)", menteeList));

        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        JButton autoMatchButton = new JButton("자동 매칭");
        JButton manualMatchButton = new JButton("수동 매칭");
        buttonPanel.add(autoMatchButton);
        buttonPanel.add(manualMatchButton);

        autoMatchButton.addActionListener(e -> performAutoMatching());
        manualMatchButton.addActionListener(e -> performManualMatching());

        // 전체 레이아웃
        add(buttonPanel, BorderLayout.NORTH);
        add(listPanel, BorderLayout.CENTER);
        add(new JScrollPane(matchingResultArea), BorderLayout.SOUTH);

        // 초기 데이터 로드
        updateLists();
    }

    private JPanel createListPanel(String title, JList<Participant> list) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        return panel;
    }

    public void updateLists() {
        mentorModel.clear();
        menteeModel.clear();

        for (Participant p : mainProgram.getParticipants()) {
            if (p.isMentor()) {
                mentorModel.addElement(p);
            } else {
                menteeModel.addElement(p);
            }
        }

        updateMatchingResult();
    }

    private void performAutoMatching() {
        List<Participant> mentors = new ArrayList<>();
        List<Participant> mentees = new ArrayList<>();

        for (int i = 0; i < mentorModel.size(); i++) {
            mentors.add(mentorModel.getElementAt(i));
        }

        for (int i = 0; i < menteeModel.size(); i++) {
            mentees.add(menteeModel.getElementAt(i));
        }

        // 자동 매칭 수행
        int matchCount = Math.min(mentors.size(), mentees.size());
        for (int i = 0; i < matchCount; i++) {
            Participant mentor = mentors.get(i);
            Participant mentee = mentees.get(i);
            mainProgram.createMatch(mentor, mentee);
        }

        updateMatchingResult();
        JOptionPane.showMessageDialog(this,
                matchCount + "개의 매칭이 완료되었습니다.");
    }

    private void performManualMatching() {
        if (mentorList.getSelectedValue() == null ||
                menteeList.getSelectedValue() == null) {
            JOptionPane.showMessageDialog(this,
                    "멘토와 멘티를 모두 선택해주세요.",
                    "알림",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Participant mentor = mentorList.getSelectedValue();
        Participant mentee = menteeList.getSelectedValue();

        mainProgram.createMatch(mentor, mentee);
        updateMatchingResult();

        JOptionPane.showMessageDialog(this, "매칭이 완료되었습니다.");
    }

    private void updateMatchingResult() {
        StringBuilder result = new StringBuilder("현재 매칭 현황:\n\n");
        for (Pair pair : mainProgram.getMatches().values()) {
            result.append(String.format("멘토: %s (Korean) - 멘티: %s (English)\n",
                    pair.getMentor().getName(),
                    pair.getMentee().getName()));
        }
        matchingResultArea.setText(result.toString());
    }
}