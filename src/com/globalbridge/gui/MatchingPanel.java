package com.globalbridge.gui;

import com.globalbridge.model.Participant;
import com.globalbridge.model.Pair;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Kim Hyeong Jun
 * @version 1.0
 * @since 2024-12-09
 *
 * 멘토-멘티 매칭을 관리하는 GUI 패널 클래스입니다.
 *
 * <p>
 * 이 클래스는 등록된 참가자들을 멘토(Korean)와 멘티(English)로 구분하여 표시하고,
 * 자동 또는 수동으로 매칭을 수행할 수 있는 인터페이스를 제공합니다.
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
 * @see GlobalBridgeProgram
 * @see Participant
 * @see Pair
 */
public class MatchingPanel extends JPanel {
    /** 메인 프로그램 참조 */
    private GlobalBridgeProgram mainProgram;

    /** 멘토 목록을 위한 리스트 모델 */
    private DefaultListModel<Participant> mentorModel;

    /** 멘티 목록을 위한 리스트 모델 */
    private DefaultListModel<Participant> menteeModel;

    /** 멘토 선택을 위한 JList */
    private JList<Participant> mentorList;

    /** 멘티 선택을 위한 JList */
    private JList<Participant> menteeList;

    /** 매칭 결과 표시 영역 */
    private JTextArea matchingResultArea;

    /**
     * MatchingPanel을 생성하고 초기화합니다.
     *
     * @param mainProgram 메인 프로그램 객체
     */
    public MatchingPanel(GlobalBridgeProgram mainProgram) {
        this.mainProgram = mainProgram;
        setLayout(new BorderLayout(10, 10));
        initComponents();
    }

    /**
     * 패널의 UI 컴포넌트들을 초기화하고 배치합니다.
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

        updateLists();
    }    /**
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
     * 업데이트 후 매칭 결과도 함께 갱신됩니다.
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
     * 현재 등록된 멘토와 멘티를 순서대로 1:1 매칭합니다.
     * 매칭 가능한 최대 쌍을 생성하며, 남는 인원은 매칭되지 않습니다.
     * </p>
     *
     * <p>
     * 매칭 프로세스:
     * <ul>
     *   <li>멘토와 멘티 목록을 각각 리스트로 변환</li>
     *   <li>두 리스트 중 더 작은 크기만큼 매칭 수행</li>
     *   <li>매칭된 결과를 시스템에 등록</li>
     * </ul>
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

    /**
     * 수동 매칭을 수행합니다.
     *
     * <p>
     * 사용자가 선택한 멘토와 멘티를 매칭합니다.
     * 양쪽 모두 선택되지 않은 경우 경고 메시지를 표시합니다.
     * </p>
     *
     * <p>
     * 매칭 조건:
     * <ul>
     *   <li>멘토와 멘티가 모두 선택되어야 함</li>
     *   <li>멘토는 Korean 언어 사용자여야 함</li>
     *   <li>멘티는 English 언어 사용자여야 함</li>
     * </ul>
     * </p>
     */
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

    /**
     * 현재까지의 매칭 결과를 텍스트 영역에 업데이트합니다.
     *
     * <p>
     * 매칭된 모든 멘토-멘티 쌍의 정보를 포맷에 맞춰 표시합니다.
     * 각 쌍은 멘토와 멘티의 이름과 사용 언어를 포함합니다.
     * </p>
     *
     * <p>
     * 표시 형식:
     * 멘토: [이름] (Korean) - 멘티: [이름] (English)
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
}