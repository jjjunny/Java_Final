package com.globalbridge.gui;

import com.globalbridge.model.Activity;
import com.globalbridge.model.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

/**
 * @author Kim Hyeong Jun
 * @version 1.0
 * @since 2024-12-09
 *
 *
 * @version 1.1
 * @since 2024-12-21
 * 멘토-멘티 활동을 기록하고 관리하는 GUI 패널 클래스.
 *
 * <p>
 * 이 클래스는 매칭된 멘토-멘티 쌍의 활동을 기록하고 조회하는 기능을 제공합니다.
 * 사용자는 활동 내용, 장소, 시간 등의 정보를 입력하여 저장할 수 있으며,
 * 각 멘토-멘티 쌍의 활동 이력을 시각적으로 확인할 수 있습니다.
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

 *
 * @see GlobalBridgeProgram
 * @see Activity
 * @see Pair
 */
public class ActivityPanel extends JPanel {

    /**
     * 메인 프로그램 객체 참조.
     *
     * <p>활동 데이터를 저장하거나 가져오는 데 사용됩니다.</p>
     */
    private GlobalBridgeProgram mainProgram;

    /**
     * 멘토-멘티 쌍 선택 콤보박스.
     *
     * <p>현재 매칭된 멘토-멘티 쌍 목록을 표시합니다.</p>
     */
    private JComboBox<String> pairSelector;

    /**
     * 활동 내용 입력 필드.
     *
     * <p>사용자가 활동 내용을 입력할 수 있는 텍스트 필드입니다.</p>
     */
    private JTextField contentField;

    /**
     * 활동 장소 입력 필드.
     *
     * <p>사용자가 활동 장소를 입력할 수 있는 텍스트 필드입니다.</p>
     */
    private JTextField locationField;

    /**
     * 활동 이력을 표시하는 텍스트 영역.
     *
     * <p>모든 멘토-멘티 쌍의 활동 이력을 텍스트로 표시합니다.</p>
     */
    private JTextArea activityHistoryArea;

    /**
     * ActivityPanel 생성자.
     *
     * <p>패널의 레이아웃과 UI 컴포넌트를 초기화합니다.</p>
     *
     * @param mainProgram 메인 프로그램 객체. 활동 데이터를 관리합니다.
     */
    public ActivityPanel(GlobalBridgeProgram mainProgram) {
        this.mainProgram = mainProgram;
        setLayout(new BorderLayout(10, 10));
        initComponents();
        updatePairSelector();
        updateActivityHistory();
    }

    /**
     * 패널의 UI 컴포넌트들을 초기화하고 배치합니다.
     *
     * <p>
     * 다음 컴포넌트들이 초기화됩니다:
     * <ul>
     *   <li>멘토-멘티 쌍 선택 콤보박스</li>
     *   <li>활동 내용 및 장소 입력 필드</li>
     *   <li>활동 등록 버튼</li>
     *   <li>활동 이력 표시 영역</li>
     * </ul>
     * </p>
     */
    private void initComponents() {
        // 상단 입력 패널
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 콤보박스 및 입력 필드 초기화
        pairSelector = new JComboBox<>();
        contentField = new JTextField(20);
        locationField = new JTextField(20);

        // 활동 이력 표시 영역 초기화
        activityHistoryArea = new JTextArea(15, 40);
        activityHistoryArea.setEditable(false);
        activityHistoryArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        activityHistoryArea.setBorder(BorderFactory.createTitledBorder("활동 이력"));

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

        // 등록 버튼 추가
        JButton registerButton = new JButton("활동 등록");
        registerButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        registerButton.addActionListener(e -> registerActivity());

        gbc.gridx = 1; gbc.gridy = 3;
        inputPanel.add(registerButton, gbc);

        // 전체 레이아웃 구성
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(activityHistoryArea), BorderLayout.CENTER);
    }

    /**
     * 멘토-멘티 쌍 선택 콤보박스를 최신 데이터로 업데이트합니다.
     *
     * <p>현재 매칭된 모든 멘토-멘티 쌍을 콤보박스에 추가합니다.</p>
     */
    public void updatePairSelector() {
        pairSelector.removeAllItems();

        for (String pairId : mainProgram.getMatches().keySet()) {
            Pair pair = mainProgram.getMatches().get(pairId);
            pairSelector.addItem(String.format("%s (%s - %s)",
                    pairId,
                    pair.getMentor().getName(),
                    pair.getMentee().getName()));
        }
    }

    /**
     * 새로운 활동을 등록합니다.
     *
     * <p>선택된 멘토-멘티 쌍에 대해 입력된 활동 정보를 저장합니다.</p>
     *
     * @throws IllegalArgumentException 활동 정보가 비어 있는 경우 예외를 발생시킵니다.
     */
    private void registerActivity() {
        if (pairSelector.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "매칭된 멘토-멘티 쌍을 선택해주세요.",
                    "경고",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String pairId = pairSelector.getSelectedItem().toString().split(" ")[0];

        if (contentField.getText().trim().isEmpty() || locationField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "활동 내용과 장소를 모두 입력해주세요.",
                    "경고",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Activity activity = new Activity(
                new Date(),
                contentField.getText(),
                locationField.getText()
        );

        mainProgram.addActivity(pairId, activity);

        clearFields();

        updateActivityHistory();

        JOptionPane.showMessageDialog(this,
                "활동이 성공적으로 등록되었습니다.",
                "등록 완료",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 모든 멘토-멘티 쌍의 활동 이력을 업데이트하여 표시합니다.
     *
     * <p>각 멘토-멘티 쌍의 활동 내역을 시간순으로 정렬하여 텍스트 영역에 표시합니다.</p>
     */
    private void updateActivityHistory() {
        StringBuilder history = new StringBuilder("활동 내역:\n\n");

        for (String pairId : mainProgram.getActivities().keySet()) {
            Pair pair = mainProgram.getMatches().get(pairId);
            history.append(String.format("[ %s - %s ]\n",
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
     * <p>활동 내용과 장소 입력 필드를 비웁니다.</p>
     */
    private void clearFields() {
        contentField.setText("");
        locationField.setText("");
    }
}
