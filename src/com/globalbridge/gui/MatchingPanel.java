package com.globalbridge.gui;

import com.globalbridge.model.Participant;
import com.globalbridge.model.Pair;

import javax.swing.*;
import java.awt.*;
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
 * 멘토-멘티 매칭을 관리하는 GUI 패널 클래스.
 *
 * <p>
 * 이 클래스는 글로벌 브릿지 프로그램에서 등록된 참가자들을 멘토(Korean)와 멘티(English)로 구분하여 표시하고,
 * 자동 또는 수동으로 매칭을 수행할 수 있는 사용자 인터페이스를 제공합니다.
 * </p>
 *
 * <p>
 * 주요 기능:
 * <ul>
 *   <li>멘토/멘티 목록 표시</li>
 *   <li>자동 매칭 - 순서대로 자동 매칭 수행</li>
 *   <li>수동 매칭 - 사용자가 선택하여 매칭</li>
 *   <li>매칭 결과 실시간 표시</li>
 * </ul>
 * </p>
 *
 * <p>
 * 이 클래스는 {@link GlobalBridgeProgram}, {@link Participant}, {@link Pair} 클래스와 상호작용합니다.
 * </p>
 *
 */
public class MatchingPanel extends JPanel {

    /**
     * 메인 프로그램 객체 참조.
     *
     * <p>
     * {@link GlobalBridgeProgram} 객체를 참조하여 참가자 데이터를 가져오거나
     * 새로운 매칭을 생성하는 데 사용됩니다.
     * </p>
     */
    private GlobalBridgeProgram mainProgram;

    /**
     * 멘토 목록을 위한 리스트 모델.
     *
     * <p>멘토 데이터를 저장하고 JList에 표시하기 위한 모델입니다.</p>
     */
    private DefaultListModel<Participant> mentorModel;

    /**
     * 멘티 목록을 위한 리스트 모델.
     *
     * <p>멘티 데이터를 저장하고 JList에 표시하기 위한 모델입니다.</p>
     */
    private DefaultListModel<Participant> menteeModel;

    /**
     * 멘토 선택을 위한 JList 컴포넌트.
     *
     * <p>사용자가 멘토를 선택할 수 있는 리스트입니다.</p>
     */
    private JList<Participant> mentorList;

    /**
     * 멘티 선택을 위한 JList 컴포넌트.
     *
     * <p>사용자가 멘티를 선택할 수 있는 리스트입니다.</p>
     */
    private JList<Participant> menteeList;

    /**
     * 매칭 결과를 표시하는 텍스트 영역.
     *
     * <p>현재까지 생성된 모든 매칭 결과를 실시간으로 표시합니다.</p>
     */
    private JTextArea matchingResultArea;

    /**
     * MatchingPanel 생성자.
     *
     * <p>
     * 이 생성자는 패널의 레이아웃과 UI 컴포넌트를 초기화하고,
     * 메인 프로그램 객체를 통해 데이터를 가져옵니다.
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
     * 다음 컴포넌트들이 초기화됩니다:
     * <ul>
     *   <li>멘토/멘티 리스트 모델과 JList</li>
     *   <li>매칭 결과 표시 영역</li>
     *   <li>자동/수동 매칭 버튼</li>
     * </ul>
     * </p>
     */
    private void initComponents() {
        // 리스트 모델 초기화
        mentorModel = new DefaultListModel<>();
        menteeModel = new DefaultListModel<>();

        // 리스트 생성
        mentorList = new JList<>(mentorModel);
        menteeList = new JList<>(menteeModel);

        // 리스트 스타일 설정
        mentorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menteeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 결과 표시 영역
        matchingResultArea = new JTextArea(10, 40);
        matchingResultArea.setEditable(false);
        matchingResultArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        matchingResultArea.setBorder(BorderFactory.createTitledBorder("매칭 결과"));

        // 패널 구성
        JPanel listPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        listPanel.add(createListPanel("멘토 목록 (Korean)", mentorList));
        listPanel.add(createListPanel("멘티 목록 (English)", menteeList));

        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton autoMatchButton = new JButton("자동 매칭");
        JButton manualMatchButton = new JButton("수동 매칭");

        autoMatchButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        manualMatchButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        buttonPanel.add(autoMatchButton);
        buttonPanel.add(manualMatchButton);

        autoMatchButton.addActionListener(e -> performAutoMatching());
        manualMatchButton.addActionListener(e -> performManualMatching());

        // 전체 레이아웃 구성
        add(buttonPanel, BorderLayout.NORTH);
        add(listPanel, BorderLayout.CENTER);
        add(new JScrollPane(matchingResultArea), BorderLayout.SOUTH);
    }

    /**
     * 제목이 있는 스크롤 가능한 리스트 패널을 생성합니다.
     *
     * @param title 패널의 제목
     * @param list 표시할 JList 컴포넌트
     * @return 생성된 패널
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
     * 전체 참가자 목록에서 언어(Korean/English)를 기준으로
     * 멘토와 멘티를 분류하여 각각의 리스트에 표시합니다.
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
 * 현재 등록된 멘토와 멘티를 순서대로 1:1 매칭합니다.
 * 남는 인원은 매칭되지 않습니다.
 * </P>.
 *
 * 예외 상황도 처리됩니다.
 * </P>
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
 * 사용자가 선택한 멘토와 멘티를 매칭합니다.
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
     * 모든 멘토-멘티 쌍의 정보를 포맷에 맞춰 표시합니다.
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
}