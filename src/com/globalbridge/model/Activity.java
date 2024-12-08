package com.globalbridge.model;

import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * @author Kim Hyeong Jun
 * @version 1.0
 * @since 2024-12-09
 *
 * 글로벌 브릿지 프로그램의 멘토-멘티 활동 정보를 관리하는 클래스입니다.
 *
 * <p>
 * 이 클래스는 멘토-멘티 간의 만남, 학습, 문화 교류 등의 활동 정보를 저장하고 관리합니다.
 * 각 활동에 대한 날짜, 내용, 장소 정보와 함께 활동 완료 여부를 추적합니다.
 * Serializable을 구현하여 활동 데이터의 영구 저장을 지원합니다.
 * </p>
 *
 * @see Pair
 * @see Participant
 */
public class Activity implements Serializable {
    /** 활동이 진행된 날짜와 시간 */
    private Date date;

    /** 활동 내용에 대한 설명 */
    private String content;

    /** 활동이 진행된 장소 */
    private String location;

    /** 활동 완료 여부를 나타내는 플래그 */
    private boolean isCompleted;

    /**
     * Activity 객체를 생성하여 새로운 활동을 기록합니다.
     *
     * <p>
     * 생성 시점에는 활동이 완료되지 않은 것으로 초기화됩니다.
     * 활동 완료 상태는 {@link #setCompleted(boolean)} 메소드를 통해
     * 나중에 변경할 수 있습니다.
     * </p>
     *
     * @param date 활동 날짜와 시간
     * @param content 활동 내용
     * @param location 활동 장소
     */
    public Activity(Date date, String content, String location) {
        this.date = date;
        this.content = content;
        this.location = location;
        this.isCompleted = false;
    }

    /**
     * 활동 날짜와 시간을 반환합니다.
     *
     * @return 활동이 진행된 Date 객체
     */
    public Date getDate() { return date; }

    /**
     * 활동 내용을 반환합니다.
     *
     * @return 활동 내용 문자열
     */
    public String getContent() { return content; }

    /**
     * 활동 장소를 반환합니다.
     *
     * @return 활동 장소 문자열
     */
    public String getLocation() { return location; }

    /**
     * 활동 완료 여부를 반환합니다.
     *
     * @return 활동이 완료되었으면 true, 진행 중이면 false
     */
    public boolean isCompleted() { return isCompleted; }

    /**
     * 활동의 완료 상태를 설정합니다.
     *
     * @param completed 설정할 완료 상태 (완료: true, 진행 중: false)
     */
    public void setCompleted(boolean completed) { isCompleted = completed; }

    /**
     * 활동 정보를 문자열로 변환합니다.
     *
     * <p>
     * 반환되는 문자열의 형식:
     * "yyyy-MM-dd HH:mm | 활동내용 @ 활동장소 [완료상태]"
     * </p>
     *
     * <p>
     * 예시:
     * "2024-12-09 14:30 | 영어 회화 학습 @ 중앙도서관 [진행중]"
     * </p>
     *
     * @return 활동 정보를 포맷팅한 문자열
     */
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return String.format("%s | %s @ %s [%s]",
                sdf.format(date),
                content,
                location,
                isCompleted ? "완료" : "진행중");
    }
}