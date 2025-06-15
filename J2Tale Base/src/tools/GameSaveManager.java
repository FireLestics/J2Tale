package tools;

import javax.microedition.rms.*;
import java.io.*;

public class GameSaveManager {

    private static final String RECORD_STORE_NAME = "J2TaleBase";
    private RecordStore recordStore = null;

    public GameSaveManager() {
        try {
            recordStore = RecordStore.openRecordStore(RECORD_STORE_NAME, true);
        } catch (RecordStoreException e) {
            System.out.println("Error opening record store: " + e.getMessage());
        }
    }

    public void saveInt(String key, int value) {
        saveData(key, String.valueOf(value), "int");
    }

    public int loadInt(String key, int defaultValue) {
        String value = loadData(key, "int");
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.println("Error parsing int: " + e.getMessage());
            }
        }
        return defaultValue;
    }

    public void saveString(String key, String value) {
        saveData(key, value, "string");
    }

    public String loadString(String key, String defaultValue) {
        String value = loadData(key, "string");
        return (value != null) ? value : defaultValue;
    }

    public void saveBoolean(String key, boolean value) {
        saveData(key, value ? "true" : "false", "boolean");
    }

    public boolean loadBoolean(String key, boolean defaultValue) {
        String value = loadData(key, "boolean");
        if (value != null) {
            return value.equals("true");
        }
        return defaultValue;
    }

    private void saveData(String key, String value, String type) {
        if (recordStore == null) {
            System.out.println("Record store is not initialized.");
            return;
        }

        try {
            String data = key + ":" + type + ":" + value;
            byte[] bytes = data.getBytes("UTF-8");

            int recordId = findRecordId(key);

            if (recordId != -1) {
                recordStore.setRecord(recordId, bytes, 0, bytes.length);
            } else {
                recordStore.addRecord(bytes, 0, bytes.length);
            }
        } catch (RecordStoreException e) {
            System.out.println("Error saving data: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.out.println("Error encoding data: " + e.getMessage());
        }
    }

    private String loadData(String key, String expectedType) {
        if (recordStore == null) {
            System.out.println("Record store is not initialized.");
            return null;
        }

        try {
            int recordId = findRecordId(key);

            if (recordId != -1) {
                byte[] data = recordStore.getRecord(recordId);
                String record = new String(data, "UTF-8");

                String[] parts = split(record, ":");
                if (parts.length == 3 && parts[0].equals(key) && parts[1].equals(expectedType)) {
                    return parts[2];
                } else {
                    System.out.println("Data corrupted or wrong type.");
                    return null;
                }
            } else {
                return null;
            }
        } catch (RecordStoreException e) {
            System.out.println("Error loading data: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.out.println("Error decoding data: " + e.getMessage());
        }
        return null;
    }


    private int findRecordId(String key) throws RecordStoreException {
        RecordEnumeration recordEnum = recordStore.enumerateRecords(null, null, false);
        while (recordEnum.hasNextElement()) {
            int recordId = recordEnum.nextRecordId();
            byte[] data = recordStore.getRecord(recordId);
            try {
                String record = new String(data, "UTF-8");
                String[] parts = split(record, ":");

                if (parts.length > 0 && parts[0].equals(key)) {
                    return recordId;
                }
            } catch (UnsupportedEncodingException e) {
                System.out.println("Error decoding data: " + e.getMessage());
            }
        }
        return -1;
    }


    public void close() {
        try {
            if (recordStore != null) {
                recordStore.closeRecordStore();
                recordStore = null;
            }
        } catch (RecordStoreException e) {
            System.out.println("Error closing record store: " + e.getMessage());
        }
    }

    public void delete() {
        try {
            if(RecordStore.listRecordStores() != null){
                RecordStore.deleteRecordStore(RECORD_STORE_NAME);
            }
        } catch (RecordStoreException e) {
            System.out.println("Error deleting record store: " + e.getMessage());
        }
    }

    private String[] split(String str, String delimiter) {
        java.util.Vector vector = new java.util.Vector();
        int index = 0;
        int prevIndex = 0;

        while ((index = str.indexOf(delimiter, prevIndex)) != -1) {
            vector.addElement(str.substring(prevIndex, index));
            prevIndex = index + delimiter.length();
        }

        vector.addElement(str.substring(prevIndex));

        String[] result = new String[vector.size()];
        vector.copyInto(result);

        return result;
    }
}