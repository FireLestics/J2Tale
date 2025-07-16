package tools;

import javax.microedition.media.*;
import javax.microedition.media.control.*;
import java.io.*;

public class SimpleMIDIPlayer {

    private Player backgroundPlayer = null;
    private Player effectPlayer = null;
    private MIDIControl midiControl = null;

    public boolean midiSupported = false;

    private String basePath = "/";        // Базовый путь к ресурсам

    public SimpleMIDIPlayer() {
        // Проверка поддержки MIDI при создании плеера
        try {
            Player testPlayer = Manager.createPlayer(Manager.MIDI_DEVICE_LOCATOR);
            midiControl = (MIDIControl) testPlayer.getControl("javax.microedition.media.control.MIDIControl");
            if (midiControl != null) {
                midiSupported = true;
                System.out.println("MIDIControl is supported!");
            } else {
                System.out.println("MIDIControl is NOT supported!");
            }
            testPlayer.close(); // Закрываем тестовый плеер
        } catch (Exception e) {
            System.err.println("Error checking MIDI support: " + e.getMessage());
            midiSupported = false;
        }
    }

    // --------------------  MIDI Functionality --------------------

    // Загрузка фоновой музыки (MIDI)
    public void loadBackgroundMusic(String midiFile, int loop) {
        try {
            backgroundPlayer = Manager.createPlayer(getClass().getResourceAsStream(basePath + midiFile), "audio/midi");
            backgroundPlayer.prefetch();
            if (loop == 0) {
                backgroundPlayer.setLoopCount(1);
            } else {
                backgroundPlayer.setLoopCount(loop);
            }
        } catch (Exception e) {
            System.err.println("Error loading background music: " + e.getMessage());
            e.printStackTrace();
            backgroundPlayer = null; // Важно обнулить, если произошла ошибка
        }
    }

    // Воспроизведение фоновой музыки
    public void playBackgroundMusic() {
        try {
            if (backgroundPlayer != null) {
                backgroundPlayer.start();
            }
        } catch (Exception e) {
            System.err.println("Error playing background music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Загрузка звукового эффекта (MIDI)
    public void loadMidiEffect(int channel, int note, int velocity) {
        try {
            effectPlayer = Manager.createPlayer(Manager.MIDI_DEVICE_LOCATOR);
            effectPlayer.prefetch();
            midiControl = (MIDIControl) effectPlayer.getControl("javax.microedition.media.control.MIDIControl");

            if (midiControl != null) {
                midiControl.shortMidiEvent(MIDIControl.NOTE_ON | channel, note, velocity);
                System.out.println("Playing MIDI effect: channel=" + channel + ", note=" + note + ", velocity=" + velocity);
            } else {
                System.err.println("MIDIControl not available for effect!");
            }

        } catch (Exception e) {
            System.err.println("Error loading MIDI effect: " + e.getMessage());
            e.printStackTrace();
            effectPlayer = null; // Важно обнулить, если произошла ошибка
        }
    }

    // Воспроизведение MIDI-эффекта (нота)
    public void playMidiNote(int channel, int note, int velocity, int duration) {
        if (midiControl != null) {
            try {
                midiControl.shortMidiEvent(MIDIControl.NOTE_ON | channel, note, velocity);
                Thread.sleep(duration); // Длительность звучания ноты
                midiControl.shortMidiEvent(MIDIControl.NOTE_ON | channel, note, 0); // Note OFF
            } catch (Exception e) {
                System.err.println("Error playing MIDI note: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("MIDI not supported, or midiControl is null.");
        }
    }

    // -------------------- Utility methods --------------------

    // Остановка всех плееров
    public void stopAll() {
        stopPlayer(backgroundPlayer);
        stopPlayer(effectPlayer);
    }

    // Остановка плеера
    private void stopPlayer(Player player) {
        if (player != null) {
            try {
                player.stop();
                player.setMediaTime(0); // Перемотка в начало
            } catch (Exception e) {
                System.err.println("Ошибка при остановке плеера: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Закрытие всех плееров
    public void closeAll() {
        closePlayer(backgroundPlayer);
        closePlayer(effectPlayer);
    }

    // Закрытие плеера
    private void closePlayer(Player player) {
        if (player != null) {
            try {
                player.close();
            } catch (Exception e) {
                System.err.println("Ошибка при закрытии плеера: " + e.getMessage());
                e.printStackTrace();
            }
            player = null; // Обнуляем ссылку после закрытия
        }
    }
}