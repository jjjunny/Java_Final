package com.globalbridge.gui;

import com.globalbridge.model.Participant;

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
 * @since 2024-12-23
 * 참가자 등록을 위한 GUI 패널 클래스.
 *
 * <p>
 * 이 클래스는 글로벌 브릿지 프로그램에서 참가자를 등록하기 위한 사용자 인터페이스를 제공합니다.
 * 사용자는 이름, 학번, 전공, 언어, 학년 정보를 입력한 후 등록 버튼을 클릭하여 새로운 참가자를 시스템에 추가할 수 있습니다.
 * 또한, 참가자 데이터를 파일에 저장하거나 불러오는 기능도 제공합니다.
 *
 * 주요 기능:
 * <ul>
 *   <li>참가자 등록</li>
 *   <li>참가자 데이터 저장 및 불러오기</li>
 *   <li>입력 필드 초기화</li>
 * </ul>
 * </p>
 */
public class RegistrationPanel extends JPanel {

    private GlobalBridgeProgram mainProgram;
    private JTextField nameField;
    private JTextField studentIdField;
    private JTextField majorField;
    private JComboBox<String> languageBox;
    private JComboBox<Integer> gradeBox;

    /**
     * RegistrationPanel 생성자.
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
     * 상단에는 제목 레이블을 배치하고, 중앙에는 이름, 학번, 전공, 언어, 학년 입력 필드를 포함한 폼을 배치합니다.
     * 하단에는 등록, 저장, 불러오기 버튼이 포함된 버튼 패널을 배치합니다.
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
        JButton saveButton = new JButton("저장");
        JButton loadButton = new JButton("불러오기");

        // 버튼 스타일 설정
        registerButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        saveButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        loadButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));

        registerButton.setPreferredSize(new Dimension(100, 40));
        saveButton.setPreferredSize(new Dimension(100, 40));
        loadButton.setPreferredSize(new Dimension(100, 40));

        // 버튼 클릭 이벤트 리스너 추가
        registerButton.addActionListener(e -> registerParticipant());
        saveButton.addActionListener(e -> saveParticipantsToFile());
        loadButton.addActionListener(e -> loadParticipantsFromFile());

        buttonPanel.add(registerButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * 레이블과 컴포넌트를 지정된 위치에 추가합니다.
     *
     * <p>
     * 주어진 레이블 텍스트와 컴포넌트를 {@link JPanel}에 추가하며,
     * GridBagConstraints를 사용해 위치를 지정합니다.
     * </p>
     *
     * @param panel      컴포넌트를 추가할 {@link JPanel}.
     * @param labelText  레이블에 표시할 텍스트.
     * @param component  추가할 {@link JComponent} (예: JTextField, JComboBox).
     * @param row        컴포넌트를 배치할 행 번호.
     * @param gbc        {@link GridBagConstraints} 객체로 레이아웃 제어에 사용됩니다.
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
     * 사용자가 입력한 이름, 학번, 전공 정보를 기반으로 새로운 {@link Participant} 객체를 생성하고,
     * 글로벌 브릿지 프로그램에 등록합니다. 입력값의 유효성을 검사하며,
     * 유효하지 않은 경우 경고 메시지를 표시합니다.
     * </p>
     *
     * <p>
     * 등록이 완료되면 입력 필드를 초기화하고 성공 메시지를 표시합니다.
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
     * 참가자 데이터를 파일에 저장합니다.
     *
     * <p>
     * 현재 등록된 모든 참가자 데이터를 "participants.txt" 파일에 저장합니다.
     * 각 참가자는 CSV 형식으로 저장되며, 형식은 다음과 같습니다:
     * </p>
     *
     * <pre>
     * [이름],[학번],[전공],[언어],[학년]
     * </pre>
     *
     * <p>
     * 저장이 성공적으로 완료되면 성공 메시지를 표시하며,
     * 저장 중 오류가 발생할 경우 오류 메시지를 표시합니다.
     * </p>
     */
    private void saveParticipantsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("participants.txt"))) {
            for (Participant participant : mainProgram.getParticipants()) {
                writer.write(String.format("%s,%s,%s,%s,%d\n",
                        participant.getName(),
                        participant.getStudentId(),
                        participant.getMajor(),
                        participant.getLanguage(),
                        participant.getGrade()));
            }
            JOptionPane.showMessageDialog(this,
                    "참가자 데이터가 성공적으로 저장되었습니다.",
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
     * 참가자 데이터를 파일에서 불러옵니다.
     *
     * <p>
     * "participants.txt" 파일에서 저장된 참가자 데이터를 읽어와
     * 글로벌 브릿지 프로그램에 로드합니다. 각 줄은 CSV 형식으로 되어 있으며,
     * 데이터는 다음과 같은 형식을 따릅니다:
     * </p>
     *
     * <pre>
     * [이름],[학번],[전공],[언어],[학년]
     * </pre>
     *
     * <p>
     * 데이터가 성공적으로 로드되면 관련 UI를 업데이트하고 사용자에게 성공 메시지를 표시하며,
     * 파일 읽기 중 오류가 발생할 경우 오류 메시지를 표시합니다.
     * </p>
     */
    private void loadParticipantsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("participants.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Participant participant = new Participant(
                        data[0], // 이름
                        data[1], // 학번
                        data[2], // 전공
                        data[3], // 언어
                        Integer.parseInt(data[4]) // 학년
                );
                mainProgram.addParticipant(participant);
            }
            JOptionPane.showMessageDialog(this,
                    "참가자 데이터가 성공적으로 불러와졌습니다.",
                    "불러오기 성공",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "파일 불러오기 중 오류가 발생했습니다: " + e.getMessage(),
                    "오류",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 모든 입력 필드를 초기화합니다.
     *
     * <p>입력 필드와 콤보박스의 선택값을 초기 상태로 설정합니다.</p>
     */
    private void clearFields() {
        nameField.setText("");
        studentIdField.setText("");
        majorField.setText("");
        languageBox.setSelectedIndex(0);
        gradeBox.setSelectedIndex(0);
    }
}
