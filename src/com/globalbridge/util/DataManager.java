package com.globalbridge.util;

import java.io.*;
import java.util.*;

public class DataManager {
    private static final String DATA_FILE = "globalbridge_data.ser";

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