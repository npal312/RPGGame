package com.example;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
//import java.awt.RenderingHints;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

//idk what this is
//import org.omg.CosNaming._BindingIteratorImplBase;

import entity.Entity;
import object.OBJ_Ball;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_Mana;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font maruMonica, firstOrder;
    BufferedImage heart_full, heart_half, heart_blank, mana_full, mana_blank, ammo_full, ammo_blank, coin;
    //Font arial_50, arial_100B;

    public boolean messageOn = false;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public boolean gameFinished = false;
    public String currentDialogue = "";
    //for choosing between title screen options
    public int commandNum = 0;
    //for character creation/substates in title screen
    //0 = first screen, 1 = second screen
    public int titleScreenState = 0;
    //indicating cursor position
    public int playerSlotCol = 0;
    public int playerSlotRow = 0;
    public int npcSlotCol = 0;
    public int npcSlotRow = 0;
    int subState = 0;
    int counter = 0;
    public Entity merchant;

    //COLORS
    public Color outerSlime = new Color(70, 194, 93);
    public Color innerSlime = new Color(147, 230, 155);
    public Color darkOuterSlime = new Color(39, 121, 54);

    



    public UI(GamePanel gp){
        this.gp = gp;

        try {
            InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
            //is = getClass().getResourceAsStream("/font/firstorder.ttf");
            //firstOrder = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        //font name (if supported by computer), font type, font size
        //arial_50 = new Font("Arial", Font.PLAIN, 50);
        //arial_100B = new Font("Arial", Font.BOLD, 100);

        //CREATE HUD OBJECT
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
        Entity crystal = new OBJ_Mana(gp);
        mana_full = crystal.image;
        mana_blank = crystal.image2;
        Entity ammo = new OBJ_Ball(gp);
        ammo_full = ammo.image;
        ammo_blank = ammo.image2;
        Entity bronzeCoin = new OBJ_Coin_Bronze(gp);
        coin = bronzeCoin.down1;

    }

    public void addMessage(String text){

        message.add(text);
        messageCounter.add(0);

    }

    public void draw(Graphics2D g2){

        this.g2 = g2;

        g2.setFont(maruMonica);
        //anti-aliasing for getting rid of jagged lines
        //g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.white);

        //TITLE STATE
        if (gp.gameState == gp.titleState){
            drawTitleScreen();
        }
        //PLAY STATE
        if (gp.gameState == gp.playState){
            drawPlayerLife();
            drawMessage();
        }
        //PAUSE STATE
        if (gp.gameState == gp.pauseState){
            drawPlayerLife();
            drawPauseScreen();
        }
        //DIALOGUE STATE
        if (gp.gameState == gp.dialogueState){
            drawDialogueScreen();
        }
        //CHARACTER STATE
        if (gp.gameState == gp.characterState){
            drawCharacterScreen();
            drawInventory(gp.player, true);
        }
        //OPTIONS STATE
        if (gp.gameState == gp.optionsState){
            drawOptionsScreen();
        }
        //GAME OVER STATE
        if (gp.gameState == gp.gameOverState){
            drawGameOverScreen();
        }
        //TRANSITION STATE
        if (gp.gameState == gp.transitionState){
            drawTransition();
        }
        //TRADE STATE
        if (gp.gameState == gp.tradeState){
            drawTradeScreen();
        }


    }

    public void drawPlayerLife(){

        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        int i = 0;

        //DRAW MAX LIFE (BLANK HEARTS)
        while ( i < gp.player.maxLife / 2){
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += gp.tileSize;
        }

        //RESET
        x = gp.tileSize / 2;
        y = gp.tileSize / 2;
        i = 0;

        //DRAW CURRENT LIFE
        while ( i < gp.player.life){
            g2.drawImage(heart_half, x, y, null);
            i++;
            if (i < gp.player.life){
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += gp.tileSize;
        }

        if (gp.player.projectile.type == gp.player.type_range_mana){
            //RESET
            x = gp.tileSize / 2;
            y = gp.tileSize * 2 - (gp.tileSize / 4);
            i = 0;

            //DRAW MAX MANA
            while (i < gp.player.maxMana){
                g2.drawImage(mana_blank, x, y, null);
                i++;
                x += gp.tileSize * 13 / 16;
            }

            //RESET
            x = gp.tileSize / 2;
            y = gp.tileSize * 2 - (gp.tileSize / 4);
            i = 0;

            //DRAW MANA
            while (i < gp.player.mana){
                g2.drawImage(mana_full, x, y, null);
                i++;
                x += gp.tileSize * 13 / 16;
            }
        }
        else if (gp.player.projectile.type == gp.player.type_range_phys){
            //RESET
            x = gp.tileSize / 2;
            y = gp.tileSize * 2 - (gp.tileSize / 2);
            i = 0;

            //DRAW MAX AMMO
            while (i < gp.player.maxAmmo){
                g2.drawImage(ammo_blank, x, y, null);
                i++;
                x += gp.tileSize * 12 / 16;
            }

            //RESET
            x = gp.tileSize / 2;
            y = gp.tileSize * 2 - (gp.tileSize / 2);
            i = 0;

            //DRAW AMMO
            while (i < gp.player.ammo){
                g2.drawImage(ammo_full, x, y, null);
                i++;
                x += gp.tileSize * 12 / 16;
            }
        }
        

    }

    public void drawMessage(){

        int messageX = gp.tileSize;
        int messageY = gp.tileSize * 4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));

        for (int i = 0; i < message.size(); i++){

            //checks if arraylist is empty (could do other way)
            //but also want to check if message is there so this works
            if (message.get(i) != null){
                g2.setColor(Color.black);
                g2.drawString(message.get(i), messageX + 2, messageY + 2);
                g2.setColor(Color.white);
                g2.drawString(message.get(i), messageX, messageY);

                int counter = messageCounter.get(i) + 1; //messageCounter++
                messageCounter.set(i, counter); //adds updated message num to array
                messageY += 50;

                if (messageCounter.get(i) > 180){
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }

        }

    }

    public void drawTitleScreen(){

        //in menu
        //could do this differently but following directly for now
        if (titleScreenState == 0){
            g2.setColor(new Color(0, 100, 0));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

            //TITLE NAME
            g2.setFont(maruMonica);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
            String text = "Slime Game";
            int x = getXforCenteredText(text);
            int y = gp.tileSize * 3;

            //SHADOW
            g2.setColor(Color.black);
            g2.drawString(text, x + 5, y + 5);
            
            //MAIN COLOR
            g2.setColor(Color.white);
            g2.drawString(text, x, y);

            //SLIME IMAGE
            x = gp.screenWidth / 2 - gp.tileSize;
            y += gp.tileSize / 2;

            g2.drawImage(gp.player.down1, x, y, gp.tileSize * 2, gp.tileSize * 2, null);

            //find a way to make random number affect this without bugging out
            /*if (i <= 95){
                g2.drawImage(gp.player.down1, x, y, gp.tileSize * 2, gp.tileSize * 2, null);
            }
            else if (i <= 100){
                g2.drawImage(gp.player.right2, x, y, gp.tileSize * 2, gp.tileSize * 2, null);
            }*/

            //MENU
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));
            text = "New Game";
            x = getXforCenteredText(text);
            y += gp.tileSize * 3;
            g2.drawString(text, x, y);
            if(commandNum == 0){
                g2.drawString(">", x - gp.tileSize / 2, y);
            }

            text = "Load Game";
            x = getXforCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if(commandNum == 1){
                g2.drawString(">", x - gp.tileSize / 2, y);
            }

            text = "Quit";
            x = getXforCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if(commandNum == 2){
                g2.drawString(">", x - gp.tileSize / 2, y);
            }
        }
        else if (titleScreenState == 1){

            //fill with rectangle of color to overwrite old screen
            //old screen stays on (maybe it gets fixed tho)
            g2.setColor(Color.black);
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

            //CLASS SELECTION SCREEN
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(64F));

            String text = "Select your class!";
            int x = getXforCenteredText(text);
            int y = gp.tileSize * 3;
            g2.drawString(text, x, y);

            g2.setFont(g2.getFont().deriveFont(42F));

            text = "Fighter";
            x = getXforCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 0){
                g2.drawString(">", x - gp.tileSize / 2, y);
            }

            text = "Thief";
            x = getXforCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 1){
                g2.drawString(">", x - gp.tileSize / 2, y);
            }

            text = "Sorcerer";
            x = getXforCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 2){
                g2.drawString(">", x - gp.tileSize / 2, y);
            }

            text = "Back";
            x = getXforCenteredText(text);
            y += gp.tileSize * 2;
            g2.drawString(text, x, y);
            if (commandNum == 3){
                g2.drawString(">", x - gp.tileSize / 2, y);
            }

        }

    }

    public void drawPauseScreen(){

        //for changing font size/attributes without making a new font
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 100F));
        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight / 2;

        g2.drawString(text, x ,y);

    }

    public void drawDialogueScreen(){

        //WINDOW
        //can also just write pixel int instead of equation
        int x = gp.tileSize * 3;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - 2 * x;
        int height = gp.tileSize * 3;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40));
        x += gp.tileSize;
        y += gp.tileSize;

        for (String line: currentDialogue.split("\n")){
            g2.drawString(line, x, y);
            y += 40;
        }

    }

    public void drawCharacterScreen(){

        //CREATE A FRAME
        final int frameX = gp.tileSize * 2;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize * 5;
        final int frameHeight = gp.tileSize * 10 + (gp.tileSize / 2);
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        //TEXT
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(40F));

        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 45; //LOWEST IT CAN BE IS FONT SIZE

        //NAMES
        g2.drawString("Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Life", textX, textY);
        textY += lineHeight;
        g2.drawString("Mana", textX, textY);
        textY += lineHeight;
        g2.drawString("Ammo", textX, textY);
        textY += lineHeight;
        g2.drawString("Strength", textX, textY);
        textY += lineHeight;
        g2.drawString("Dexterity", textX, textY);
        textY += lineHeight;
        g2.drawString("Attack", textX, textY);
        textY += lineHeight;
        g2.drawString("Defense", textX, textY);
        textY += lineHeight;
        g2.drawString("Exp", textX, textY);
        textY += lineHeight;
        g2.drawString("Next Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Coin", textX, textY);
        textY += lineHeight + 20;
        g2.drawString("Weapon", textX, textY);
        textY += lineHeight + 15;
        g2.drawString("Shield", textX, textY);
        textY += lineHeight;

        //VALUES
        int tailX = (frameX + frameWidth) - 30;
        //Reset textY
        textY = frameY + gp.tileSize;
        String value;

        value = String.valueOf(gp.player.level);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.mana + "/" + gp.player.maxMana);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.ammo + "/" + gp.player.maxAmmo);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.strength);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.dexterity);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.attack);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.defense);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.exp);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.nextLevelExp);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.coin);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize + 10, textY - 30, null);
        textY += gp.tileSize;
        g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize + 10, textY - 30, null);

    }

    public void drawInventory(Entity entity, boolean cursor){

        int frameX = 0;
        int frameY = 0;
        int frameWidth = 0;
        int frameHeight = 0;
        int slotCol = 0;
        int slotRow = 0;

        if (entity == gp.player){
            frameX = gp.tileSize * 12;
            frameY = gp.tileSize;
            frameWidth = gp.tileSize * 6 - 10;// + (gp.tileSize / 2) + 7;
            frameHeight = gp.tileSize * 5 - 13;// + (gp.tileSize / 2) + 7;
            slotCol = playerSlotCol;
            slotRow = playerSlotRow;
        }
        else{
            frameX = gp.tileSize * 2;
            frameY = gp.tileSize;
            frameWidth = gp.tileSize * 6 - 10;// + (gp.tileSize / 2) + 7;
            frameHeight = gp.tileSize * 5 - 13;// + (gp.tileSize / 2) + 7;
            slotCol = npcSlotCol;
            slotRow = npcSlotRow;
        }

        //FRAME
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        //I can find a way to automate this and make as many slots
        //as needed for inventory
        //would need to learn scrolling windows first

        //SLOT
        final int slotXstart = frameX + 20;
        final int slotYstart = frameY + 20;
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slotSize = gp.tileSize + 3;

        //DRAW ENTITY'S ITEMS
        for (int i = 0; i < entity.inventory.size(); i++){

            //EQUIPPED ITEM CURSOR
            if (entity.inventory.get(i) == entity.currentWeapon || 
                entity.inventory.get(i) == entity.currentShield){
                g2.setColor(darkOuterSlime);
                g2.fillRoundRect(slotX, slotY, slotSize, slotSize, 7, 7);
            }

            g2.drawImage(entity.inventory.get(i).down1, slotX, slotY, null);

            slotX += slotSize;

            if ( (i + 1) % 5 == 0){
                slotX = slotXstart;
                slotY += slotSize;
            }
            
            //could replace with 19 or max num if causes problems
            if (i >= gp.player.maxInventorySize - 1){
                break;
            }

        }

        //CURSOR
        if (cursor == true){
            int cursorX = slotXstart + (slotSize * slotCol);
            int cursorY = slotYstart + (slotSize * slotRow);
            int cursorWidth = slotSize;
            int cursorHeight = slotSize;

            //DRAW CURSOR
            g2.setColor(innerSlime);
            g2.setStroke(new BasicStroke(4));
            g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 7, 7);

            //DESCRIPTION FRAME
            //could put this and code block below all in that if statement so it only even makes the variables if the size check works
            int dFrameX = frameX;
            int dFrameY = frameY + frameHeight;
            int dFrameWidth = frameWidth;
            int dFrameHeight = gp.tileSize * 3;
            //drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);

            //DRAW DESCRIPTION TEXT
            int textX = dFrameX + 20;
            int textY = dFrameY + gp.tileSize;
            g2.setFont(g2.getFont().deriveFont(40F));

            int itemIndex = getItemIndexOnSlot(slotCol, slotRow);

            if (itemIndex < entity.inventory.size()){

                drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);

                for (String line: entity.inventory.get(itemIndex).description.split("\n")){
                    g2.drawString(line, textX, textY);
                    textY += 50;
                }

            }
        }
    }

    public void drawGameOverScreen(){

        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110F));

        text = "Game Over";
        //Shadow
        g2.setColor(Color.black);
        x = getXforCenteredText(text);
        y = gp.tileSize * 4;
        g2.drawString(text, x, y);
        //Main
        g2.setColor(Color.white);
        x = getXforCenteredText(text);
        y = gp.tileSize * 4;
        g2.drawString(text, x - 4, y - 4);

        //Retry
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 60F));
        text = "Retry";
        x = getXforCenteredText(text);
        y += gp.tileSize * 5;
        g2.drawString(text, x, y);
        if (commandNum == 0){
            g2.drawString(">", x-40, y);
        }

        //Back to title screen
        text = "Quit";
        x = getXforCenteredText(text);
        y += 85;
        g2.drawString(text, x, y);
        if (commandNum == 1){
            g2.drawString(">", x-40, y);
        }

    }

    public void drawOptionsScreen(){

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(40F));

        //SUB WINDOW
        int frameX = gp.tileSize * 6;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 8;
        int frameHeight = gp.tileSize * 10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        switch(subState){
            case 0:
                options_top(frameX, frameY);
                break;
            case 1:
                options_fullScreenNotification(frameX, frameY);
                break;
            case 2:
                options_control(frameX, frameY);
                break;
            case 3:
                options_endGameConfirmation(frameX, frameY);
                break;
        }

        gp.keyH.enterPressed = false;

    }

    public void options_top(int frameX, int frameY){

        int textX;
        int textY;

        //TITLE
        String text = "Options";
        textX = getXforCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        //FULL SCREEN ON/OFF
        textX = frameX + gp.tileSize;
        textY += gp.tileSize * 2;
        g2.drawString("Full Screen", textX, textY);
        if (commandNum == 0){
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed == true){
                if (gp.fullScreenOn == false){
                    gp.fullScreenOn = true;
                }
                else if (gp.fullScreenOn == true){
                    gp.fullScreenOn = false;
                }
                subState = 1;
            }
        }

        //MUSIC
        textY += gp.tileSize;
        g2.drawString("Music", textX, textY);
        if (commandNum == 1){
            g2.drawString(">", textX - 25, textY);
        }

        //SE
        textY += gp.tileSize;
        g2.drawString("Sound Effects", textX, textY);
        if (commandNum == 2){
            g2.drawString(">", textX - 25, textY);
        }

        //CONTROLS
        textY += gp.tileSize;
        g2.drawString("Controls", textX, textY);
        if (commandNum == 3){
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed == true){
                subState = 2;
                commandNum = 0;
            }
        }

        //END GAME
        textY += gp.tileSize;
        g2.drawString("End Game", textX, textY);
        if (commandNum == 4){
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed == true){
                subState = 3;
                commandNum = 0;
            }
        }

        //BACK
        textY += gp.tileSize * 2;
        g2.drawString("Back", textX, textY);
        if (commandNum == 5){
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed == true){
                gp.gameState = gp.playState;
                commandNum = 0;
            }
        }


        //FULL SCREEN CHECK BOX
        textX = frameX + gp.tileSize * 4 + (gp.tileSize / 2);
        textY = frameY + gp.tileSize * 2 + (gp.tileSize / 2);
        g2.setStroke(new BasicStroke(4));
        g2.drawRect(textX, textY, 42, 42);
        if (gp.fullScreenOn == true){
            g2.fillRect(textX, textY, 42, 42);
        }

        //MUSIC VOLUME
        textY += gp.tileSize;
        g2.drawRect(textX, textY, 200, 42); // 200 / 5 = 40
        int volumeWidth = 40 * gp.music.volumeScale;
        g2.fillRect(textX, textY, volumeWidth, 42);

        //SOUND EFFECT VOLUME
        textY += gp.tileSize;
        g2.drawRect(textX, textY, 200, 42); // 200 / 5
        volumeWidth = 40 * gp.soundEffect.volumeScale;
        g2.fillRect(textX, textY, volumeWidth, 42);

        //SAVES CONFIG INFO AT END
        gp.config.saveConfig();

    }

    public void options_fullScreenNotification(int frameX, int frameY){

        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize * 3;

        currentDialogue = "The change will take effect\nafter restarting the game.";

        for (String line: currentDialogue.split("\n")){
            g2.drawString(line, textX, textY);
            textY += 48;
        }

        //BACK
        textY = frameY + gp.tileSize * 9;
        g2.drawString("Back", textX, textY);
        if (commandNum == 0){
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed == true){
                subState = 0;
            }
        }

    }

    public void options_control(int frameX, int frameY){

        int textX;
        int textY;

        //TITLE
        String text = "Controls";
        textX = getXforCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        textX = frameX + gp.tileSize;
        textY += gp.tileSize;
        g2.drawString("Move:", textX, textY);
        textY += gp.tileSize;
        textY += gp.tileSize;
        g2.drawString("Confirm/Attack:", textX, textY);
        textY += gp.tileSize;
        g2.drawString("Shoot/Cast:", textX, textY);
        textY += gp.tileSize;
        g2.drawString("Character Screen:", textX, textY);
        textY += gp.tileSize;
        g2.drawString("Pause:", textX, textY);
        textY += gp.tileSize;
        g2.drawString("Options:", textX, textY);

        textX = frameX + gp.tileSize * 6;
        textY = frameY + gp.tileSize * 2;
        g2.drawString("WASD", textX, textY);
        textX -= gp.tileSize;
        textY += gp.tileSize;
        g2.drawString("OR ARROWS", textX, textY);
        textX +=gp.tileSize;
        textY += gp.tileSize;
        g2.drawString("ENTER", textX, textY);
        textY += gp.tileSize;
        g2.drawString("E", textX, textY);
        textY += gp.tileSize;
        g2.drawString("C", textX, textY);
        textY += gp.tileSize;
        g2.drawString("P", textX, textY);
        textY += gp.tileSize;
        g2.drawString("ESC", textX, textY);

        //BACK
        textX = frameX  + gp.tileSize;
        textY = frameY + gp.tileSize * 9;
        g2.drawString("Back", textX, textY);
        if (commandNum == 0){
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed == true){
                subState = 0;
                commandNum = 3;
            }
        }


    }

    public void options_endGameConfirmation(int frameX, int frameY){

        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize;

        currentDialogue = "Quit the game and\nreturn to the title screen?";

        for (String line: currentDialogue.split("\n")){
            g2.drawString(line, textX, textY);
            textY += 48;
        }

        //YES
        String text = "Yes";
        textX = getXforCenteredText(text);
        textY += gp.tileSize * 5 + (gp.tileSize / 2);
        g2.drawString(text, textX, textY);
        if (commandNum == 0){
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed == true){
                subState = 0;
                commandNum = 0;
                titleScreenState = 0;
                gp.stopMusic();
                gp.gameState = gp.titleState;
            }
        }

        //NO
        text = "No";
        textX = getXforCenteredText(text);
        textY += gp.tileSize;
        g2.drawString(text, textX, textY);
        if (commandNum == 1){
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed == true){
                subState = 0;
                commandNum = 4;
            }
        }

    }

    public void drawTransition(){

        counter++;
        //last val = opacity
        g2.setColor(new Color(0, 0, 0, counter * 5));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        if (counter == 50){
            counter = 0;
            gp.gameState = gp.playState;
            gp.currentMap = gp.eHandler.tempMap;
            gp.player.worldX = gp.tileSize * gp.eHandler.tempCol;
            gp.player.worldY = gp.tileSize * gp.eHandler.tempRow;
            gp.eHandler.previousEventX = gp.player.worldX;
            gp.eHandler.previousEventY = gp.player.worldY;
        }

    }

    public void drawTradeScreen(){

        switch(subState){
            case 0:
                trade_select();
                break;
            case 1:
                trade_buy();
                break;
            case 2:
                trade_sell();
                break;
        }

        gp.keyH.enterPressed = false;

    }

    public void trade_select(){

        drawDialogueScreen();

        //DRAW WINDOW
        int x = gp.tileSize * 15;
        int y = gp.tileSize * 4;
        int width = gp.tileSize * 3;
        int height = (int)(gp.tileSize * 3.5);
        drawSubWindow(x, y, width, height);

        //DRAW TEXTS
        x += gp.tileSize;
        y += gp.tileSize;
        g2.drawString("Buy", x, y);
        if (commandNum == 0){
            g2.drawString(">", x - 25, y);
            if (gp.keyH.enterPressed == true){
                subState = 1;
            }
        }
        y += gp.tileSize;
        g2.drawString("Sell", x, y);
        if (commandNum == 1){
            g2.drawString(">", x - 25, y);
            if (gp.keyH.enterPressed == true){
                subState = 2;
            }
        }
        y += gp.tileSize;
        g2.drawString("Leave", x, y);
        if (commandNum == 2){
            g2.drawString(">", x - 25, y);
            if (gp.keyH.enterPressed == true){
                commandNum = 0;
                gp.gameState = gp.dialogueState;
                currentDialogue = "Bye, come again!";
            }
        }
        y += gp.tileSize;

    }

    public void trade_buy(){

        //DRAW PLAYER INVENTORY
        drawInventory(gp.player, false);
        //DRAW NPC INVENTORY
        drawInventory(merchant, true);

        //DRAW HINT WINDOW
        int x = gp.tileSize * 2;
        int y = gp.tileSize * 9;
        int width = gp.tileSize * 6;
        int height = gp.tileSize * 2;
        drawSubWindow(x, y, width, height);
        g2.drawString("[ESC] Back", x + 25, y + 80);

        //DRAW PLAYER COIN WINDOW
        x = gp.tileSize * 12;
        y = gp.tileSize * 9;
        width = gp.tileSize * 6;
        height = gp.tileSize * 2;
        drawSubWindow(x, y, width, height);
        g2.drawString("Your coins: " + gp.player.coin, x + 25, y + 80);

        //DRAW PRICE WINDOW
        int itemIndex = getItemIndexOnSlot(npcSlotCol, npcSlotRow);
        if (itemIndex < merchant.inventory.size()){
            
            x = (int)(gp.tileSize * 5.3);
            y = (int)(gp.tileSize * 5.3);
            width = (int)(gp.tileSize * 2.5);
            height = gp.tileSize;
            drawSubWindow(x, y, width, height);
            g2.drawImage(coin, x + 10, y + 10, 40, 40, null);
        
            int price = merchant.inventory.get(itemIndex).price;
            String text = "" + price;
            x = getXforAlignToRightText(text, (int)(gp.tileSize * 7.5));
            g2.drawString(text, x, y + 46);

            //BUY AN ITEM
            if (gp.keyH.enterPressed == true){
                if (merchant.inventory.get(itemIndex).price > gp.player.coin){
                    subState = 0;
                    gp.gameState = gp.dialogueState;
                    currentDialogue = "You need more money to buy that!";
                    drawDialogueScreen();
                }
                else if (gp.player.inventory.size() == gp.player.maxInventorySize){
                    subState = 0;
                    gp.gameState = gp.dialogueState;
                    currentDialogue = "You cannot carry any more!";
                    drawDialogueScreen();
                }
                else{
                    gp.player.coin -= merchant.inventory.get(itemIndex).price;
                    gp.player.inventory.add(merchant.inventory.get(itemIndex));
                }
            }
        }
    }

    public void trade_sell(){

        //DRAW PLAYER INVENTORY
        drawInventory(gp.player, true);

        //DRAW HINT WINDOW
        int x = gp.tileSize * 2;
        int y = gp.tileSize * 9;
        int width = gp.tileSize * 6;
        int height = gp.tileSize * 2;
        drawSubWindow(x, y, width, height);
        g2.drawString("[ESC] Back", x + 25, y + 80);

        //DRAW PLAYER COIN WINDOW
        x = gp.tileSize * 12;
        y = gp.tileSize * 9;
        width = gp.tileSize * 6 - 10;
        height = gp.tileSize * 2;
        drawSubWindow(x, y, width, height);
        g2.drawString("Your coins: " + gp.player.coin, x + 25, y + 80);

        //DRAW PRICE WINDOW
        int itemIndex = getItemIndexOnSlot(playerSlotCol, playerSlotRow);
        if (itemIndex < gp.player.inventory.size()){
            
            x = (int)(gp.tileSize * 15.3);
            y = (int)(gp.tileSize * 5.3);
            width = (int)(gp.tileSize * 2.5);
            height = gp.tileSize;
            drawSubWindow(x, y, width, height);
            g2.drawImage(coin, x + 10, y + 10, 40, 40, null);
        
            int price = gp.player.inventory.get(itemIndex).price / 2;
            String text = "" + price;
            x = getXforAlignToRightText(text, (int)(gp.tileSize * 17.5));
            g2.drawString(text, x, y + 46);

            //SELL AN ITEM
            if (gp.keyH.enterPressed == true){

                if (gp.player.inventory.get(itemIndex) == gp.player.currentWeapon
                        || gp.player.inventory.get(itemIndex) == gp.player.currentShield){
                    commandNum = 0;
                    subState = 0;
                    gp.gameState = gp.dialogueState;
                    currentDialogue = "You cannot sell an equipped item!";
                }
                else{
                    gp.player.inventory.remove(itemIndex);
                    gp.player.coin += price;
                }

            }
        }

    }

    public int getItemIndexOnSlot(int slotCol, int slotRow){
        return slotCol + (slotRow * 5);
    }

    public void drawSubWindow(int x, int y, int width, int height){

        //4th num can be opacity (255 = max, 0 = transparent)
        Color c = new Color(0, 0, 0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 25, 25);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 35, 35);

    }

    //to make sure we dont keep redoing this for everything we want to center
    public int getXforCenteredText(String text){

        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        return x;

    }

    public int getXforAlignToRightText(String text, int tailX){

        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;
        return x;

    }

}
