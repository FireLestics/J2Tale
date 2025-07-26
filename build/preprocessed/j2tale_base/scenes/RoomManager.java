package j2tale_base.scenes;

import java.util.Hashtable;

public class RoomManager {
    private static Hashtable rooms = new Hashtable(); // ключ — id комнаты

    public static void registerRoom(int id, RoomBase room) {
        rooms.put(String.valueOf(id), room);
    }

    public static RoomBase getRoom(int id) {
        return (RoomBase) rooms.get(String.valueOf(id));
    }

    public static void clear() {
        rooms.clear();
    }
}
