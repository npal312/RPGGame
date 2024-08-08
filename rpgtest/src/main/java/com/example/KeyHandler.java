package com.example;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{
    
    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed;
    //DEBUG
    boolean showDebugText = false;
    //true for mana start, false for ball start
    public boolean manaShows = true;
    //true for mute, false for sound
    boolean muteAudio = false;

    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }

    public boolean keyUp(int code){

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP || code == KeyEvent.VK_KP_DOWN){
            return true;
        }
        return false;

    }

    public boolean keyDown(int code){

        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){
            return true;
        }
        return false;

    }

    public boolean keyLeft(int code){

        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT){
            return true;
        }
        return false;

    }

    public boolean keyRight(int code){

        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT){
            return true;
        }
        return false;

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        // won't use
        //throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        //TITLE STATE
        if (gp.gameState == gp.titleState){
            titleState(code);
        }
        //PLAY STATE
        else if (gp.gameState == gp.playState){
            playState(code);
        }
        //PAUSE STATE
        else if (gp.gameState == gp.pauseState){
            pauseState(code);
        }
        //DIALOGUE STATE
        else if (gp.gameState == gp.dialogueState){
            dialogueState(code);
        }
        //CHARACTER STATE
        else if (gp.gameState == gp.characterState){
            characterState(code);
        }
        //OPTIONS STATE
        else if (gp.gameState == gp.optionsState){
            optionsState(code);
        }
        //GAME OVER STATE
        else if (gp.gameState == gp.gameOverState){
            gameOverState(code);
        }
        //TRADE STATE
        else if (gp.gameState == gp.tradeState){
            tradeState(code);
        }
    }


    public void titleState(int code){
        
        if (gp.ui.titleScreenState == 0){

            if (keyUp(code) == true){
                gp.ui.commandNum--;
                gp.playSE(9);
                if (gp.ui.commandNum < 0){
                    gp.ui.commandNum = 2;
                }
            }
            if (keyDown(code) == true){
                gp.ui.commandNum++;
                gp.playSE(9);
                if (gp.ui.commandNum > 2){
                    gp.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER){
                gp.playSE(9);

                if (gp.ui.commandNum == 0){
                    //NEW GAME STUFF
                    gp.ui.titleScreenState = 1;
                    //gp.gameState = gp.playState;
                    //gp.playMusic(0);
                }
                if (gp.ui.commandNum == 1){
                    //LOAD GAME STUFF
                }
                if (gp.ui.commandNum == 2){
                    //QUIT GAME STUFF
                    System.exit(0);
                }

            }

        }

        else if (gp.ui.titleScreenState == 1){

            if (keyUp(code) == true){
                gp.playSE(9);
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0){
                    gp.ui.commandNum = 3;
                }
            }
            if (keyDown(code) == true){
                gp.playSE(9);
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 3){
                    gp.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER){
                gp.playSE(9);

                if (gp.ui.commandNum == 0){
                    //FIGHTER
                    System.out.println("Add Fighter Specific Stuff Here");
                    gp.gameState = gp.playState;
                    gp.playMusic(0);
                }
                if (gp.ui.commandNum == 1){
                    //THIEF
                    System.out.println("Add Thief Specific Stuff Here");
                    gp.gameState = gp.playState;
                    gp.playMusic(0);
                }
                if (gp.ui.commandNum == 2){
                    //SORCERER
                    System.out.println("Add Sorcerer Specific Stuff Here");
                    gp.gameState = gp.playState;
                    gp.playMusic(0);
                }
                if (gp.ui.commandNum == 3){
                    //BACK
                    gp.ui.titleScreenState = 0;
                    gp.ui.commandNum = 0;
                }
                //idk if it should be here or not
                gp.ui.titleScreenState = 0;

            }

        }

    }

    public void playState(int code){

        if (keyUp(code) == true){
            upPressed = true;
        }
        if (keyDown(code) == true){
            downPressed = true;
        }
        if (keyLeft(code) == true){
            leftPressed = true;
        }
        if (keyRight(code) == true){
            rightPressed = true;
        }
        if (code == KeyEvent.VK_C){
            gp.gameState = gp.characterState;
        }
        //speed increase and decrease for play-testing only
        if (code == KeyEvent.VK_SPACE){
            gp.player.speed += 2;
        }
        if (code == KeyEvent.VK_SHIFT){
            gp.player.speed -= 2;
        }
        if (code == KeyEvent.VK_P){
            gp.gameState = gp.pauseState;
        }
        //maybe modify so you don't have to run into NPC to talk
        //just pressing enter near them will do it
        //maybe a collision check in the player class when you press enter
        //(basically seeing if you're 1 tile away or not)
        if (code == KeyEvent.VK_ENTER){
            enterPressed = true;
        }
        if (code == KeyEvent.VK_E){
            shotKeyPressed = true;
        }
        if (code == KeyEvent.VK_ESCAPE){
            gp.gameState = gp.optionsState;
        }
        //bc O looks like a ball
        if (code == KeyEvent.VK_O){
            if (manaShows){
                manaShows = false;
                gp.ui.drawPlayerLife();
            }
            else if (!manaShows){
                manaShows = true;
                gp.ui.drawPlayerLife();
            }
        }

        //DEBUG
        //shows debug screen
        if (code == KeyEvent.VK_T){
            if (showDebugText == false){
                showDebugText = true;
            }
            else if (showDebugText == true){
                showDebugText = false;
            }
        }
        //refreshes map
        if (code == KeyEvent.VK_R){
            switch(gp.currentMap){
                case 0:
                    gp.tileM.loadMap("/maps/world01.txt", 0);
                    break;
                case 1:
                    gp.tileM.loadMap("/maps/interior01.txt", 1);
                    break;
            }
        }
        //mutes game
        if (code == KeyEvent.VK_M){
            if (!muteAudio){
                muteAudio = true;
                gp.stopMusic();
                for (int i = 0; i < 5; i++){
                    gp.stopSE();
                }
            }
            //could find a way to pause/clip the music and start it from that point again
            else if (muteAudio){
                muteAudio = false;
                gp.resumeMusic(0);
            }
        }

    }

    public void pauseState(int code){
        
        if (code == KeyEvent.VK_P){
            gp.gameState = gp.playState;
        }

    }

    public void dialogueState(int code){
        
        if (code == KeyEvent.VK_ENTER){
            gp.gameState = gp.playState;
        }

    }

    public void characterState(int code){
        
        if (code == KeyEvent.VK_C){
            gp.gameState = gp.playState;
        }
        if (code == KeyEvent.VK_ENTER){
            gp.player.selectItem();
        }
        playerInventory(code);

    }

    public void optionsState(int code){

        if (code == KeyEvent.VK_ESCAPE){
            gp.gameState = gp.playState;
            gp.ui.commandNum = 0;
        }
        if (code == KeyEvent.VK_ENTER){
            enterPressed = true;
        }

        int maxCommandNum = 0;
        switch(gp.ui.subState){
            case 0:
                maxCommandNum = 5;
                break;
            case 1:
                maxCommandNum = 0;
                break;
            case 2:
                maxCommandNum = 0;
                break;
            case 3:
                maxCommandNum = 1;
                break;
        }
        if (keyUp(code) == true){
            gp.ui.commandNum--;
            gp.playSE(9);
            if (gp.ui.commandNum < 0){
                gp.ui.commandNum = maxCommandNum;
            }
        }
        if (keyDown(code) == true){
            gp.ui.commandNum++;
            gp.playSE(9);
            if (gp.ui.commandNum > maxCommandNum){
                gp.ui.commandNum = 0;
            }
        }
        if (keyLeft(code) == true){
            if (gp.ui.subState == 0){
                if (gp.ui.commandNum == 1 && gp.music.volumeScale > 0){
                    gp.music.pause();
                    gp.music.volumeScale--;
                    gp.music.checkVolume();
                    gp.playSE(9);
                    gp.music.resume();
                }
                if (gp.ui.commandNum == 2 && gp.soundEffect.volumeScale > 0){
                    gp.soundEffect.volumeScale--;
                    gp.playSE(9);
                }
            }
        }
        if (keyRight(code) == true){
            if (gp.ui.subState == 0){
                if (gp.ui.commandNum == 1 && gp.music.volumeScale < 5){
                    gp.music.pause();
                    gp.music.volumeScale++;
                    //line below is only needed to change volume if something's already playing
                    gp.music.checkVolume();
                    gp.playSE(9);
                    gp.music.resume();
                }
                if (gp.ui.commandNum == 2 && gp.soundEffect.volumeScale < 5){
                    gp.soundEffect.volumeScale++;
                    gp.playSE(9);
                }
            }
        }
        if (code == KeyEvent.VK_ENTER){

            if (gp.ui.commandNum != 1 && gp.ui.commandNum != 2){
                gp.playSE(9);
            }

        }

    }

    public void gameOverState(int code){

        if (keyUp(code) == true){
            gp.ui.commandNum--;
            gp.playSE(9);
            if (gp.ui.commandNum < 0){
                gp.ui.commandNum = 1;
            }
        }
        if (keyDown(code) == true){
            gp.ui.commandNum++;
            gp.playSE(9);
            if (gp.ui.commandNum > 1){
                gp.ui.commandNum = 0;
            }
        }
        if (code == KeyEvent.VK_ENTER){

            if (gp.ui.commandNum == 0){
                gp.gameState = gp.playState;
                gp.retry();
                gp.playMusic(0);
            }
            else if (gp.ui.commandNum == 1){
                gp.ui.commandNum = 0;
                gp.gameState = gp.titleState;
                gp.restart();
            }

        }

    }

    public void tradeState(int code){

        if (code == KeyEvent.VK_ENTER){
            enterPressed = true;
        }
        if (gp.ui.subState == 0){
            if (keyUp(code) == true){
                gp.ui.commandNum--;
                gp.playSE(9);
                if (gp.ui.commandNum < 0){
                    gp.ui.commandNum = 2;
                }
            }
            if (keyDown(code) == true){
                gp.ui.commandNum++;
                gp.playSE(9);
                if (gp.ui.commandNum > 2){
                    gp.ui.commandNum = 0;
                }
            }
        }
        if (gp.ui.subState == 1){
            npcInventory(code);

            if (code == KeyEvent.VK_ESCAPE){
                gp.ui.subState = 0;
            }
        }
        if (gp.ui.subState == 2){
            playerInventory(code);

            if (code == KeyEvent.VK_ESCAPE){
                gp.ui.subState = 0;
            }
        }

    }

    //now moving across inventories is separate from character screen but still used
    //so inventory movement can be used for other parts
    public void playerInventory(int code){
        if (keyUp(code) == true){
            if (gp.ui.playerSlotRow != 0){
                gp.ui.playerSlotRow--;
                gp.playSE(9);
            }
        }
        if (keyDown(code) == true){
            if (gp.ui.playerSlotRow != 3){
                gp.ui.playerSlotRow++;
                gp.playSE(9);
            }
        }
        if (keyLeft(code) == true){
            if (gp.ui.playerSlotCol != 0){
                gp.ui.playerSlotCol--;
                gp.playSE(9);
            }
        }
        if (keyRight(code) == true){
            if (gp.ui.playerSlotCol != 4){
                gp.ui.playerSlotCol++;
                gp.playSE(9);
            }
        }
    }

    public void npcInventory(int code){
        if (keyUp(code) == true){
            if (gp.ui.npcSlotRow != 0){
                gp.ui.npcSlotRow--;
                gp.playSE(9);
            }
        }
        if (keyDown(code) == true){
            if (gp.ui.npcSlotRow != 3){
                gp.ui.npcSlotRow++;
                gp.playSE(9);
            }
        }
        if (keyLeft(code) == true){
            if (gp.ui.npcSlotCol != 0){
                gp.ui.npcSlotCol--;
                gp.playSE(9);
            }
        }
        if (keyRight(code) == true){
            if (gp.ui.npcSlotCol != 4){
                gp.ui.npcSlotCol++;
                gp.playSE(9);
            }
        }
    }




    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();



        if(keyUp(code) == true){
            upPressed = false;
        }
        if(keyDown(code) == true){
            downPressed = false;
        }
        if(keyLeft(code) == true){
            leftPressed = false;
        }
        if(keyRight(code) == true){
            rightPressed = false;
        }
        if (code == KeyEvent.VK_E){
            shotKeyPressed = false;
        }
        
    }
}
