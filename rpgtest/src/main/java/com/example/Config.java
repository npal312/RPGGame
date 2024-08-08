package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {

    GamePanel gp;

    public Config(GamePanel gp){
        this.gp = gp;
    }

    public void saveConfig(){

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("config.txt"));
            
            //Full screen
            if (gp.fullScreenOn == true){
                bw.write("On");
            }
            if (gp.fullScreenOn == false){
                bw.write("Off");
            }
            bw.newLine();

            //Music volume
            bw.write(String.valueOf(gp.music.volumeScale));
            bw.newLine();

            //SE volume
            bw.write(String.valueOf(gp.soundEffect.volumeScale));
            bw.newLine();

            bw.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void loadConfig(){

        try {
            BufferedReader br = new BufferedReader(new FileReader("config.txt"));
        
            String s = br.readLine();

            //Full screen
            if (s.equals("On")){
                gp.fullScreenOn = true;
            }
            if (s.equals("Off")){
                gp.fullScreenOn = false;
            }

            //Music volume
            s = br.readLine();
            gp.music.volumeScale = Integer.parseInt(s);

            //SE volume
            s = br.readLine();
            gp.soundEffect.volumeScale = Integer.parseInt(s);

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e) {
            System.out.println("Error: cannot read config. Setting default values");
            gp.fullScreenOn = false;
            gp.music.volumeScale = 3;
            gp.soundEffect.volumeScale = 3;
            e.printStackTrace();
        }

    }

}
