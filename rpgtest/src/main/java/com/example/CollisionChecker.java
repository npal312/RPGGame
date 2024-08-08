package com.example;

import entity.Entity;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp){
        this.gp = gp;
    }

    //SHOULD IMPLEMENT way to stop out of bounds inputs from mattering
    public void checkTile(Entity entity){
        
        //getting corner values of hitbox
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        //getting row/col values of hitbox
        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        //for checking the two tiles at a time needed to check for collision
        //ex: if moving up, only top left and top right corner boxes need to be checked
        //can maybe add more tileNums and check more tiles (or check multiple directions at once) if omnidirectional movement is desired
        int tileNum1, tileNum2, tileNum3, tileNum4;

        tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
        tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
        tileNum3 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
        tileNum4 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
        if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true
            || gp.tileM.tile[tileNum3].collision == true || gp.tileM.tile[tileNum4].collision == true){
            entity.collisionOn = true;
        }
    }

    public int checkObject(Entity entity, boolean player){

        //default val (one we will never reach)
        int index = 999;

        //way to check for collision if there aren't many objects
        //many tiles onscreen, so using a for loop to check every tile would take forever
        //other method only checks 2 at a time (the necessary ones)
        for (int i = 0; i < gp.obj[1].length; i++){
            if (gp.obj[gp.currentMap][i] != null){
                //get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                //get the object's solid area position
                //solidArea.x is 0 but works if vals change
                gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].worldX + gp.obj[gp.currentMap][i].solidArea.x;
                gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].worldY + gp.obj[gp.currentMap][i].solidArea.y;

                switch(entity.direction){
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                }

                //checks if hitbox Rectangles are colliding
                if (entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)){
                    if(gp.obj[gp.currentMap][i].collision == true){
                        entity.collisionOn = true;
                    }
                    if (player == true){
                        index = i;
                    }
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].solidAreaDefaultX;
                gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].solidAreaDefaultY;
            }
        }

        return index;
    }
    
    //NPC WHILE STANDING STILL
    public int checkTalk(Entity entity, Entity[][] target){
        int index = 999;

        for (int i = 0; i < target[1].length; i++){
            if (target[gp.currentMap][i] != null){
                //get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                //get the object's solid area position
                //solidArea.x is 0 but works if vals change
                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].worldX + target[gp.currentMap][i].solidArea.x;
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].worldY + target[gp.currentMap][i].solidArea.y;

                //tileSize / 2 to reach NPC's that aren't directly colliding
                switch(entity.direction){
                    case "up":
                        entity.solidArea.y -= gp.tileSize / 2;
                        break;
                    case "down":
                        entity.solidArea.y += gp.tileSize / 2;
                        break;
                    case "left":
                        entity.solidArea.x -= gp.tileSize / 2;
                        break;
                    case "right":
                        entity.solidArea.x += gp.tileSize / 2;
                        break;
                }

                //checks if hitbox Rectangles are colliding
                //DOESN'T change collisionOn
                if (entity.solidArea.intersects(target[gp.currentMap][i].solidArea)){
                    if (target[gp.currentMap][i] != entity){
                        index = i;
                    }
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].solidAreaDefaultX;
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].solidAreaDefaultY;

            }
        }

        return index;
    }

    //NPC OR MONSTER
    public int checkEntity(Entity entity, Entity[][] target){
        //default val (one we will never reach)
        int index = 999;

        //way to check for collision if there aren't many objects
        //many tiles onscreen, so using a for loop to check every tile would take forever
        //other method only checks 2 at a time (the necessary ones)
        for (int i = 0; i < target[1].length; i++){
            if (target[gp.currentMap][i] != null){
                //get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                //get the object's solid area position
                //solidArea.x is 0 but works if vals change
                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].worldX + target[gp.currentMap][i].solidArea.x;
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].worldY + target[gp.currentMap][i].solidArea.y;

                switch(entity.direction){
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                }

                //checks if hitbox Rectangles are colliding
                if (entity.solidArea.intersects(target[gp.currentMap][i].solidArea)){
                    if (target[gp.currentMap][i] != entity){
                        entity.collisionOn = true;
                        index = i;
                    }
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].solidAreaDefaultX;
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].solidAreaDefaultY;
            }
        }

        return index;
    }

    public boolean checkPlayer(Entity entity){

        boolean contactPlayer = false;

        //get entity's solid area position
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;

        //get the object's solid area position
        //solidArea.x is 0 but works if vals change
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        switch(entity.direction){
            case "up":
                entity.solidArea.y -= entity.speed;
                break;
            case "down":
                entity.solidArea.y += entity.speed;
                break;
            case "left":
                entity.solidArea.x -= entity.speed;
                break;
            case "right":
                entity.solidArea.x += entity.speed;
                break;
        }

        //checks if hitbox Rectangles are colliding
        if (entity.solidArea.intersects(gp.player.solidArea)){
            entity.collisionOn = true;
            contactPlayer = true;
        }

        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;

        return contactPlayer;

    }

}
