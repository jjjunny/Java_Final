package com.globalbridge.gui;

import com.globalbridge.model.Participant;
import javax.swing.*;
import java.awt.*;

public class RegistrationPanel extends JPanel {
    private GlobalBridgeProgram mainProgram;
    private JTextField nameField;
    private JTextField studentIdField;
    private JTextField majorField;
    private JComboBox<String> languageBox;
    private JComboBox<Integer> gradeBox;

    public RegistrationPanel(GlobalBridgeProgram mainProgram) {
        this.mainProgram = mainProgram;
        setLayout(new GridBagLayout());
        initComponents();
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // 컴포넌트 초기화
        nameField = new JTextField(20);
        studentIdField = new JTextField(20);
        majorField = new JTextField(20);
        languageBox = new JComboBox<>(new String[]{"Korean", "English"});
        gradeBox = new JComboBox<>(new Integer[]{1, 2, 3, 4});

        // 컴포넌트 배치
        addComponent("이름:", nameField, 0, gbc);
        addComponent("학번:", studentIdField, 1, gbc);
        addComponent("전공:", majorField, 2, gbc);
        addComponent("언어:", languageBox, 3, gbc);
        addComponent("학년:", gradeBox, 4, gbc);

        // 등록 버튼
        JButton registerButton = new JButton("등록");
        gbc.gridy = 5;
        gbc.gridx = 1;
        add(registerButton, gbc);

        registerButton.addActionListener(e -> registerParticipant());
    }

    private void addComponent(String label, JComponent component, int row,
                              GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel(label), gbc);

        gbc.gridx = 1;
        add(component, gbc);
    }

    private void registerParticipant() {
        try {
            Participant participant = new Participant(
                    nameField.getText(),
                    studentIdField.getText(),
                    majorField.getText(),
                    languageBox.getSelectedItem().toString(),
                    (Integer)gradeBox.getSelectedItem()
            );

            mainProgram.addParticipant(participant);
            clearFields();
            JOptionPane.showMessageDialog(this,
                    "참가자가 등록되었습니다. (" +
                            (participant.isMentor() ? "멘토" : "멘티") +
                            "로 배정됨)");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "등록 중 오류가 발생했습니다: " + e.getMessage(),
                    "오류",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        nameField.setText("");
        studentIdField.setText("");
        majorField.setText("");
        languageBox.setSelectedIndex(0);
        gradeBox.setSelectedIndex(0);
    }
}