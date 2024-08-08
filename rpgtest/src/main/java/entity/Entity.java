package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.example.GamePanel;
import com.example.UtilityTool;

public class Entity {
    
    GamePanel gp;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
    public BufferedImage image, image2, image3;
    //for drawing hitbox
    //can just define with = new Rectangle(val, val, val, val) here if they are all the same
    //REPLACE 64 WITH WHATEVER TILE SIZE IS;
    public Rectangle solidArea = new Rectangle(0, 0, 64, 64);
    //depends on weapon
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collision = false;
    //to add dialogue (also can go crazy with changing this depending on what part of the game you're on or if you've talked to them already)
    //can add different checks that change the dialogue array a specific entity references
    String dialogues[] = new String[20];
    
    //STATE
    public int worldX, worldY;
    public String direction = "down";
    //for animation with multiple sprites
    public int spriteNum = 1;
    int dialogueIndex = 0;
    public boolean collisionOn = false;
    public boolean invincible = false;
    boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    boolean hpBarOn = false;
    public boolean onPath = false;
    public boolean knockback = false;

    public boolean animationCanceled = true;

    //COUNTER
    public int spriteCounter = 0;
    //so npc doesn't go crazy and move constantly
    public int actionLockCounter = 0;
    public int invincibleCounter = 0;
    public int shotAvailableCounter = 0;
    int dyingCounter = 0;
    int hpBarCounter = 0;
    int knockbackCounter = 0;

    //CHARACTER ATTRIBUTES
    public String name;
    //assign defaultSpeed to any entity that you want to provide knockback to
    //knockback changes speed, uses this to reassign speed afterwards
    public int defaultSpeed;
    public int speed;
    public int maxLife;
    public int life;
    public int maxMana;
    public int mana;
    public int maxAmmo;
    public int ammo;
    public int level;
    public int strength;
    public int dexterity;
    public int attack;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public Entity currentWeapon;
    public Entity currentShield;
    public Projectile projectile;

    //ITEM ATTRIBUTES
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;
    public int value;
    public int attackValue;
    public int defenseValue;
    public String description = "";
    //mana usecost
    public int useCost;
    public int price;
    public int knockbackPower = 0;

    //TYPE
    public int type; //0 = player, 1 = npc, 2 = monster
    public final int type_player = 0;
    public final int type_npc = 1;
    public final int type_monster = 2;
    public final int type_sword = 3;
    public final int type_club = 4;
    public final int type_shield = 5;
    public final int type_consumable = 6;
    public final int type_pickup_item = 7;
    public final int type_range_mana = 8;
    public final int type_range_phys = 9;

    public Entity(GamePanel gp){
        this.gp = gp;
    }

    public void setAction(){}

    public void damageReaction(){}

    public void use(Entity entity){}

    public void checkDrop(){

    }

    public void dropItem(Entity droppedItem){

        for (int i = 0; i < gp.obj[1].length; i++){
            //when it dies, drop where it was
            if (gp.obj[gp.currentMap][i] == null){
                gp.obj[gp.currentMap][i] = droppedItem;
                gp.obj[gp.currentMap][i].worldX = worldX;
                gp.obj[gp.currentMap][i].worldY = worldY;
                break;
            }
        }

    }

