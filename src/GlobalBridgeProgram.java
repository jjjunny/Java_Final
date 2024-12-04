import java.io.*;
import java.util.*;

/**
 * GlobalBridgeProgram 클래스는 언어 교환 프로그램 참가자를 관리하는 시스템을 구현합니다.
 * 이 클래스는 참가자 정보를 파일에서 읽고 쓰는 기능을 제공합니다.
 *
 * @author Kim Hyeong Jun (gudwns0240@naver.com)
 * @version 1.0
 * @since 0.0.1
 * {@code@created} 2024-12-05
 * {@code@changelog}
 * <ul>
 *     <li>2024-11-26: 최초 생성</li>
 *     <li>2024-12-05: 프로그램 기획</li>
 *     <li>2024-12-05: 텍스트 파일 입출력 기능 생성</li>
 * </ul>
 */
public class GlobalBridgeProgram {
    /** 모든 참가자를 저장하는 ArrayList */
    private List<User> users = new ArrayList<>();

    /**
     * 프로그램의 메인 메서드입니다.
     * 파일에서 참가자 정보를 로드하고, 새 참가자를 추가한 후, 업데이트된 정보를 파일에 저장합니다.
     *
     * {@code@created} 2024-12-05
     * {@code@lastmodified}
     * <ul>
     *     <li>2024-12-05: 최초 생성</li>
     *     <li>2024-12-05: 메인 메서드 생성</li>
     * </ul>
     * @param args 명령줄 인자 (사용되지 않음)
     */
    public static void main(String[] args) {
        GlobalBridgeProgram program = new GlobalBridgeProgram();
        program.loadUsersFromFile("participants.txt");
        program.addUser(new User("John Doe", "john@example.com", "English", "Korean"));
        program.saveUsersToFile("participants.txt");
    }

    /**
     * 파일에서 참가자 정보를 읽어오는 메서드입니다.
     * {@code@created} 2024-12-05
     * {@code@lastmodified}
     *  <ul>
     *      <li>2024-12-05: 최초 생성</li>
     *      <li>2024-12-05: 메인 메서드 생성</li>
     * </ul>
     *
     * @param filename 읽어올 파일의 이름
     */
    public void loadUsersFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    User user = new User(data[0], data[1], data[2], data[3]);
                    users.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 참가자 정보를 파일에 저장하는 메서드입니다.
     * {@code@created} 2024-12-05
     * {@code@lastmodified}
     *<ul>
     *      <li>2024-12-05: 최초 생성</li>
     *      <li>2024-12-05: 메인 메서드 생성</li>
     * </ul>
     *
     * @param filename 저장할 파일의 이름
     */
    public void saveUsersToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (User user : users) {
                writer.write(user.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 새로운 참가자를 추가하는 메서드입니다.
     * {@code@created} 2024-12-05
     *{@code@lastmodified}
     * <ul>
     *      <li>2024-12-05: 최초 생성</li>
     *      <li>2024-12-05: 메인 메서드 생성</li>
     *</ul>
     *
     * @param user 추가할 User 객체
     */
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * 사용자 정보를 나타내는 내부 클래스입니다.
     * {@code@created} 2024-12-05
     *{@code@lastmodified}
     *<ul>
     *      <li>2024-12-05: 최초 생성</li>
     *      <li>2024-12-05: 메인 메서드 생성</li>
     *</ul>
     */
    static class User {
        private String name;
        private String email;
        private String nativeLanguage;
        private String targetLanguage;

        /**
         * User 객체를 생성하는 생성자입니다.
         * {@code@created} 2024-12-05
         * {@code@lastmodified}
         * <ul>
         *      <li>2024-12-05: 최초 생성</li>
         *      <li>2024-12-05: 메인 메서드 생성</li>
         * </ul>
         *
         * @param name 사용자 이름
         * @param email 사용자 이메일
         * @param nativeLanguage 사용자의 모국어
         * @param targetLanguage 사용자가 학습하고자 하는 목표 언어
         */
        public User(String name, String email, String nativeLanguage, String targetLanguage) {
            this.name = name;
            this.email = email;
            this.nativeLanguage = nativeLanguage;
            this.targetLanguage = targetLanguage;
        }

        /**
         * User 객체를 문자열로 변환합니다.
         * {@code@created} 2024-12-05
         *{@code@lastmodified}
         * <ul>
         *     <li>2024-12-05: 최초 생성</li>
         *     <li>2024-12-05: 메인 메서드 생성</li>
         * </ul>
         *
         * @return 사용자 정보를 담은 문자열
         */
        @Override
        public String toString() {
            return name + "," + email + "," + nativeLanguage + "," + targetLanguage;
        }
    }
}