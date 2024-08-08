package com.example;

import javax.swing.JFrame;
//RUN THIS FILE TO START GRAPHICS

public class Main {

    public static JFrame window;
    public static void main(String[] args){
        System.setProperty("sun.java2d.d3d", "false");
        window = new JFrame();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2D Test");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        gamePanel.config.loadConfig();
        if (gamePanel.fullScreenOn == true){
            window.setUndecorated(true);
        }

        window.pack();

        //puts window in middle of screen
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        
        gamePanel.setupGame();
        gamePanel.startGameThread();;
    }
    
}