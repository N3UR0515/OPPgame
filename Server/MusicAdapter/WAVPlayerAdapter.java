package Server.MusicAdapter;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import javax.swing.*;

public class WAVPlayerAdapter implements MusicPlayer{
    private WAVPlayer wavPlayer;
    public WAVPlayerAdapter(WAVPlayer mp3Player){
        this.wavPlayer = mp3Player;
    }
    @Override
    public void playIntroMusic(String musicFile) {
        try {
            File soundFile = new File(musicFile);
            if (soundFile.exists()) {
                wavPlayer.playAudio(soundFile);
                Timer timer = new Timer(10000, e -> wavPlayer.stopAudio());

                timer.setRepeats(false);
                timer.start();
            }else {
            System.out.println("File not found: " + musicFile);
        }
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            System.out.println("Music failed to start");
        }
    }
}
