package com.globalbridge;

import com.globalbridge.gui.GlobalBridgeProgram;
import javax.swing.*;

/**
 * @author Kim Hyeong Jun
 * @version 1.0
 * @since 2024-12-09
 *
 * 글로벌 브릿지 프로그램의 메인 클래스입니다.
 *
 * <p>
 * 이 클래스는 프로그램의 진입점(entry point)으로,
 * GUI 애플리케이션을 초기화하고 실행합니다.
 * 시스템 기본 테마를 적용하여 일관된 사용자 인터페이스를 제공합니다.
 * </p>
 *
 * <p>
 * 주요 기능:
 * <ul>
 *   <li>시스템 룩앤필(Look and Feel) 설정</li>
 *   <li>메인 프로그램 윈도우 생성 및 표시</li>
 *   <li>이벤트 디스패치 스레드에서의 안전한 GUI 실행</li>
 * </ul>
 * </p>
 *
 * @see GlobalBridgeProgram
 */
public class Main {
    /**
     * 프로그램의 진입점입니다.
     *
     * <p>
     * 다음과 같은 순서로 프로그램을 초기화합니다:
     * <ol>
     *   <li>시스템 기본 룩앤필 설정 시도</li>
     *   <li>이벤트 디스패치 스레드에서 메인 윈도우 생성</li>
     *   <li>GUI 표시</li>
     * </ol>
     * </p>
     *
     * @param args 명령행 인자 (사용하지 않음)
     */
    public static void main(String[] args) {
        // Swing 테마 설정 (시스템 기본 테마 사용)
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("룩앤필 설정 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }

        // GUI 실행 - EDT(Event Dispatch Thread)에서 안전하게 실행
        SwingUtilities.invokeLater(() -> {
            GlobalBridgeProgram program = new GlobalBridgeProgram();
            program.setVisible(true);
        });
    }
}