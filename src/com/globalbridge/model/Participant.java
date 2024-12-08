package com.globalbridge.model;

import java.io.Serializable;

/**
 * @author Kim Hyeong Jun
 * @version 1.0
 * @since 2024-12-09
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
 * @see Pair
 * @see Activity
 */
public class Participant implements Serializable {
    /** 참가자 이름 */
    private String name;

    /** 학번 */
    private String studentId;

    /** 전공 */
    private String major;

    /** 사용 언어 (Korean 또는 English) */
    private String language;

    /** 학년 (1~4) */
    private int grade;

    /**
     * Participant 객체를 생성하여 새로운 참가자를 등록합니다.
     *
     * <p>
     * 참가자의 기본 정보를 입력받아 객체를 초기화합니다.
     * 언어 정보를 바탕으로 멘토/멘티 여부가 자동으로 결정됩니다.
     * </p>
     *
     * @param name 참가자 이름
     * @param studentId 학번
     * @param major 전공
     * @param language 사용 언어 ("Korean" 또는 "English")
     * @param grade 학년 (1~4)
     */
    public Participant(String name, String studentId, String major,
                       String language, int grade) {
        this.name = name;
        this.studentId = studentId;
        this.major = major;
        this.language = language;
        this.grade = grade;
    }

    /**
     * 참가자가 멘토인지 여부를 확인합니다.
     *
     * <p>
     * Korean 언어 사용자는 멘토로, English 언어 사용자는 멘티로 분류됩니다.
     * 언어 비교 시 대소문자를 구분하지 않습니다.
     * </p>
     *
     * @return Korean 언어 사용자(멘토)인 경우 true, 그렇지 않은 경우 false
     */
    public boolean isMentor() {
        return language.equalsIgnoreCase("Korean");
    }

    /**
     * 참가자의 이름을 반환합니다.
     * @return 참가자 이름
     */
    public String getName() { return name; }

    /**
     * 참가자의 학번을 반환합니다.
     * @return 학번
     */
    public String getStudentId() { return studentId; }

    /**
     * 참가자의 전공을 반환합니다.
     * @return 전공
     */
    public String getMajor() { return major; }

    /**
     * 참가자의 사용 언어를 반환합니다.
     * @return 사용 언어 ("Korean" 또는 "English")
     */
    public String getLanguage() { return language; }

    /**
     * 참가자의 학년을 반환합니다.
     * @return 학년 (1~4)
     */
    public int getGrade() { return grade; }

    /**
     * 참가자 정보를 문자열로 변환합니다.
     *
     * <p>
     * 반환되는 문자열의 형식:
     * "[이름] ([학번]) - [언어] - [전공]"
     * </p>
     *
     * <p>
     * 예시:
     * "홍길동 (20240001) - Korean - 컴퓨터공학"
     * "John Doe (20240002) - English - 경영학"
     * </p>
     *
     * @return 참가자 정보를 포맷팅한 문자열
     */
    @Override
    public String toString() {
        return name + " (" + studentId + ") - " + language + " - " + major;
    }
}