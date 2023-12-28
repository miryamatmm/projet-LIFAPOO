package VueControleur;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;
import javax.sound.sampled.* ;

public class Musique {
    private Clip clip;
    private FloatControl volumeControl;

    public Musique(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            clip = AudioSystem.getClip();
            clip.open(audioStream);

            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            diminuerVolume();
            diminuerVolume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            addMusicListener(clip);
            clip.start();
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }

    public void ajouterVolume(){
        if (clip != null){
            setVolume(volumeControl.getValue() + 10);
        }
    }

    public void diminuerVolume(){
        if (clip != null){
            setVolume(volumeControl.getValue() - 10);
        }
    }

    public void setVolume(float value) {
        if (volumeControl != null) {
            volumeControl.setValue(value);
        }
    }

    private void addMusicListener(Clip clip) {
        clip.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP && clip.getFramePosition() == clip.getFrameLength()) {
                clip.setFramePosition(0);
            }
        });
    }
}
