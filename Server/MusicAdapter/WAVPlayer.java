package Server.MusicAdapter;
import java.io.File;
import javax.sound.sampled.*;
import java.io.IOException;
public class WAVPlayer {
    private Clip clip;

    void playAudio(File soundFile) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
        clip = AudioSystem.getClip();
        clip.open(audioIn);
        clip.start();

    }

    void stopAudio() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}
