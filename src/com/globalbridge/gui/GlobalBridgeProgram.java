package com.globalbridge.gui;

import com.globalbridge.model.*;
import com.globalbridge.util.DataManager;
import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * @author Kim Hyeong Jun
 * @version 1.0
 * @since 2024-12-09
 *
 * @version 1.1
 * @since 2024-12-23
 *
 *
 * 글로벌 브릿지 프로그램의 메인 GUI 프레임 클래스.
 *
 * <p>
 * 이 클래스는 글로벌 브릿지 프로그램의 주요 기능인 참가자 등록, 멘토-멘티 매칭,
 * 활동 관리를 위한 사용자 인터페이스를 제공합니다. JFrame을 상속받아 구현되었으며,
 * 탭 형식의 인터페이스를 통해 각 기능에 접근할 수 있습니다.
 * </p>
 *
 * <p>
 * 주요 기능:
 * <ul>
 *   <li>참가자 등록</li>
 *   <li>멘토-멘티 매칭</li>
 *   <li>활동 관리</li>
 *   <li>데이터 저장 및 로드</li>
 * </ul>
 * </p>
 *
 *
 * @see RegistrationPanel
 * @see MatchingPanel
 * @see ActivityPanel
 */
public class GlobalBridgeProgram extends JFrame {

    /**
     * 등록된 모든 참가자들의 목록을 저장하는 ArrayList.
     */
    private ArrayList<Participant> participants;

    /**
     * 멘토-멘티 매칭 정보를 저장하는 HashMap.
     *
     * <p>키는 "멘토학번-멘티학번" 형식의 문자열이며, 값은 {@link Pair} 객체입니다.</p>
     */
    private HashMap<String, Pair> matches;

    /**
     * 각 매칭 쌍의 활동 기록을 저장하는 HashMap.
     *
     * <p>키는 매칭 ID("멘토학번-멘티학번" 형식)이며, 값은 {@link Activity} 객체 리스트입니다.</p>
     */
    private HashMap<String, ArrayList<Activity>> activities;

    /**
     * 데이터 저장 및 로드를 담당하는 매니저.
     */
    private DataManager dataManager;

    /**
     * 참가자 등록을 위한 패널.
     */
    private RegistrationPanel registrationPanel;

    /**
     * 멘토-멘티 매칭을 위한 패널.
     */
    private MatchingPanel matchingPanel;

    /**
     * 활동 관리를 위한 패널.
     */
    private ActivityPanel activityPanel;

    /**
     * GlobalBridgeProgram의 메인 프레임을 생성하고 초기화합니다.
     *
     * <p>
     * 생성자에서는 다음과 같은 작업을 수행합니다:
     * <ul>
     *   <li>데이터 구조 초기화</li>
     *   <li>프레임 기본 설정(제목, 크기, 위치 등)</li>
     *   <li>UI 컴포넌트 초기화</li>
     *   <li>저장된 데이터 로드</li>
     * </ul>
     * </p>
     */
    public GlobalBridgeProgram() {
        participants = new ArrayList<>();
        matches = new HashMap<>();
        activities = new HashMap<>();
        dataManager = new DataManager();

        setTitle("글로벌 브릿지 - 함께 성장하는 캠퍼스 문화 교류");
        setSize(900, 700); // UI 크기 조정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 화면 중앙에 배치

        initializeUI();
        loadData();
    }

    /**
     * UI 컴포넌트들을 초기화하고 배치합니다.
     *
     * <p>
     * 탭 패널(JTabbedPane)을 생성하고 각각의 기능별 패널(등록, 매칭, 활동 관리)을
     * 탭으로 추가합니다. 각 탭에는 아이콘과 제목이 설정됩니다.
     * </p>
     */
    private void initializeUI() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // 각 기능별 패널 초기화
        registrationPanel = new RegistrationPanel(this);
        matchingPanel = new MatchingPanel(this);
        activityPanel = new ActivityPanel(this);

        // 탭 추가 (아이콘 포함)
        tabbedPane.addTab("참가자 등록", new ImageIcon("icons/register.png"), registrationPanel);
        tabbedPane.addTab("멘토-멘티 매칭", new ImageIcon("icons/matching.png"), matchingPanel);
        tabbedPane.addTab("활동 관리", new ImageIcon("icons/activity.png"), activityPanel);

        // 탭 스타일 설정
        tabbedPane.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        add(tabbedPane);
    }

    /**
     * 새로운 참가자를 시스템에 등록합니다.
     *
     * <p>
     * 참가자를 목록에 추가하고, 매칭 패널의 리스트를 업데이트한 후,
     * 변경된 데이터를 저장합니다.
     * </p>
     *
     * @param participant 등록할 참가자 객체
     */
    public void addParticipant(Participant participant) {
        participants.add(participant);
        matchingPanel.updateLists();
        dataManager.saveData(participants, matches, activities);
    }

    /**
     * 새로운 멘토-멘티 매칭을 생성합니다.
     *
     * <p>
     * 멘토와 멘티의 학번을 조합하여 고유 ID를 생성하고,
     * 매칭 정보를 저장한 후 활동 패널을 업데이트합니다.
     * </p>
     *
     * @param mentor 멘토로 지정될 참가자
     * @param mentee 멘티로 지정될 참가자
     */
    public void createMatch(Participant mentor, Participant mentee) {
        String matchId = mentor.getStudentId() + "-" + mentee.getStudentId();
        matches.put(matchId, new Pair(mentor, mentee));
        activityPanel.updatePairSelector();
        dataManager.saveData(participants, matches, activities);
    }

    /**
     * 매칭된 페어의 새로운 활동을 기록합니다.
     *
     * <p>
     * 해당 페어의 첫 활동인 경우 새로운 활동 목록을 생성하고,
     * 활동을 추가한 후 데이터를 저장합니다.
     * </p>
     *
     * @param pairId 매칭 ID (멘토학번-멘티학번 형식)
     * @param activity 기록할 활동 정보
     */
    public void addActivity(String pairId, Activity activity) {
        if (!activities.containsKey(pairId)) {
            activities.put(pairId, new ArrayList<>());
        }
        activities.get(pairId).add(activity);
        dataManager.saveData(participants, matches, activities);
    }

    /**
     * 저장된 데이터를 로드하고 UI를 업데이트합니다.
     *
     * <p>
     * 데이터 매니저로부터 저장된 데이터를 불러와 각 컬렉션에 저장하고,
     * 관련된 UI 컴포넌트들을 업데이트합니다.
     *</P>.
     */

     private void loadData() {
        Object[] data = dataManager.loadData();
        if (data != null) {
            participants = (ArrayList<Participant>) data[0];
            matches = (HashMap<String, Pair>) data[1];
            activities = (HashMap<String, ArrayList<Activity>>) data[2];

            matchingPanel.updateLists();
            activityPanel.updatePairSelector();
        }
    }

    /**
     * 등록된 모든 참가자 목록을 반환합니다.
     * @return 참가자 목록을 담은 ArrayList
     */
    public ArrayList<Participant> getParticipants() { return participants; }

    /**
     * 모든 멘토-멘티 매칭 정보를 반환합니다.
     * @return 매칭 정보를 담은 HashMap
     */
    public HashMap<String, Pair> getMatches() { return matches; }

    /**
     * 모든 활동 기록을 반환합니다.
     * @return 활동 기록을 담은 HashMap
     */
    public HashMap<String, ArrayList<Activity>> getActivities() { return activities; }
}