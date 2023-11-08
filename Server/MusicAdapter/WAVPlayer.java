package Server.MusicAdapter;
import java.io.File;
import javax.sound.sampled.*;
import java.io.IOException;
public class WAVPlayer {
    private Clip clip;

    void playAudio(String audioFile) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        File soundFile = new File(audioFile);
        if (soundFile.exists()) {
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);

        clip = AudioSystem.getClip();
        clip.open(audioIn);
        clip.start();
        }else {
            System.out.println("File not found: " + audioFile);
        }
    }

    void stopAudio() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}
