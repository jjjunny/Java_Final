package com.globalbridge.gui;

import com.globalbridge.model.Participant;
import com.globalbridge.model.Pair;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kim Hyeong Jun
 * @version 1.0
 * @since 2024-12-09
 *
 * @version 1.1
 * @since 2024-12-21
 *
 * @version 1.2
 * @since 2024-12-24
 *
 * 멘토-멘티 매칭을 관리하는 GUI 패널 클래스.
 *
 * <p>
 * 이 클래스는 글로벌 브릿지 프로그램에서 등록된 참가자들을 멘토(Korean)와 멘티(English)로 구분하여 표시하고,
 * 자동 또는 수동으로 매칭을 수행할 수 있는 사용자 인터페이스를 제공합니다.
 * 또한, 매칭 데이터를 파일에 저장하거나 불러오는 기능도 제공합니다.
 * </p>
 */
public class MatchingPanel extends JPanel {

    private GlobalBridgeProgram mainProgram;
    private DefaultListModel<Participant> mentorModel;
    private DefaultListModel<Participant> menteeModel;
    private JList<Participant> mentorList;
    private JList<Participant> menteeList;
    private JTextArea matchingResultArea;

    /**
     * MatchingPanel 생성자.
     *
     * @param mainProgram 메인 프로그램 객체. 참가자 데이터와 매칭 정보를 관리합니다.
     */

    public MatchingPanel(GlobalBridgeProgram mainProgram) {
        this.mainProgram = mainProgram;
        setLayout(new BorderLayout(10, 10));
        initComponents();
        updateLists();
    }

    /**
     * 패널의 UI 컴포넌트를 초기화하고 배치합니다.
     */
    private void initComponents() {
        mentorModel = new DefaultListModel<>();
        menteeModel = new DefaultListModel<>();

        mentorList = new JList<>(mentorModel);
        menteeList = new JList<>(menteeModel);

        mentorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menteeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        matchingResultArea = new JTextArea(10, 40);
        matchingResultArea.setEditable(false);
        matchingResultArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        matchingResultArea.setBorder(BorderFactory.createTitledBorder("매칭 결과"));

        JPanel listPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        listPanel.add(createListPanel("멘토 목록 (Korean)", mentorList));
        listPanel.add(createListPanel("멘티 목록 (English)", menteeList));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton autoMatchButton = new JButton("자동 매칭");
        JButton manualMatchButton = new JButton("수동 매칭");
        JButton saveButton = new JButton("저장");
        JButton loadButton = new JButton("불러오기");

        autoMatchButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        manualMatchButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        saveButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        loadButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        autoMatchButton.addActionListener(e -> performAutoMatching());
        manualMatchButton.addActionListener(e -> performManualMatching());
        saveButton.addActionListener(e -> saveMatchesToFile());
        loadButton.addActionListener(e -> loadMatchesFromFile());

        buttonPanel.add(autoMatchButton);
        buttonPanel.add(manualMatchButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        add(buttonPanel, BorderLayout.NORTH);
        add(listPanel, BorderLayout.CENTER);
        add(new JScrollPane(matchingResultArea), BorderLayout.SOUTH);
    }

    /**
     * 제목이 있는 스크롤 가능한 리스트 패널을 생성합니다.
     */
    private JPanel createListPanel(String title, JList<Participant> list) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        return panel;
    }

    /**
     * 멘토와 멘티 목록을 최신 데이터로 업데이트합니다.
     */
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

    /**
     * 자동 매칭을 수행합니다.
     */
    private void performAutoMatching() {
        List<Participant> mentors = new ArrayList<>();
        List<Participant> mentees = new ArrayList<>();

        for (int i = 0; i < mentorModel.size(); i++) {
            mentors.add(mentorModel.getElementAt(i));
        }

        for (int i = 0; i < menteeModel.size(); i++) {
            mentees.add(menteeModel.getElementAt(i));
        }

        int matchCount = Math.min(mentors.size(), mentees.size());

        for (int i = 0; i < matchCount; i++) {
            Participant mentor = mentors.get(i);
            Participant mentee = mentees.get(i);
            mainProgram.createMatch(mentor, mentee);
        }

        updateMatchingResult();

        JOptionPane.showMessageDialog(this,
                matchCount + "개의 매칭이 완료되었습니다.",
                "매칭 성공",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 수동 매칭을 수행합니다.
     */
    private void performManualMatching() {
        if (mentorList.getSelectedValue() == null || menteeList.getSelectedValue() == null) {
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

        JOptionPane.showMessageDialog(this,
                "수동 매칭이 완료되었습니다.",
                "매칭 성공",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 현재까지의 매칭 결과를 텍스트 영역에 업데이트합니다.
     */
    private void updateMatchingResult() {
        StringBuilder result = new StringBuilder("현재 매칭 현황:\n\n");

        for (Pair pair : mainProgram.getMatches().values()) {
            result.append(String.format("멘토: %s (Korean) - 멘티: %s (English)\n",
                    pair.getMentor().getName(),
                    pair.getMentee().getName()));
        }

        matchingResultArea.setText(result.toString());
    }

    /**
     * 매칭 데이터를 파일에 저장합니다.
     */
    private void saveMatchesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("matches.txt"))) {
            for (Pair pair : mainProgram.getMatches().values()) {
                writer.write(String.format("%s,%s,%s,%s\n",
                        pair.getMentor().getName(),
                        pair.getMentor().getStudentId(),
                        pair.getMentee().getName(),
                        pair.getMentee().getStudentId()));
            }
            JOptionPane.showMessageDialog(this,
                    "매칭 데이터가 성공적으로 저장되었습니다.",
                    "저장 성공",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "파일 저장 중 오류가 발생했습니다: " + e.getMessage(),
                    "오류",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 매칭 데이터를 파일에서 불러옵니다.
     */
    private void loadMatchesFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("matches.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4) { // 데이터 검증
                    Participant mentor = new Participant(data[0], data[1], null, "Korean", 1);
                    Participant mentee = new Participant(data[2], data[3], null, "English", 1);

                    mainProgram.createMatch(mentor, mentee);
                }
            }
            updateMatchingResult();
            JOptionPane.showMessageDialog(this,
                    "매칭 데이터가 성공적으로 불러와졌습니다.",
                    "불러오기 성공",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "파일 불러오기 중 오류가 발생했습니다: " + e.getMessage(),
                    "오류",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
