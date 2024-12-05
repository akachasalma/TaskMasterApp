/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.io.File;
import java.io.IOException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author akach
 */
public class SoundPlayer {
   public static void playSound(String filePath) {
       
            File soundFile = new File(filePath);
            
            
            Media media = new Media(soundFile.toURI().toString());
            MediaPlayer MP = new MediaPlayer(media);
            MP.play();
      
    } 
}
