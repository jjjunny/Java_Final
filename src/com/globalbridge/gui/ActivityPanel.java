package com.globalbridge.gui;

import com.globalbridge.model.Activity;
import com.globalbridge.model.Pair;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Date;

/**
 * @author Kim Hyeong Jun
 * @version 1.0
 * @since 2024-12-09
 *
 *
 * @version 1.1
 * @since 2024-12-21
 *
 * @version 1.2
 * @since 2024-12-23
 *
 * 멘토-멘티 활동을 기록하고 관리하는 GUI 패널 클래스.
 *
 * <p>
 * 이 클래스는 매칭된 멘토-멘티 쌍의 활동을 기록하고 조회하는 기능을 제공합니다.
 * 사용자는 활동 내용, 장소, 시간 등의 정보를 입력하여 저장할 수 있으며,
 * 각 멘토-멘티 쌍의 활동 이력을 시각적으로 확인할 수 있습니다.
 * 또한, 활동 데이터를 파일에 저장하거나 불러오는 기능도 제공합니다.
 * </p>
 */
public class ActivityPanel extends JPanel {

    private GlobalBridgeProgram mainProgram;
    private JComboBox<String> pairSelector;
    private JTextField contentField;
    private JTextField locationField;
    private JTextArea activityHistoryArea;

    /**
     * ActivityPanel 생성자.
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
     */
    private void initComponents() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        pairSelector = new JComboBox<>();
        contentField = new JTextField(20);
        locationField = new JTextField(20);

        activityHistoryArea = new JTextArea(15, 40);
        activityHistoryArea.setEditable(false);
        activityHistoryArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        activityHistoryArea.setBorder(BorderFactory.createTitledBorder("활동 이력"));

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

        JButton registerButton = new JButton("활동 등록");
        JButton saveButton = new JButton("저장");
        JButton loadButton = new JButton("불러오기");

        registerButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        saveButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        loadButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        registerButton.addActionListener(e -> registerActivity());
        saveButton.addActionListener(e -> saveActivitiesToFile());
        loadButton.addActionListener(e -> loadActivitiesFromFile());

        gbc.gridx = 1; gbc.gridy = 3;
        inputPanel.add(registerButton, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(activityHistoryArea), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * 멘토-멘티 쌍 선택 콤보박스를 최신 데이터로 업데이트합니다.
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
     */
    private void clearFields() {
        contentField.setText("");
        locationField.setText("");
    }

    /**
     * 활동 데이터를 파일에 저장합니다.
     */
    private void saveActivitiesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("activities.txt"))) {
            for (String pairId : mainProgram.getActivities().keySet()) {
                Pair pair = mainProgram.getMatches().get(pairId);
                writer.write(String.format("[ %s - %s ]\n",
                        pair.getMentor().getName(),
                        pair.getMentee().getName()));

                for (Activity activity : mainProgram.getActivities().get(pairId)) {
                    writer.write("- " + activity.toString() + "\n");
                }
                writer.write("\n");
            }
            JOptionPane.showMessageDialog(this,
                    "활동 데이터가 성공적으로 저장되었습니다.",
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
     * 활동 데이터를 파일에서 불러옵니다.
     */
    private void loadActivitiesFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("activities.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // 읽은 데이터 출력 (파싱 로직 추가 필요)
            }
            JOptionPane.showMessageDialog(this,
                    "활동 데이터가 성공적으로 로드되었습니다.",
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
