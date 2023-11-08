package Server.MusicAdapter;
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
            wavPlayer.playAudio(musicFile);
            Timer timer = new Timer(4000, e -> wavPlayer.stopAudio());

            timer.setRepeats(false);
            timer.start();

        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            System.out.println("Music failed to start");
        }
    }
}
