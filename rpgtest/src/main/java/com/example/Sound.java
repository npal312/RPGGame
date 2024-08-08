package com.example;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {

    Clip clip;
    URL soundURL[] = new URL[30];
    FloatControl fc;
    int volumeScale = 3;
    float volume;
    long currentPosition = 0;

    public Sound(){
        //background music
        soundURL[0] = getClass().getResource("/sound/transient-love.wav");
        //pickup sound
        soundURL[1] = getClass().getResource("/sound/minecraftoof.wav");
        //soundURL[1] = getClass().getResource("/sound/LA.wav");
        //powerup sound
        soundURL[2] = getClass().getResource("/sound/Sound.wav");
        //door unlock sound
        soundURL[3] = getClass().getResource("/sound/aaah.wav");
        //fanfare
        soundURL[4] = getClass().getResource("/sound/yippee.wav");
        //hitmonster
        soundURL[5] = getClass().getResource("/sound/minecraftoof.wav");
        //receivedamage
        soundURL[6] = getClass().getResource("/sound/vineboom.wav");
        //swingweapon
        soundURL[7] = getClass().getResource("/sound/berserkclang.wav");
        //levelup (same as fanfare but done twice to make easier to follow)
        soundURL[8] = getClass().getResource("/sound/yippee.wav");
        //cursormove
        soundURL[9] = getClass().getResource("/sound/ff7-cursor-move.wav");
        //slimeball
        soundURL[10] = getClass().getResource("/sound/mc-slime.wav");
        //break wall
        soundURL[11] = getClass().getResource("/sound/mc-stone-break.wav");
        //gameover sound
        soundURL[12] = getClass().getResource("/sound/mc-zombie-death.wav");
        //house enter sound? (I'll make better later)
        soundURL[13] = getClass().getResource("/sound/mc-slime.wav");
    }

    public void setFile(int i){
        
        try{

            //format for opening audio file in Java (MUST BE 16 bit and .wav format)
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            //change volume line
            fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            checkVolume();

        } catch (Exception e){
            System.out.println("uh oh.");
        }

    }

    public void play(){

        clip.start();

    }

    public void pause(){

        currentPosition = clip.getMicrosecondPosition();
        System.out.println("Real: " + clip.getMicrosecondPosition());
        System.out.println("Fake: " + currentPosition);
        clip.stop();

    }

    public void resume(){

        clip.setMicrosecondPosition(currentPosition);
        clip.start();

    }

    public void loop(){

        clip.loop(Clip.LOOP_CONTINUOUSLY);

    }

    public void stop(){

        clip.stop();

    }

    public void checkVolume(){

        float[] volumes = {-80f, -20f, -12f, -5f, 1f, 6f};
        //takes -80f to 6f vals (above that won't let it lower volume)
        //-30's already pretty low so it's near a max volume decrease
        
        /*switch(volumeScale){
        case 0:
            volume = -80f;
            break;
        case 1:
            volume = -20f;
            break;
        case 2:
            volume = -12f;
            break;
        case 3:
            volume = -5f;
            break;
        case 4:
            volume = 1f;
            break;
        case 5:
            volume = 6f;
            break;
        }
        fc.setValue(volume);*/

        fc.setValue(volumes[volumeScale]);

    }

}
