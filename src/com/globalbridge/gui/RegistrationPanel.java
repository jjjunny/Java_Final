package com.globalbridge.gui;

import com.globalbridge.model.Participant;
import javax.swing.*;
import java.awt.*;

/**
 * @author Kim Hyeong Jun
 * @version 1.0
 * @since 2024-12-09
 *
 * 글로벌 브릿지 프로그램의 참가자 등록을 위한 GUI 패널 클래스입니다.
 *
 * <p>
 * 이 클래스는 새로운 참가자의 정보를 입력받기 위한 폼을 제공합니다.
 * 사용자로부터 이름, 학번, 전공, 언어, 학년 정보를 입력받아
 * 새로운 참가자를 시스템에 등록합니다.
 * </p>
 *
 * <p>
 * 입력 필드 구성:
 * <ul>
 *   <li>이름: 텍스트 필드</li>
 *   <li>학번: 텍스트 필드</li>
 *   <li>전공: 텍스트 필드</li>
 *   <li>언어: 콤보박스 (Korean/English)</li>
 *   <li>학년: 콤보박스 (1~4학년)</li>
 * </ul>
 * </p>
 *
 * @see GlobalBridgeProgram
 * @see Participant
 */
public class RegistrationPanel extends JPanel {
    /** 메인 프로그램 참조 */
    private GlobalBridgeProgram mainProgram;

    /** 이름 입력 필드 */
    private JTextField nameField;

    /** 학번 입력 필드 */
    private JTextField studentIdField;

    /** 전공 입력 필드 */
    private JTextField majorField;

    /** 언어 선택 콤보박스 */
    private JComboBox<String> languageBox;

    /** 학년 선택 콤보박스 */
    private JComboBox<Integer> gradeBox;

    /**
     * RegistrationPanel을 생성하고 초기화합니다.
     *
     * @param mainProgram 메인 프로그램 객체
     */
    public RegistrationPanel(GlobalBridgeProgram mainProgram) {
        this.mainProgram = mainProgram;
        setLayout(new GridBagLayout());
        initComponents();
    }

    /**
     * 패널의 UI 컴포넌트들을 초기화하고 배치합니다.
     *
     * <p>
     * GridBagLayout을 사용하여 컴포넌트들을 정렬하고,
     * 각 입력 필드와 레이블을 배치합니다.
     * </p>
     */
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

    /**
     * 레이블과 컴포넌트를 지정된 위치에 추가합니다.
     *
     * @param label 필드 레이블
     * @param component 입력 컴포넌트
     * @param row 배치할 행 번호
     * @param gbc GridBagConstraints 객체
     */
    private void addComponent(String label, JComponent component, int row,
                              GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel(label), gbc);

        gbc.gridx = 1;
        add(component, gbc);
    }

    /**
     * 입력된 정보를 바탕으로 새로운 참가자를 등록합니다.
     *
     * <p>
     * 입력된 정보의 유효성을 검사하고, 유효한 경우 새로운 Participant 객체를
     * 생성하여 시스템에 등록합니다. 등록 성공/실패 여부를 사용자에게 알립니다.
     * </p>
     */
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

    /**
     * 모든 입력 필드를 초기화합니다.
     *
     * <p>
     * 텍스트 필드의 내용을 지우고,
     * 콤보박스들을 기본값으로 재설정합니다.
     * </p>
     */
    private void clearFields() {
        nameField.setText("");
        studentIdField.setText("");
        majorField.setText("");
        languageBox.setSelectedIndex(0);
        gradeBox.setSelectedIndex(0);
    }
}