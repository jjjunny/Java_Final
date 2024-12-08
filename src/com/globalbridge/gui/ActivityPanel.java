package com.globalbridge.gui;

import com.globalbridge.model.Activity;
import com.globalbridge.model.Pair;
import javax.swing.*;
import java.awt.*;
import java.util.Date;

/**
 * @author YourName
 * @version 1.0
 * @since 2024-12-09
 *
 * 멘토-멘티 활동을 기록하고 관리하는 GUI 패널 클래스입니다.
 *
 * <p>
 * 이 클래스는 매칭된 멘토-멘티 쌍의 활동을 기록하고 조회하는 기능을 제공합니다.
 * 활동 내용, 장소, 시간 등의 정보를 입력받아 저장하고,
 * 페어별 활동 이력을 시각적으로 표시합니다.
 * </p>
 *
 * <p>
 * 주요 기능:
 * <ul>
 *   <li>멘토-멘티 쌍 선택</li>
 *   <li>활동 내용 및 장소 입력</li>
 *   <li>활동 기록 저장</li>
 *   <li>활동 이력 조회</li>
 * </ul>
 * </p>
 *
 * @see GlobalBridgeProgram
 * @see Activity
 * @see Pair
 */
public class ActivityPanel extends JPanel {
    /** 메인 프로그램 참조 */
    private GlobalBridgeProgram mainProgram;

    /** 멘토-멘티 쌍 선택 콤보박스 */
    private JComboBox<String> pairSelector;

    /** 활동 내용 입력 필드 */
    private JTextField contentField;

    /** 활동 장소 입력 필드 */
    private JTextField locationField;

    /** 활동 이력 표시 영역 */
    private JTextArea activityHistoryArea;

    /**
     * ActivityPanel을 생성하고 초기화합니다.
     *
     * @param mainProgram 메인 프로그램 객체
     */
    public ActivityPanel(GlobalBridgeProgram mainProgram) {
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
     *   <li>멘토-멘티 쌍 선택 콤보박스</li>
     *   <li>활동 내용 입력 필드</li>
     *   <li>활동 장소 입력 필드</li>
     *   <li>활동 등록 버튼</li>
     *   <li>활동 이력 표시 영역</li>
     * </ul>
     * </p>
     */
    private void initComponents() {
        // 입력 패널
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 컴포넌트 초기화
        pairSelector = new JComboBox<>();
        contentField = new JTextField(20);
        locationField = new JTextField(20);
        activityHistoryArea = new JTextArea();
        activityHistoryArea.setEditable(false);

        // 컴포넌트 배치
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("멘토-멘티 쌍:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(pairSelector, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("활동 내용:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(contentField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("활동 장소:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(locationField, gbc);

        // 등록 버튼
        JButton registerButton = new JButton("활동 등록");
        gbc.gridx = 1; gbc.gridy = 3;
        inputPanel.add(registerButton, gbc);

        registerButton.addActionListener(e -> registerActivity());

        // 전체 레이아웃
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(activityHistoryArea), BorderLayout.CENTER);

        // 데이터 갱신
        updatePairSelector();
        updateActivityHistory();
    }

    /**
     * 멘토-멘티 쌍 선택 콤보박스를 최신 데이터로 업데이트합니다.
     *
     * <p>
     * 현재 매칭된 모든 멘토-멘티 쌍을 콤보박스에 표시합니다.
     * 표시 형식: "매칭ID (멘토이름-멘티이름)"
     * </p>
     */
    public void updatePairSelector() {
        pairSelector.removeAllItems();
        for (String pairId : mainProgram.getMatches().keySet()) {
            Pair pair = mainProgram.getMatches().get(pairId);
            pairSelector.addItem(String.format("%s (%s-%s)",
                    pairId,
                    pair.getMentor().getName(),
                    pair.getMentee().getName()));
        }
    }

    /**
     * 새로운 활동을 등록합니다.
     *
     * <p>
     * 선택된 멘토-멘티 쌍에 대해 입력된 활동 정보를 저장합니다.
     * 활동 등록 시 현재 시간이 자동으로 기록됩니다.
     * </p>
     *
     * <p>
     * 등록 조건:
     * <ul>
     *   <li>멘토-멘티 쌍이 선택되어 있어야 함</li>
     *   <li>활동 내용이 입력되어 있어야 함</li>
     *   <li>활동 장소가 입력되어 있어야 함</li>
     * </ul>
     * </p>
     */
    private void registerActivity() {
        if (pairSelector.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "매칭된 멘토-멘티 쌍을 선택해주세요.",
                    "알림",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String pairId = pairSelector.getSelectedItem().toString().split(" ")[0];
        Activity activity = new Activity(
                new Date(),
                contentField.getText(),
                locationField.getText()
        );

        mainProgram.addActivity(pairId, activity);
        clearFields();
        updateActivityHistory();

        JOptionPane.showMessageDialog(this, "활동이 등록되었습니다.");
    }

    /**
     * 활동 이력을 업데이트하여 표시합니다.
     *
     * <p>
     * 모든 멘토-멘티 쌍의 활동 이력을 시간순으로 정렬하여 표시합니다.
     * 각 활동에는 날짜, 내용, 장소 정보가 포함됩니다.
     * </p>
     *
     * <p>
     * 표시 형식:
     * [ 멘토이름 (Korean) - 멘티이름 (English) ]
     * - 날짜 | 활동내용 @ 활동장소 [상태]
     * </p>
     */
    private void updateActivityHistory() {
        StringBuilder history = new StringBuilder("활동 내역:\n\n");
        for (String pairId : mainProgram.getActivities().keySet()) {
            Pair pair = mainProgram.getMatches().get(pairId);
            history.append(String.format("[ %s (Korean) - %s (English) ]\n",
                    pair.getMentor().getName(),
                    pair.getMentee().getName()));

            for (Activity activity : mainProgram.getActivities().get(pairId)) {
                history.append("- ").append(activity.toString()).append("\n");
            }
            history.append("\n");
        }
        activityHistoryArea.setText(history.toString());
    }

    /**
     * 입력 필드들을 초기화합니다.
     *
     * <p>
     * 활동 내용과 장소 입력 필드를 비웁니다.
     * 새로운 활동 등록을 위한 준비 상태로 만듭니다.
     * </p>
     */
    private void clearFields() {
        contentField.setText("");
        locationField.setText("");
    }
}