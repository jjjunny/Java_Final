import java.io.*;
import java.util.*;

public class GlobalBridgeProgram {
    // 모든 참가자를 저장하는 ArrayList
    private List<User> users = new ArrayList<>();

    public static void main(String[] args) {
        GlobalBridgeProgram program = new GlobalBridgeProgram();
        // 파일에서 기존 참가자 정보를 불러옴
        program.loadUsersFromFile("participants.txt");
        // 새로운 참가자를 추가
        program.addUser(new User("John Doe", "john@example.com", "English", "Korean"));
        // 업데이트된 참가자 목록을 파일에 저장
        program.saveUsersToFile("participants.txt");
    }

    // 파일에서 참가자 정보를 읽어오는 메서드
    public void loadUsersFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader("participants.txt"))) {
            String line;
            // 파일의 각 줄을 읽어 처리
            while ((line = reader.readLine()) != null) {
                // 쉼표로 구분된 데이터를 분리
                String[] data = line.split(",");
                // 데이터가 4개 필드를 모두 가지고 있는지 확인
                if (data.length == 4) {
                    // User 객체 생성 및 리스트에 추가
                    User user = new User(data[0], data[1], data[2], data[3]);
                    users.add(user);
                }
            }
        } catch (IOException e) {
            // 파일 읽기 중 오류 발생 시 스택 트레이스 출력
            e.printStackTrace();
        }
    }

    // 참가자 정보를 파일에 저장하는 메서드
    public void saveUsersToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // 모든 사용자 정보를 순회하며 파일에 기록
            for (User user : users) {
                writer.write(user.toString());
                writer.newLine(); // 각 사용자 정보를 새 줄에 기록
            }
        } catch (IOException e) {
            // 파일 쓰기 중 오류 발생 시 스택 트레이스 출력
            e.printStackTrace();
        }
    }

    // 새로운 참가자를 추가하는 메서드
    public void addUser(User user) {
        users.add(user);
    }

    // 사용자 정보를 나타내는 내부 클래스
    static class User {
        private String name;
        private String email;
        private String nativeLanguage;
        private String targetLanguage;

        // 사용자 객체 생성자
        public User(String name, String email, String nativeLanguage, String targetLanguage) {
            this.name = name;
            this.email = email;
            this.nativeLanguage = nativeLanguage;
            this.targetLanguage = targetLanguage;
        }

        // 사용자 정보를 문자열로 변환 (파일 저장 시 사용)
        @Override
        public String toString() {
            return name + "," + email + "," + nativeLanguage + "," + targetLanguage;
        }
    }
}