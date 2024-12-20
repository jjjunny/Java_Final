package com.globalbridge.gui;

import com.globalbridge.model.Participant;

import javax.swing.*;
import java.awt.*;

/**
 * 참가자 등록을 위한 GUI 패널 클래스.
 *
 * <p>
 * 이 클래스는 글로벌 브릿지 프로그램에서 참가자를 등록하기 위한 사용자 인터페이스를 제공합니다.
 * 사용자는 이름, 학번, 전공, 언어, 학년 정보를 입력한 후 등록 버튼을 클릭하여 새로운 참가자를 시스템에 추가할 수 있습니다.
 * </p>
 *
 * <p>
 * 주요 기능:
 * <ul>
 *     <li>사용자로부터 이름, 학번, 전공 입력</li>
 *     <li>언어와 학년 선택을 위한 콤보박스 제공</li>
 *     <li>등록 버튼을 통한 데이터 유효성 검사 및 등록 처리</li>
 *     <li>입력값 검증: 공백 확인, 유효한 문자 형식 검사</li>
 * </ul>
 * </p>
 *
 * <p>
 * 이 클래스는 {@link GlobalBridgeProgram}과 {@link Participant} 클래스와 상호작용합니다.
 * </p>
 *
 * @author Kim Hyeong Jun
 * @version 1.1
 * @since 2024-12-21
 */
public class RegistrationPanel extends JPanel {

    /**
     * 메인 프로그램 객체 참조.
     *
     * <p>
     * {@link GlobalBridgeProgram} 객체를 참조하여 참가자 등록 시 데이터를 전달하거나
     * 프로그램의 다른 기능과 상호작용할 수 있습니다.
     * </p>
     * <ul>
     *     <li>이름 입력 필드</li>
     *     <li>학번 입력 필드</li>
     *     <li>전공 입력 필드</li>
     *     <li>언어 선택 콤보박스</li>
     *     <li>학년 선택 콤보박스</li>
     * </ul>
     */
    private GlobalBridgeProgram mainProgram;
    private JTextField nameField;
    private JTextField studentIdField;
    private JTextField majorField;
    private JComboBox<String> languageBox;
    private JComboBox<Integer> gradeBox;

    /**
     * RegistrationPanel 생성자.
     *
     * <p>
     * 이 생성자는 참가자 등록 패널의 레이아웃과 UI 컴포넌트를 초기화합니다.
     * BorderLayout을 사용하여 제목, 폼 입력 영역, 버튼 영역으로 구성됩니다.
     * </p>
     *
     * @param mainProgram 메인 프로그램 객체. 참가자 데이터를 전달하거나 다른 기능과 상호작용하기 위해 사용됩니다.
     */
    public RegistrationPanel(GlobalBridgeProgram mainProgram) {
        this.mainProgram = mainProgram;
        setLayout(new BorderLayout());
        initComponents();
    }

    /**
     * 패널의 UI 컴포넌트를 초기화하고 배치합니다.
     *
     * <p>
     * 이 메서드는 다음과 같이 UI를 구성합니다:
     * <ul>
     *   <li>상단 제목 레이블: "참가자 등록" 텍스트를 표시</li>
     *   <li>중앙 입력 폼: 이름, 학번, 전공 입력 필드와 언어 및 학년 선택 콤보박스를 배치</li>
     *   <li>하단 버튼 영역: "등록" 버튼을 배치하고 클릭 이벤트 리스너를 설정</li>
     * </ul>
     * </p>
     */
    private void initComponents() {
        // 상단 제목 패널
        JLabel titleLabel = new JLabel("참가자 등록", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // 중앙 입력 폼 패널
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // 입력 필드 초기화
        nameField = new JTextField(20);
        studentIdField = new JTextField(20);
        majorField = new JTextField(20);
        languageBox = new JComboBox<>(new String[]{"Korean", "English"});
        gradeBox = new JComboBox<>(new Integer[]{1, 2, 3, 4});

        // 필드 추가
        addComponent(formPanel, "이름:", nameField, 0, gbc);
        addComponent(formPanel, "학번:", studentIdField, 1, gbc);
        addComponent(formPanel, "전공:", majorField, 2, gbc);
        addComponent(formPanel, "언어:", languageBox, 3, gbc);
        addComponent(formPanel, "학년:", gradeBox, 4, gbc);

        add(formPanel, BorderLayout.CENTER);

        // 하단 버튼 패널
        JPanel buttonPanel = new JPanel();
        JButton registerButton = new JButton("등록");

        // 버튼 스타일 설정
        registerButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        registerButton.setPreferredSize(new Dimension(100, 40));

        // 버튼 클릭 이벤트 리스너 추가
        registerButton.addActionListener(e -> registerParticipant());

        buttonPanel.add(registerButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * 레이블과 컴포넌트를 지정된 위치에 추가합니다.
     *
     * @param panel 컴포넌트를 추가할 패널
     * @param labelText 레이블에 표시될 텍스트
     * @param component 추가할 컴포넌트 (텍스트 필드 또는 콤보박스)
     * @param row 컴포넌트를 배치할 행 번호
     * @param gbc GridBagConstraints 객체로 레이아웃 제약 조건을 설정합니다.
     */
    private void addComponent(JPanel panel,
                              String labelText,
                              JComponent component,
                              int row,
                              GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = row;

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("맑은 고딕", Font.PLAIN, 14));

        panel.add(label, gbc);

        gbc.gridx = 1;

        if (component instanceof JTextField || component instanceof JComboBox) {
            component.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
            component.setPreferredSize(new Dimension(200, 30));
            if (component instanceof JTextField) {
                ((JTextField) component).setHorizontalAlignment(SwingConstants.LEFT);
            }
            panel.add(component, gbc);
        }
    }

    /**
     * 참가자를 등록하는 메서드.
     *
     * <p>
     * 사용자가 입력한 정보를 검증한 후 유효한 경우 새로운 Participant 객체를 생성하여 시스템에 등록합니다.
     * 입력값이 유효하지 않은 경우 오류 메시지를 표시합니다.
     * </p>
     */
    private void registerParticipant() {
        String name = nameField.getText().trim();
        String studentId = studentIdField.getText().trim();
        String major = majorField.getText().trim();

        // 공백 검사
        if (name.isEmpty() || studentId.isEmpty() || major.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "모든 필드를 입력해주세요.",
                    "오류",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 이름과 전공 유효성 검사 (한글/영문만 허용)
        if (!name.matches("^[a-zA-Z가-힣]+$") || !major.matches("^[a-zA-Z가-힣]+$")) {
            JOptionPane.showMessageDialog(this,
                    "이름과 전공은 한글 또는 영어만 입력 가능합니다.",
                    "오류",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 학번 유효성 검사 (숫자만 허용)
        if (!studentId.matches("^\\d+$")) {
            JOptionPane.showMessageDialog(this,
                    "학번은 숫자만 입력 가능합니다.",
                    "오류",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Participant participant = new Participant(
                    name,
                    studentId,
                    major,
                    languageBox.getSelectedItem().toString(),
                    (Integer) gradeBox.getSelectedItem()
            );
            mainProgram.addParticipant(participant);

            clearFields();
            JOptionPane.showMessageDialog(this,
                    "참가자가 성공적으로 등록되었습니다.",
                    "성공",
                    JOptionPane.INFORMATION_MESSAGE);

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
     * 텍스트 필드는 비우고 콤보박스는 기본값으로 설정합니다.
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