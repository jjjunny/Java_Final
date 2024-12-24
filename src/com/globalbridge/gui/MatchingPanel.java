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
    /**
     * 글로벌 브릿지 프로그램의 메인 객체.
     *
     * <p>참가자 데이터와 매칭 정보를 관리합니다.</p>
     *
     * 멘토 목록을 관리하는 리스트 모델.
     * 멘토 목록을 표시하는 JList 컴포넌트.
     * 멘티 목록을 표시하는 JList 컴포넌트.
     * 현재 매칭 결과를 표시하는 텍스트 영역.
     */
    private GlobalBridgeProgram mainProgram;
    private DefaultListModel<Participant> mentorModel;
    private DefaultListModel<Participant> menteeModel;
    private JList<Participant> mentorList;
    private JList<Participant> menteeList;
    private JTextArea matchingResultArea;

    /**
     * MatchingPanel 생성자.
     *
     * <p>
     * 패널의 레이아웃과 UI 컴포넌트를 초기화하며, 참가자 데이터를 기반으로
     * 멘토 및 멘티 목록을 업데이트합니다.
     * </p>
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
     *
     * <p>
     * 멘토 및 멘티 목록, 버튼, 매칭 결과 영역을 설정하며,
     * 버튼 클릭 이벤트를 처리하는 리스너를 추가합니다.
     * </p>
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
     *
     * @param title 리스트 패널의 제목
     * @param list  표시할 JList 컴포넌트
     * @return 생성된 JPanel 객체
     */
    private JPanel createListPanel(String title, JList<Participant> list) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        return panel;
    }

    /**
     * 멘토와 멘티 목록을 최신 데이터로 업데이트합니다.
     *
     * <p>
     * 참가자 데이터를 기반으로 멘토와 멘티를 분류하여 각각의 리스트 모델에 추가합니다.
     * </p>
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
     *
     * <p>
     * 멘토와 멘티를 순서대로 매칭하며,
     * 가능한 모든 쌍이 매칭될 때까지 진행됩니다.
     * </p>
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
     *
     * <p>
     * 사용자가 멘토 목록과 멘티 목록에서 각각 하나씩 선택한 후,
     * 해당 멘토와 멘티를 매칭합니다. 매칭이 완료되면 결과를 업데이트하고
     * 사용자에게 성공 메시지를 표시합니다.
     * </p>
     *
     * <p>
     * 만약 멘토 또는 멘티가 선택되지 않은 경우, 경고 메시지를 표시하고
     * 작업을 종료합니다.
     * </p>
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
     *
     * <p>
     * 글로벌 브릿지 프로그램의 모든 매칭 데이터를 가져와
     * 텍스트 형식으로 변환한 후, 결과 영역에 표시합니다.
     * </p>
     *
     * <p>
     * 각 매칭은 "멘토: [멘토 이름] (Korean) - 멘티: [멘티 이름] (English)" 형식으로 출력됩니다.
     * </p>
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
     *
     * <p>
     * 현재까지 생성된 모든 멘토-멘티 매칭 데이터를 "matches.txt" 파일에 저장합니다.
     * 각 매칭은 CSV 형식으로 저장되며, 형식은 다음과 같습니다:
     * </p>
     *
     * <pre>
     * [멘토 이름],[멘토 학번],[멘티 이름],[멘티 학번]
     * </pre>
     *
     * <p>
     * 저장이 성공적으로 완료되면 사용자에게 성공 메시지를 표시하며,
     * 저장 중 오류가 발생할 경우 오류 메시지를 표시합니다.
     * </p>
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
     *
     * <p>
     * "matches.txt" 파일에서 저장된 매칭 데이터를 읽어와
     * 글로벌 브릿지 프로그램에 로드합니다. 각 줄은 CSV 형식으로 되어 있으며,
     * 데이터는 다음과 같은 형식을 따릅니다:
     * </p>
     *
     * <pre>
     * [멘토 이름],[멘토 학번],[멘티 이름],[멘티 학번]
     * </pre>
     *
     * <p>
     * 데이터가 성공적으로 로드되면 결과를 업데이트하고 사용자에게 성공 메시지를 표시하며,
     * 파일 읽기 중 오류가 발생할 경우 오류 메시지를 표시합니다.
     * </p>
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