    public void speak(){

        if (dialogues[dialogueIndex] == null){
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;

        switch(gp.player.direction){
            case "up":
                direction = "down";
                break;
            case "down":
                direction = "up";
                break;
            case "left":
                direction = "right";
                break;
            case "right":
                direction = "left";
                break;
        }


    }

    public Color getParticleColor(){
        return null;
    }

    public int getParticleSize(){
        int size = 0; //in pixels
        return size;
    }

    public int getParticleSpeed(){
        int speed = 0;
        return speed;
    }

    public int getParticleMaxLife(){
        int maxLife = 0;
        return maxLife;
    }

    public void generateParticle(Entity generator, Entity target){
        
        Color color = generator.getParticleColor();
        int size = generator.getParticleSize();
        int speed = generator.getParticleSpeed();
        int maxLife = generator.getParticleMaxLife();

        //integers at end are for direction
        //top left, top right, bottom left, bottom right
        //can increase horizontal or vertical vectors separately
        Particle p1 = new Particle(gp, target, color, size, speed, maxLife, -2, -1);
        Particle p2 = new Particle(gp, target, color, size, speed, maxLife, 2, -1);
        Particle p3 = new Particle(gp, target, color, size, speed, maxLife, -2, 1);
        Particle p4 = new Particle(gp, target, color, size, speed, maxLife, 2, 1);
        gp.particleList.add(p1);
        gp.particleList.add(p2);
        gp.particleList.add(p3);
        gp.particleList.add(p4);

    }

    public void checkCollision(){

        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        gp.cChecker.checkEntity(this, gp.iTile);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        if (this.type == type_monster && contactPlayer == true){
            damagePlayer(attack);
        }

    }

    //entity update (besides player)
    public void update(){

        if (knockback == true){
            checkCollision();

            if (collisionOn == true){
                knockbackCounter = 0;
                knockback = false;
                speed = defaultSpeed;
            }
            else if (collisionOn == false){
                switch(gp.player.direction){
                    case "up":
                        //y values go top down, so subtract to go up
                        worldY -= speed;
                        animationCanceled = false;
                        break;
                    case "down":
                        worldY += speed;
                        animationCanceled = false;
                        break;
                    case "left":
                        //x values go left to right, so subtract to go left
                        worldX -= speed;
                        animationCanceled = false;
                        break;
                    case "right":
                        worldX += speed;
                        animationCanceled = false;
                        break;
                }

                knockbackCounter++;
                if (knockbackCounter == 10){
                    knockbackCounter = 0;
                    knockback = false;
                    speed = defaultSpeed;
                }

            }
        }
        else{
            //subclass takes priority
            setAction();
            checkCollision();

            //IF COLLISION IS FALSE, PLAYER CAN MOVE
            if(collisionOn == false){
                switch(direction){
                    case "up":
                        //y values go top down, so subtract to go up
                        worldY -= speed;
                        animationCanceled = false;
                        break;
                    case "down":
                        worldY += speed;
                        animationCanceled = false;
                        break;
                    case "left":
                        //x values go left to right, so subtract to go left
                        worldX -= speed;
                        animationCanceled = false;
                        break;
                    case "right":
                        worldX += speed;
                        animationCanceled = false;
                        break;
                }
            }
        }

        //makes animations not continue if game is spammed
        if (animationCanceled == false){
            spriteCounter++;
        }
        animationCanceled = true;

        //10 for amount of update calls between sprites
        if (spriteCounter > 24){
            if(spriteNum == 1){
                spriteNum = 2;
            }
            else if (spriteNum == 2){
                spriteNum = 1;
            }
            spriteCounter = 0;
        }


        if (invincible == true){
            invincibleCounter++;
            if (invincibleCounter > 40){
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if (shotAvailableCounter < 30){
            shotAvailableCounter++;
        }

    }

    public void damagePlayer(int attack){

        if (gp.player.invincible == false){
            //we can give damage
            gp.playSE(6);

            int damage = attack - gp.player.defense;
            if (damage < 0){
                damage = 0;
            }

            gp.player.life -= damage;
            gp.player.invincible = true;
            animationCanceled = false;
        }

    }

    public void draw(Graphics2D g2){

        BufferedImage image = null;

        //puts object in correct spot by figuring out where it should be on the screen in reference to the player (both in the world and given that they're in the middle)
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        
        //ensures only objects shown on screen get drawn instead of every single object in the map, even ones offscreen
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && 
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY){

            switch(direction){
                case "up":
                    if (spriteNum == 1){image = up1;}
                    if (spriteNum == 2){image = up2;}
                    break;
                case "down":
                    if (spriteNum == 1){image = down1;}
                    if (spriteNum == 2){image = down2;}
                    break;
                case "left":
                    if (spriteNum == 1){image = left1;}
                    if (spriteNum == 2){image = left2;}
                    break;
                case "right":
                    if (spriteNum == 1){image = right1;}
                    if (spriteNum == 2){image = right2;}
                    break;
            }

            //Monster HP bar
            //only shows up when hitting and goes away after some time
            if (type == type_monster && hpBarOn == true){

                double oneScale = (double)gp.tileSize / maxLife;
                double hpBarValue = oneScale * life;

                g2.setColor(new Color(35, 35, 35));
                g2.fillRect(screenX - 1, screenY - 1, gp.tileSize + 2, 9);

                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY, (int)hpBarValue, 7);

                hpBarCounter++;

                if (hpBarCounter > 300){
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            if (invincible == true){
                hpBarOn = true;
                hpBarCounter = 0;
                //make entity semi-transparent
                changeAlpha(g2, 0.4f);
            }

            if (dying == true){
                dyingAnimation(g2);
            }
            
            g2.drawImage(image, screenX, screenY, null);

            //RESET ALPHA
            changeAlpha(g2, 1f);
        }

    }

    public void dyingAnimation(Graphics2D g2){

        dyingCounter++;

        //makes death animation changes easier
        int i = 5;

        //oscillates between translucent and clear
        //once 40 frames pass, death animation is done
        if ((dyingCounter / i) % 2 == 0 && dyingCounter <= i * 8){changeAlpha(g2, 0f);}
        else if ((dyingCounter / i) % 2 == 1 && dyingCounter <= i * 8){changeAlpha(g2, 1f);}
        else if (dyingCounter > i * 8){
            alive = false;    
        }

    }

    public void changeAlpha(Graphics2D g2, float alphaValue){
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    //checks if any given stat is higher than max
    public int checkMax(int value, int current, int max){
        if (value + current > max){
            return max - current;
        }
        return value;
    }

    public BufferedImage setup(String imagePath){

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try{

            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch(IOException e){
            e.printStackTrace();
        }

        return image;

    }

    public BufferedImage setup(String imagePath, int width, int height){

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try{

            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, width, height);

        } catch(IOException e){
            e.printStackTrace();
        }

        return image;

    }

    public void searchPath(int goalCol, int goalRow){

        int startCol = (worldX + solidArea.x) / gp.tileSize;
        int startRow = (worldY + solidArea.y) / gp.tileSize;

        gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow);

        //path found (now must physically find path to pathfind)
        if (gp.pFinder.search() == true){

            //Next worldX and worldY
            int nextX = gp.pFinder.pathList.get(0).col * gp.tileSize;
            int nextY = gp.pFinder.pathList.get(0).row * gp.tileSize;

            //Entity's solidArea position
            int enLeftX = worldX + solidArea.x;
            int enRightX = worldX + solidArea.x + solidArea.width;
            int enTopY = worldY + solidArea.y;
            int enBottomY = worldY + solidArea.y + solidArea.height;

            //where it gets complicated (making sure it stays on path and doesn't get stuck)
            if (enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize){
                direction = "up";
            }
            else if (enTopY < nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize){
                direction = "down";
            }
            else if (enTopY >= nextY && enBottomY < nextY + gp.tileSize){
                //left or right
                if (enLeftX > nextX){
                    direction = "left";
                }
                if (enLeftX < nextX){
                    direction = "right";
                }
            }

            int nextCol = gp.pFinder.pathList.get(0).col;
            int nextRow = gp.pFinder.pathList.get(0).row;
            
            //done :)
            if (nextCol == goalCol && nextRow == goalRow){
                /*System.out.println("Yippee");
                System.out.println("L: " + enLeftX / gp.tileSize + ", R: " + enRightX / gp.tileSize + ", T: " + enTopY / gp.tileSize + ", B: " + enBottomY / gp.tileSize);
                System.out.println("GoalCol: " + goalCol + ", GoalRow: " + goalRow);
                System.out.println("L: " + enLeftX + ", R: " + enRightX + ", T: " + enTopY + ", B: " + enBottomY);
                System.out.println("WorldX: " + worldX + ", WorldY: " + worldY);*/
                System.out.println(".");
                if (goalCol == enLeftX / gp.tileSize && goalCol == enRightX / gp.tileSize && goalRow == enTopY / gp.tileSize && goalRow == enBottomY / gp.tileSize){
                //if (goalCol == worldX && goalRow == worldY){
                    onPath = false;
                    for (int i = 0; i < gp.pFinder.pathList.size(); i++){
                        System.out.println("Hi");
                    }
                }
                //if goal is to right of left side
                else if (goalCol > enLeftX / gp.tileSize) {
                    direction = "right";
                }
                //if goal is to left of right side
                else if (goalCol < enRightX / gp.tileSize){
                    direction = "left";
                }
                //if goal is below top
                else if (goalRow > enTopY / gp.tileSize){
                    direction = "down";
                }
                //if goal is above bottome
                else if (goalRow < enBottomY / gp.tileSize){
                    direction = "up";
                }

            }

        }
        else {
            onPath = false;
        }

    }

    //get rid of final part if you're following the player
    public void searchPathPlayer(int goalCol, int goalRow){

        int startCol = (worldX + solidArea.x) / gp.tileSize;
        int startRow = (worldY + solidArea.y) / gp.tileSize;

        gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow);

        //path found (now must physically find path to pathfind)
        if (gp.pFinder.search() == true){

            //Next worldX and worldY
            int nextX = gp.pFinder.pathList.get(0).col * gp.tileSize;
            int nextY = gp.pFinder.pathList.get(0).row * gp.tileSize;

            //Entity's solidArea position
            int enLeftX = worldX + solidArea.x;
            int enRightX = worldX + solidArea.x + solidArea.width;
            int enTopY = worldY + solidArea.y;
            int enBottomY = worldY + solidArea.y + solidArea.height;

            //where it gets complicated (making sure it stays on path and doesn't get stuck)
            if (enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize){
                direction = "up";
            }
            else if (enTopY < nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize){
                direction = "down";
            }
            else if (enTopY >= nextY && enBottomY < nextY + gp.tileSize){
                //left or right
                if (enLeftX > nextX){
                    direction = "left";
                }
                if (enLeftX < nextX){
                    direction = "right";
                }
            }

            int nextCol = gp.pFinder.pathList.get(0).col;
            int nextRow = gp.pFinder.pathList.get(0).row;

        }
        //maybe comment
        /*else {
            onPath = false;
        }*/

    }

}
