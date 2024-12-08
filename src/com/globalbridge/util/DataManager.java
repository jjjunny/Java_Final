package com.globalbridge.util;

import java.io.*;
import java.util.*;

/**
 * @author YourName
 * @version 1.0
 * @since 2024-12-09
 *
 * 글로벌 브릿지 프로그램의 데이터 저장 및 로드를 관리하는 클래스입니다.
 *
 * <p>
 * 이 클래스는 프로그램의 모든 데이터(참가자, 매칭, 활동 정보)를
 * 파일 시스템에 저장하고 불러오는 기능을 제공합니다.
 * 직렬화(Serialization)를 사용하여 객체를 저장하고 복원합니다.
 * </p>
 *
 * <p>
 * 저장되는 데이터:
 * <ul>
 *   <li>참가자 목록 (ArrayList&lt;Participant&gt;)</li>
 *   <li>매칭 정보 (HashMap&lt;String, Pair&gt;)</li>
 *   <li>활동 기록 (HashMap&lt;String, ArrayList&lt;Activity&gt;&gt;)</li>
 * </ul>
 * </p>
 *
 * @see java.io.Serializable
 */
public class DataManager {
    /** 데이터 저장 파일의 경로 */
    private static final String DATA_FILE = "globalbridge_data.ser";

    /**
     * 프로그램의 모든 데이터를 파일에 저장합니다.
     *
     * <p>
     * ObjectOutputStream을 사용하여 세 가지 주요 데이터 구조를
     * 직렬화하여 파일에 저장합니다. try-with-resources를 사용하여
     * 스트림을 자동으로 닫습니다.
     * </p>
     *
     * @param participants 참가자 목록
     * @param matches 멘토-멘티 매칭 정보
     * @param activities 활동 기록
     *
     * @throws IOException 파일 저장 중 오류 발생 시
     */
    public void saveData(ArrayList<?> participants,
                         HashMap<?, ?> matches,
                         HashMap<?, ?> activities) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(DATA_FILE))) {
            oos.writeObject(participants);
            oos.writeObject(matches);
            oos.writeObject(activities);
        } catch (IOException e) {
            System.err.println("데이터 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 저장된 데이터를 파일에서 불러옵니다.
     *
     * <p>
     * ObjectInputStream을 사용하여 저장된 객체들을 역직렬화하여
     * 메모리에 복원합니다. 파일이 없거나 읽기 오류 발생 시
     * 적절한 오류 메시지를 출력합니다.
     * </p>
     *
     * @return Object 배열 [참가자목록, 매칭정보, 활동기록]
     *         파일이 없거나 오류 발생 시 null 반환
     *
     * <p>
     * 반환되는 배열의 구조:
     * <ul>
     *   <li>data[0]: ArrayList&lt;Participant&gt; - 참가자 목록</li>
     *   <li>data[1]: HashMap&lt;String, Pair&gt; - 매칭 정보</li>
     *   <li>data[2]: HashMap&lt;String, ArrayList&lt;Activity&gt;&gt; - 활동 기록</li>
     * </ul>
     * </p>
     */
    public Object[] loadData() {
        Object[] data = new Object[3];
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(DATA_FILE))) {
            data[0] = ois.readObject();
            data[1] = ois.readObject();
            data[2] = ois.readObject();
            return data;
        } catch (FileNotFoundException e) {
            System.out.println("저장된 데이터가 없습니다.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("데이터 로드 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}