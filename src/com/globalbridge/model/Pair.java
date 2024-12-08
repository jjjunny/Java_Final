package com.globalbridge.model;

import java.io.Serializable;

/**
 * @author Kim Hyeong Jun
 * @version 1.0
 * @since 2024-12-09
 *
 * 글로벌 브릿지 프로그램의 멘토-멘티 매칭 정보를 관리하는 클래스입니다.
 *
 * <p>
 * 이 클래스는 멘토(Korean 언어 사용자)와 멘티(English 언어 사용자) 간의
 * 매칭 관계를 나타냅니다. 언어 기준에 따라 멘토와 멘티가 올바르게 매칭되었는지
 * 검증하는 기능을 포함합니다.
 * </p>
 *
 * <p>
 * 매칭 규칙:
 * <ul>
 *   <li>멘토는 반드시 Korean 언어 사용자여야 함</li>
 *   <li>멘티는 반드시 English 언어 사용자여야 함</li>
 * </ul>
 * </p>
 *
 * @see Participant
 * @see Activity
 */
public class Pair implements Serializable {
    /** 멘토로 지정된 참가자 (Korean 언어 사용자) */
    private Participant mentor;

    /** 멘티로 지정된 참가자 (English 언어 사용자) */
    private Participant mentee;

    /**
     * Pair 객체를 생성하여 멘토-멘티 매칭을 수행합니다.
     *
     * <p>
     * 생성자는 매개변수로 전달된 참가자들의 언어 정보를 검증하여
     * 올바른 멘토-멘티 관계가 성립되는지 확인합니다.
     * </p>
     *
     * @param mentor 멘토로 지정할 참가자
     * @param mentee 멘티로 지정할 참가자
     * @throws IllegalArgumentException 다음의 경우에 발생:
     *         <ul>
     *           <li>멘토가 Korean 언어 사용자가 아닌 경우</li>
     *           <li>멘티가 English 언어 사용자가 아닌 경우</li>
     *         </ul>
     */
    public Pair(Participant mentor, Participant mentee) {
        if (!mentor.isMentor()) {
            throw new IllegalArgumentException("멘토는 Korean 언어 사용자여야 합니다.");
        }
        if (mentee.isMentor()) {
            throw new IllegalArgumentException("멘티는 English 언어 사용자여야 합니다.");
        }

        this.mentor = mentor;
        this.mentee = mentee;
    }

    /**
     * 매칭된 멘토 참가자를 반환합니다.
     *
     * @return 멘토로 지정된 Participant 객체
     */
    public Participant getMentor() { return mentor; }

    /**
     * 매칭된 멘티 참가자를 반환합니다.
     *
     * @return 멘티로 지정된 Participant 객체
     */
    public Participant getMentee() { return mentee; }

    /**
     * 멘토-멘티 매칭 정보를 문자열로 변환합니다.
     *
     * <p>
     * 반환되는 문자열의 형식:
     * "멘토: [멘토이름] (Korean) - 멘티: [멘티이름] (English)"
     * </p>
     *
     * <p>
     * 예시:
     * "멘토: 홍길동 (Korean) - 멘티: John Doe (English)"
     * </p>
     *
     * @return 매칭 정보를 포맷팅한 문자열
     */
    @Override
    public String toString() {
        return String.format("멘토: %s (Korean) - 멘티: %s (English)",
                mentor.getName(), mentee.getName());
    }
}