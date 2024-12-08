package com.globalbridge.gui;

import com.globalbridge.model.Activity;
import com.globalbridge.model.Pair;
import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class ActivityPanel extends JPanel {
    private GlobalBridgeProgram mainProgram;
    private JComboBox<String> pairSelector;
    private JTextField contentField;
    private JTextField locationField;
    private JTextArea activityHistoryArea;

    public ActivityPanel(GlobalBridgeProgram mainProgram) {
        this.mainProgram = mainProgram;
        setLayout(new BorderLayout(10, 10));
        initComponents();
    }

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

    private void clearFields() {
        contentField.setText("");
        locationField.setText("");
    }
}