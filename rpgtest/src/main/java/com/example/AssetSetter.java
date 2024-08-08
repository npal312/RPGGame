package com.example;

import object.OBJ_Key;
import object.OBJ_Mana;
import object.OBJ_Potion_Red;
import object.OBJ_Shield_Circle;
import tile_interactive.IT_CrackedWall;
import object.OBJ_Door;
import object.OBJ_Heart;
import entity.NPC_Bro;
import entity.NPC_Rockshop;
import monster.MON_RedSlime;
import object.OBJ_Balls;
import object.OBJ_Boots;
import object.OBJ_Chest;
import object.OBJ_Club_Metal;
import object.OBJ_Coin_Bronze;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    public void setObject(){

        int mapNum = 0;
        int i = 0;

        gp.obj[mapNum][i] = new OBJ_Door(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 4;
        gp.obj[mapNum][i].worldY = gp.tileSize * 27;
        i++;

        gp.obj[mapNum][i] = new OBJ_Key(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 32;
        gp.obj[mapNum][i].worldY = gp.tileSize * 35;
        i++;

        gp.obj[mapNum][i] = new OBJ_Coin_Bronze(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 33;
        gp.obj[mapNum][i].worldY = gp.tileSize * 36;
        i++;

        gp.obj[mapNum][i] = new OBJ_Club_Metal(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 6;
        gp.obj[mapNum][i].worldY = gp.tileSize * 27;
        i++;

        gp.obj[mapNum][i] = new OBJ_Shield_Circle(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 10;
        gp.obj[mapNum][i].worldY = gp.tileSize * 29;
        i++;

        gp.obj[mapNum][i] = new OBJ_Potion_Red(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 12;
        gp.obj[mapNum][i].worldY = gp.tileSize * 28;
        i++;

        gp.obj[mapNum][i] = new OBJ_Coin_Bronze(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 5;
        gp.obj[mapNum][i].worldY = gp.tileSize * 24;
        i++;

        gp.obj[mapNum][i] = new OBJ_Heart(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 7;
        gp.obj[mapNum][i].worldY = gp.tileSize * 23;
        i++;

        gp.obj[mapNum][i] = new OBJ_Mana(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 8;
        gp.obj[mapNum][i].worldY = gp.tileSize * 23;
        i++;

        gp.obj[mapNum][i] = new OBJ_Balls(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 9;
        gp.obj[mapNum][i].worldY = gp.tileSize * 23;

    }

    public void setInteractiveTile(){

        int mapNum = 0;
        int i = 0;

        gp.iTile[mapNum][i] = new IT_CrackedWall(gp, 1, 33);
        i++;
        gp.iTile[mapNum][i] = new IT_CrackedWall(gp, 2, 33);
        i++;
        gp.iTile[mapNum][i] = new IT_CrackedWall(gp, 3, 33);
        i++;
        gp.iTile[mapNum][i] = new IT_CrackedWall(gp, 4, 33);
        i++;
        gp.iTile[mapNum][i] = new IT_CrackedWall(gp, 14, 20);
        i++;
        gp.iTile[mapNum][i] = new IT_CrackedWall(gp, 14, 21);
        i++;
        gp.iTile[mapNum][i] = new IT_CrackedWall(gp, 14, 22);
        i++;

    }

    public void setNPC(){

        int mapNum = 0;
        int i = 0;

        //MAP 0
        gp.npc[mapNum][i] = new NPC_Bro(gp);
        gp.npc[mapNum][i].worldX = gp.tileSize * 8;
        gp.npc[mapNum][i].worldY = gp.tileSize * 24;
        i++;

        //MAP 1
        mapNum = 1;
        i = 0;
        gp.npc[mapNum][i] = new NPC_Rockshop(gp);
        gp.npc[mapNum][i].worldX = gp.tileSize * 24;
        gp.npc[mapNum][i].worldY = gp.tileSize * 24;

    }

    public void setMonster(){

        //change mapNum tochange which map they spawn on
        int mapNum = 0;
        int i = 0; //makes it so you don't have to retype everything

        gp.monster[mapNum][i] = new MON_RedSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize * 24;
        gp.monster[mapNum][i].worldY = gp.tileSize * 32;

        i++;

        gp.monster[mapNum][i] = new MON_RedSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize * 35;
        gp.monster[mapNum][i].worldY = gp.tileSize * 32;

        i++;

        gp.monster[mapNum][i] = new MON_RedSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize * 35;
        gp.monster[mapNum][i].worldY = gp.tileSize * 45;

        i++;

        gp.monster[mapNum][i] = new MON_RedSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize * 24;
        gp.monster[mapNum][i].worldY = gp.tileSize * 45;

    }

}
