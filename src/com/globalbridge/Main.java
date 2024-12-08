package com.globalbridge;

import com.globalbridge.gui.GlobalBridgeProgram;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Swing 테마 설정 (시스템 기본 테마 사용)
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // GUI 실행
        SwingUtilities.invokeLater(() -> {
            GlobalBridgeProgram program = new GlobalBridgeProgram();
            program.setVisible(true);
        });
    }
}