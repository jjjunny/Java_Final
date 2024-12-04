import java.io.*;
import java.util.*;

public class GlobalBridgeProgram {
    private List<User> users = new ArrayList<>(); // 참가자 목록
    private Map<String, List<User>> languageGroups = new HashMap<>(); // 언어 그룹별 참가자

    public static void main(String[] args) {
        GlobalBridgeProgram program = new GlobalBridgeProgram();
        program.loadUsersFromFile("participants.txt");
        program.matchMentorsAndMentees();
        program.saveUsersToFile("participants_updated.txt");
    }

    // 파일에서 참가자 정보를 읽어오는 메서드
    public void loadUsersFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader("participants.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    User user = new User(data[0], data[1], data[2], data[3]);
                    users.add(user);
                    languageGroups.computeIfAbsent(data[2], k -> new ArrayList<>()).add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 멘토와 멘티를 매칭하는 메서드
    public void matchMentorsAndMentees() {
        for (String language : languageGroups.keySet()) {
            List<User> group = languageGroups.get(language);
            for (int i = 0; i < group.size(); i += 2) {
                if (i + 1 < group.size()) {
                    User mentor = group.get(i);
                    User mentee = group.get(i + 1);
                    System.out.println("Matched: " + mentor.getName() + " with " + mentee.getName());
                }
            }
        }
    }

    // 참가자 정보를 파일에 저장하는 메서드
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

    // 사용자 클래스 정의
    static class User {
        private String name;
        private String email;
        private String nativeLanguage;
        private String targetLanguage;

        public User(String name, String email, String nativeLanguage, String targetLanguage) {
            this.name = name;
            this.email = email;
            this.nativeLanguage = nativeLanguage;
            this.targetLanguage = targetLanguage;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name + "," + email + "," + nativeLanguage + "," + targetLanguage;
        }
    }
}