package com.globalbridge.model;

import java.io.Serializable;

/**
 * @author Kim Hyeong Jun
 * @version 1.0
 * @since 2024-12-09
 * @description 글로벌 브릿지 프로그램의 참가자 정보를 관리하는 클래스
 *
 * 글로벌 브릿지 프로그램의 참가자 정보를 관리하는 클래스입니다.
 *
 * <p>
 * 이 클래스는 프로그램에 참여하는 학생들의 기본 정보를 저장하고 관리합니다.
 * 참가자는 사용하는 언어(Korean/English)에 따라 멘토 또는 멘티로 분류되며,
 * 각 참가자의 이름, 학번, 전공, 학년 정보를 포함합니다.
 * </p>
 *
 * <p>
 * 멘토/멘티 분류 규칙:
 * <ul>
 *   <li>Korean 언어 사용자: 멘토로 분류</li>
 *   <li>English 언어 사용자: 멘티로 분류</li>
 * </ul>
 * </p>
 *
 * <p>
 * 데이터 유효성 검증:
 * <ul>
 *   <li>이름: 필수 입력</li>
 *   <li>학번: 유일한 식별자로 사용</li>
 *   <li>언어: "Korean" 또는 "English"만 허용</li>
 *   <li>학년: 1~4 사이의 정수만 허용</li>
 * </ul>
 * </p>
 *
 * @see Pair 멘토-멘티 매칭 관리 클래스
 * @see Activity 활동 기록 관리 클래스
 * @serial 직렬화를 위한 구현 포함
 */
public class Participant implements Serializable {
    /**
     * 참가자 이름
     * @serial 직렬화 대상 필드
     */
    private String name;

    /**
     * 학번 - 참가자 식별을 위한 고유 번호
     * @serial 직렬화 대상 필드
     */
    private String studentId;

    /**
     * 전공 - 참가자의 학과/전공
     * @serial 직렬화 대상 필드
     */
    private String major;

    /**
     * 사용 언어 (Korean 또는 English)
     * 멘토/멘티 구분의 기준이 됨
     * @serial 직렬화 대상 필드
     */
    private String language;

    /**
     * 학년 (1~4)
     * @serial 직렬화 대상 필드
     */
    private int grade;

    /**
     * Participant 객체를 생성하여 새로운 참가자를 등록합니다.
     *
     * <p>
     * 참가자의 기본 정보를 입력받아 객체를 초기화합니다.
     * 언어 정보를 바탕으로 멘토/멘티 여부가 자동으로 결정됩니다.
     * </p>
     *
     * <p>
     * 유효성 검증:
     * <ul>
     *   <li>모든 매개변수는 null이 아니어야 함</li>
     *   <li>grade는 1~4 사이의 값이어야 함</li>
     *   <li>language는 "Korean" 또는 "English"여야 함</li>
     * </ul>
     * </p>
     *
     * @param name 참가자 이름 (null 불가)
     * @param studentId 학번 (null 불가)
     * @param major 전공 (null 불가)
     * @param language 사용 언어 ("Korean" 또는 "English")
     * @param grade 학년 (1~4)
     * @throws IllegalArgumentException 매개변수가 유효하지 않은 경우
     */
    public Participant(String name, String studentId, String major,
                       String language, int grade) {
        if (name == null || studentId == null || major == null || language == null) {
            throw new IllegalArgumentException("모든 필드는 null이 될 수 없습니다.");
        }
        if (grade < 1 || grade > 4) {
            throw new IllegalArgumentException("학년은 1~4 사이여야 합니다.");
        }
        if (!language.equalsIgnoreCase("Korean") && !language.equalsIgnoreCase("English")) {
            throw new IllegalArgumentException("언어는 Korean 또는 English여야 합니다.");
        }

        this.name = name;
        this.studentId = studentId;
        this.major = major;
        this.language = language;
        this.grade = grade;
    }

    [이하 동일...]