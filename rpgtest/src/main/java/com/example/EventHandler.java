package com.example;

import entity.Entity;

public class EventHandler {

    GamePanel gp;
    EventRect eventRect[][][];

    //to save previous event and stop someone from triggering the same one repeatedly
    int previousEventX, previousEventY;
    boolean canTouchEvent = true;
    int tempMap, tempCol, tempRow;

    public EventHandler(GamePanel gp){
        this.gp = gp;

        eventRect = new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

        int map = 0;
        int col = 0;
        int row = 0;
        while (map < gp.maxMap && col < gp.maxWorldCol && row < gp.maxWorldRow){

            eventRect[map][col][row] = new EventRect();
            //making 2 x 2 pixel rectangle (so you really have to be on the tile for it to activate)
            eventRect[map][col][row].x = (gp.tileSize / 2) - 1;
            eventRect[map][col][row].y = (gp.tileSize / 2) - 1;
            eventRect[map][col][row].width = 2;
            eventRect[map][col][row].height = 2;
            eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x;
            eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y;

            col++;
            if (col == gp.maxWorldCol){
                col = 0;
                row++;

                if (row == gp.maxWorldRow){
                    row = 0;
                    map++;
                }
            }

        }

    }

    public void checkEvent(){

        //Check if player is more than 1 tile away from last event
        int xDistance = Math.abs(gp.player.worldX - previousEventX);
        int yDistance = Math.abs(gp.player.worldY - previousEventY);
        //could go crazy and calculate magnitude to see if 1 tile away (maybe shouldn't tho)
        int distance = Math.max(xDistance, yDistance);
        if (distance > gp.tileSize){
            canTouchEvent = true;
        }

        if (canTouchEvent == true){
            //even if they're 1 tile away, this works (yay)
            //if (hit(0, 3, 26, "left") == true){ damagePit(gp.dialogueState); }
            //if (hit(0, 3, 25, "any") == true){ damagePit(gp.dialogueState); }
            //if (hit(0, 11, 26, "any") == true){ teleport(gp.dialogueState, 11, 37); }
            if (hit(0, 8, 28, "down") == true){ healingPool(gp.dialogueState); }
            else if (hit(0, 14, 19, "any") == true) { mapTeleport(1, 24, 33);}
            else if (hit(1, 24, 33, "any") == true) { mapTeleport(0, 14, 19);}
            else if (hit(1, 24, 26, "up") == true) { speak(gp.npc[1][0]); }

        }
        
    }


    public boolean hit(int map, int col, int row, String reqDirection){

        boolean hit = false;

        if (map == gp.currentMap){
            gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
            gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
            eventRect[map][col][row].x = col * gp.tileSize + eventRect[map][col][row].x;
            eventRect[map][col][row].y = row * gp.tileSize + eventRect[map][col][row].y;

            //checking if hitboxes intersect and if player is in right direction (if applicable)
            if (gp.player.solidArea.intersects(eventRect[map][col][row]) && eventRect[map][col][row].eventDone == false){
                if (gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")){
                    hit = true;

                    previousEventX = gp.player.worldX;
                    previousEventY = gp.player.worldY;
                }
            }

            gp.player.solidArea.x = gp.player.solidAreaDefaultX;
            gp.player.solidArea.y = gp.player.solidAreaDefaultY;
            eventRect[map][col][row].x = eventRect[map][col][row].eventRectDefaultX;
            eventRect[map][col][row].y = eventRect[map][col][row].eventRectDefaultY;
        }
        return hit;
    }

    public void teleport(int gameState, int col, int row){

        gp.gameState = gameState;
        gp.ui.currentDialogue = "Teleporting! (oooh)";
        gp.player.worldX = gp.tileSize * col;
        gp.player.worldY = gp.tileSize * row;

    }

    public void mapTeleport(int map, int col, int row){

        gp.gameState = gp.transitionState;
        tempMap = map;
        tempCol = col;
        tempRow = row;

        canTouchEvent = false;
        gp.playSE(13);

    }

    //used to extend npc interaction through tiles
    public void speak(Entity entity){

        if (gp.keyH.enterPressed == true){

            gp.gameState = gp.dialogueState;
            gp.player.attackCanceled = true;
            entity.speak();

        }

    }

    public void damagePit(int gameState){

        gp.gameState = gameState;
        gp.ui.currentDialogue = "You fell into a pit!";
        gp.player.life -= 1;
        canTouchEvent = false;

        //makes a one-time event
        //eventRect[col][row].eventDone = true;

    }

    public void healingPool(int gameState){
        //this is now elden ring (any soulslike)
        //you heal and enemies respawn

        if (gp.keyH.enterPressed == true){
            gp.gameState = gameState;
            gp.player.attackCanceled = true;
            gp.ui.currentDialogue = "You drank from the pool.\nSurprisingly, you fully heal.";
            gp.player.life = gp.player.maxLife;
            gp.player.mana = gp.player.maxMana;
            gp.player.ammo = gp.player.maxAmmo;
            gp.aSetter.setMonster();
        }

    }


}
