package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.example.GamePanel;
import com.example.KeyHandler;
import com.example.UtilityTool;

import object.OBJ_Ball;
import object.OBJ_Key;
import object.OBJ_Shield_Normal;
import object.OBJ_Slimeball;
import object.OBJ_Sword_Normal;

public class Player extends Entity{
    
    KeyHandler keyH;
    public final int screenX;
    public final int screenY;
    public boolean attackCanceled = false;
    public boolean animationCanceled = false;

    public Player(GamePanel gp, KeyHandler keyH){
        super(gp);
        this.keyH = keyH;

        //keeps character in the middle of the screen
        screenX = gp.screenWidth / 2 - (gp.tileSize / 2); //subtract half a tile length because coordinates apply to top left of image
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);
        
        //for making hitbox the size of the tile
        //solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);

        //x, y, width, height
        //Y IS MEASURED TOP DOWN (0 is top)
        //HITBOX
        solidArea = new Rectangle(gp.tileSize / 6, gp.tileSize / 6, gp.tileSize * 4 / 6, gp.tileSize * 4 / 6);
        //for object interaction
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItems();
    }

    public void setDefaultValues(){
        //world map x and y values
        //multiplied by coordinates on world map file
        //STARTING VALUES (WHERE PLAYER SPAWNS IN)
        //worldX = gp.tileSize * 24;
        //worldY = gp.tileSize * 26;
        worldX = gp.tileSize * 8;
        worldY = gp.tileSize * 26;
        defaultSpeed = 4;
        speed = defaultSpeed;
        //any default direction works
        direction = "down";

        //PLAYER STATUS
        //1 heart = 2 hp
        level = 1;
        maxLife = 6;
        life = maxLife;
        maxMana = 4;
        mana = maxMana;
        maxAmmo = 4;
        ammo = maxAmmo;
        strength = 1; //raises attack
        dexterity = 1; //lowers damage
        exp = 0;
        nextLevelExp = 5;
        coin = 0;
        currentWeapon = new OBJ_Sword_Normal(gp);
        currentShield = new OBJ_Shield_Normal(gp);
        projectile = new OBJ_Slimeball(gp);
        attack = getAttack(); //total attack value
        defense = getDefense(); //total defense value
    }

    public void setDefaultPositions(){
        worldX = gp.tileSize * 8;
        worldY = gp.tileSize * 26;
        //any default direction works
        direction = "down";
    }

    public void restoreLifeManaAmmo(){
        life = maxLife;
        mana = maxMana;
        ammo = maxAmmo;
        invincible = false;
    }

    public void setItems(){

        inventory.clear();
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Key(gp));

    }

    public int getAttack(){
        attackArea = currentWeapon.attackArea;
        return attack = strength * currentWeapon.attackValue;
    }

    public int getDefense(){
        return defense = dexterity * currentShield.defenseValue;
    }

    public void getPlayerImage(){
       
        //modified player image grabbing (scales at beginning)
        up1 = setup("/player/slime_up_1");
        up2 = setup("/player/slime_up_2");
        down1 = setup("/player/slime_down_1");
        down2 = setup("/player/slime_down_2");
        left1 = setup("/player/slime_left_1");
        left2 = setup("/player/slime_left_2");
        right1 = setup("/player/slime_right_1");
        right2 = setup("/player/slime_right_2");
    }

    public void getPlayerAttackImage(){
       
        //modified player image grabbing (scales at beginning)
        if (currentWeapon.type == type_sword){
            attackUp1 = setup("/player/slime_attack_up_1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setup("/player/slime_attack_up_2", gp.tileSize, gp.tileSize * 2);
            attackDown1 = setup("/player/slime_attack_down_1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setup("/player/slime_attack_down_2", gp.tileSize, gp.tileSize * 2);
            attackLeft1 = setup("/player/slime_attack_left_1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setup("/player/slime_attack_left_2", gp.tileSize * 2, gp.tileSize);
            attackRight1 = setup("/player/slime_attack_right_1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setup("/player/slime_attack_right_2", gp.tileSize * 2, gp.tileSize);
        }
        if (currentWeapon.type == type_club){
            attackUp1 = setup("/player/slime_club_up_1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setup("/player/slime_club_up_2", gp.tileSize, gp.tileSize * 2);
            attackDown1 = setup("/player/slime_club_down_1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setup("/player/slime_club_down_2", gp.tileSize, gp.tileSize * 2);
            attackLeft1 = setup("/player/slime_club_left_1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setup("/player/slime_club_left_2", gp.tileSize * 2, gp.tileSize);
            attackRight1 = setup("/player/slime_club_right_1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setup("/player/slime_club_right_2", gp.tileSize * 2, gp.tileSize);
        }
    }

    public void update(){
        //if I turn into all if statements, then diagonal/omnidirectional movement
        //if-else if makes it only 4 directional
        //it only accepts one val at a time instead of multiple


        if (attacking == true){
            attacking();
        }
        //if statement makes sure animation doesn't continue when keys are not pressed
        else if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true || keyH.enterPressed == true){


            //Record current worldX and worldY
            int tempWorldX = worldX;
            int tempWorldY = worldY;

            //worldY and worldX lines go here if no collision checking
            if (keyH.upPressed == true){
                direction = "up";
                worldY -= speed;
            }
            if (keyH.downPressed == true){
                direction = "down";
                worldY += speed;
            }
            if (keyH.leftPressed == true){
                direction = "left";
                worldX -= speed;
            }
            if (keyH.rightPressed == true){
                direction = "right";
                worldX += speed;
            }

            //CHECK TILE COLLISION
            collisionOn = false;
            gp.cChecker.checkTile(this);

            //CHECK OBJECT COLLISION
            int objIndex = gp.cChecker.checkObject(this, true);

            //figures out what to do with object
            pickUpObject(objIndex);

            //CHECK NPC COLLISION
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            npcIndex = gp.cChecker.checkTalk(this, gp.npc);
            interactNPC(npcIndex);

            //CHECK MONSTER COLLISION
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            //CHECK INTERACTIVE TILE COLLISION
            gp.cChecker.checkEntity(this, gp.iTile);

            //CHECK EVENT 
            gp.eHandler.checkEvent();

            //IF COLLISION IS FALSE, PLAYER CAN MOVE
            //second condition stops player from moving when enter is pressed
            if(collisionOn == false && keyH.enterPressed == false){
                animationCanceled = false;
            }
            if(collisionOn == true){
                worldX = tempWorldX;
                worldY = tempWorldY;
            }

            if(keyH.enterPressed == true && attackCanceled == false){
                gp.playSE(7);
                attacking = true;
                spriteCounter = 0;
                animationCanceled = false;
            }

            attackCanceled = false;


            if (animationCanceled == false){
                spriteCounter++;
            }
            animationCanceled = true;
            
            //10 for amount of update calls between sprites
            if (spriteCounter > 10){
                if(spriteNum == 1){
                    spriteNum = 2;
                }
                else if (spriteNum == 2){
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        } 
        gp.keyH.enterPressed = false;

        //only one projectile at a time
        if (gp.keyH.shotKeyPressed == true && projectile.alive == false
            && shotAvailableCounter == 30 && projectile.haveResource(this) == true){

            //SET DEFAULT COORDINATES, DIRECTION, AND USER
            projectile.set(worldX, worldY, direction, true, this);

            //SUBTRACT COST
            projectile.subtractResource(this);

            //ADD IT TO THE LIST
            //gp.projectileList.add(projectile);
            //CHECK VACANCY
            for (int i = 0; i < gp.projectile[1].length; i++){
                if (gp.projectile[gp.currentMap][i] == null){
                    gp.projectile[gp.currentMap][i] = projectile;
                    break;
                }
            }

            shotAvailableCounter = 0;

            if (projectile.type == type_range_phys){
                gp.playSE(11);
            }
            else{
                gp.playSE(10);
            }
            

        }

        if (keyH.manaShows && projectile.type != type_range_mana){
            projectile = new OBJ_Slimeball(gp);
            shotAvailableCounter = 0;
        }
        if (!keyH.manaShows && projectile.type != type_range_phys){
            projectile = new OBJ_Ball(gp);
            shotAvailableCounter = 0;
        }
        
        //NEEDS to be outside of key if statement!
        if (invincible == true){
            invincibleCounter++;
            //1 second invincibility
            if (invincibleCounter > 60){
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if (shotAvailableCounter < 30){
            shotAvailableCounter++;
        }

        //not needed yet but might be good to add anyways
        /*if (life > maxLife){
            life = maxLife;
        }
        if (mana > maxMana){
            mana = maxMana;
        }*/

        if (life <= 0){
            gp.gameState = gp.gameOverState;
            gp.ui.commandNum = -1;
            gp.stopMusic();
            //would do here
            //gp.playMusic(deathMusic);
            gp.playSE(12);
        }

    }

    public void attacking(){

        spriteCounter++;

        if (spriteCounter <= 5){
            spriteNum = 1;
        }
        //change second val to make harder
        else if (spriteCounter <= 25){
            spriteNum = 2;

            //Save current worldX, worldY, solidArea
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            //Adjust player's worldX/Y for the attackArea
            switch(direction){
                case "up": worldY -= attackArea.height; break;
                case "down": worldY += attackArea.height; break;
                case "left": worldX -= attackArea.height; break;
                case "right": worldX += attackArea.height; break;
            }

            //attackArea becomes solidArea
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;
            //Check monster collision with updated worldX, worldY, and solidArea
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex, attack, currentWeapon.knockbackPower);

            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
            damageInteractiveTile(iTileIndex);

            int projectileIndex = gp.cChecker.checkEntity(this, gp.projectile);
            damageProjectile(projectileIndex);

            //After checking collision, restore original data
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;

        }
        else if (spriteCounter > 25){
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }

    }

    public void pickUpObject(int i){

        if (i != 999){

            //PICKUP ONLY ITEMS
            if (gp.obj[gp.currentMap][i].type == type_pickup_item){

                gp.obj[gp.currentMap][i].use(this);
                gp.obj[gp.currentMap][i] = null;

            }
            //INVENTORY ITEMS
            else{

                String text;

                if (inventory.size() != maxInventorySize){
    
                    inventory.add(gp.obj[gp.currentMap][i]);
                    gp.playSE(1);
                    text = "You got a " + gp.obj[gp.currentMap][i].name + "!";
    
                }
                else{
                    text = "You cannot carry any more!";
                }
                gp.ui.addMessage(text);
                gp.obj[gp.currentMap][i] = null;

            }            
        }
    }

    public void interactNPC(int i){

        if (gp.keyH.enterPressed == true){

            if (i != 999){
            
                attackCanceled = true;
                gp.gameState = gp.dialogueState;
                gp.npc[gp.currentMap][i].speak();
    
            }
        }
    }

    public void contactMonster(int i){

        if (i != 999){

            if (invincible == false && gp.monster[gp.currentMap][i].dying == false){
                gp.playSE(6);

                int damage = gp.monster[gp.currentMap][i].attack - defense;
                if (damage < 0){
                    damage = 0;
                }

                life -= damage;
                invincible = true;
            }

        }

    }

    public void damageMonster(int i, int attack, int knockbackPower){

        if (i != 999){

            if (gp.monster[gp.currentMap][i].invincible == false){

                gp.playSE(5);

                if (knockbackPower > 0){
                    knockback(gp.monster[gp.currentMap][i], knockbackPower);
                }

                int damage = attack - gp.monster[gp.currentMap][i].defense;
                if (damage < 0){
                    damage = 0;
                }

                gp.monster[gp.currentMap][i].life -= damage;
                gp.ui.addMessage(damage + " damage!");
                gp.monster[gp.currentMap][i].invincible = true;
                gp.monster[gp.currentMap][i].damageReaction();

                if (gp.monster[gp.currentMap][i].life <= 0){
                    gp.monster[gp.currentMap][i].dying = true;
                    gp.ui.addMessage("Killed the " + gp.monster[gp.currentMap][i].name + "!");
                    gp.ui.addMessage("You got " + gp.monster[gp.currentMap][i].exp + " exp!");
                    exp += gp.monster[gp.currentMap][i].exp;
                    checkLevelUp();
                }
            }
        }
    }

    public void knockback(Entity entity, int knockbackPower){

        entity.direction = direction;
        entity.speed += knockbackPower;
        entity.knockback = true;

    }

    public void damageInteractiveTile(int i){

        if (i != 999 && gp.iTile[gp.currentMap][i].destructible == true
            && gp.iTile[gp.currentMap][i].isCorrectItem(this) == true && gp.iTile[gp.currentMap][i].invincible == false){

            //if you want tiles that break in one hit
            //can get rid of life stuff and take last line out of if statement
            gp.iTile[gp.currentMap][i].playSE();
            gp.iTile[gp.currentMap][i].life--;
            gp.iTile[gp.currentMap][i].invincible = true;

            //generate particle
            generateParticle(gp.iTile[gp.currentMap][i], gp.iTile[gp.currentMap][i]);

            if (gp.iTile[gp.currentMap][i].life == 0){
                gp.iTile[gp.currentMap][i] = gp.iTile[gp.currentMap][i].getDestroyedForm();
            }

        }

    }

    public void damageProjectile(int i){
        if (i != 999){
            Entity projectile = gp.projectile[gp.currentMap][i];
            projectile.alive = false;
            generateParticle(projectile, projectile);
        }
    }

    public void checkLevelUp(){
        if (exp >= nextLevelExp){

            level++;
            nextLevelExp = nextLevelExp * 2;
            maxLife += 2;
            maxMana += 1;
            maxAmmo += 1;
            strength++;
            dexterity++;
            //recalculate attack and defense
            attack = getAttack();
            defense = getDefense();

            gp.playSE(8);
            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "You leveled up to level " + level + "!\n"
                    + "You feel stronger!";

        }
    }

    public void selectItem(){

        int itemIndex = gp.ui.getItemIndexOnSlot(gp.ui.playerSlotCol, gp.ui.playerSlotRow);

        if (itemIndex < inventory.size()){

            Entity selectedItem = inventory.get(itemIndex);

            if (selectedItem.type == type_sword || selectedItem.type == type_club){
                currentWeapon = selectedItem;
                attack = getAttack();
                getPlayerAttackImage();
            }
            if (selectedItem.type == type_shield){
                currentShield = selectedItem;
                defense = getDefense();
            }
            if (selectedItem.type == type_consumable){
                selectedItem.use(this);
                inventory.remove(itemIndex);
            }

        }

    }

    public void draw(Graphics2D g2){
        //g2.setColor(Color.white);
        //x coord, y coord, width, height
        //g2.fillRect(x, y, gp.tileSize, gp.tileSize);

        BufferedImage image = null;

        //mainly for attack animations (to stop the random teleporting)
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        //could honestly use an array of images or something to go through them instead of if statements
        //in case more frames are needed
        switch(direction){
            case "up":
                if (attacking == false){
                    if (spriteNum == 1){image = up1;}
                    if (spriteNum == 2){image = up2;}
                }
                if (attacking == true){
                    tempScreenY = screenY - gp.tileSize;
                    if (spriteNum == 1){image = attackUp1;}
                    if (spriteNum == 2){image = attackUp2;}
                }
                break;
            case "down":
                if (attacking == false){
                    if (spriteNum == 1){image = down1;}
                    if (spriteNum == 2){image = down2;}
                }
                if (attacking == true){
                    if (spriteNum == 1){image = attackDown1;}
                    if (spriteNum == 2){image = attackDown2;}
                }
                break;
            case "left":
                if (attacking == false){
                    if (spriteNum == 1){image = left1;}
                    if (spriteNum == 2){image = left2;}
                }
                if (attacking == true){
                    tempScreenX = screenX - gp.tileSize;
                    if (spriteNum == 1){image = attackLeft1;}
                    if (spriteNum == 2){image = attackLeft2;}
                }
                break;
            case "right":
                if (attacking == false){
                    if (spriteNum == 1){image = right1;}
                    if (spriteNum == 2){image = right2;}
                }
                if (attacking == true){
                    if (spriteNum == 1){image = attackRight1;}
                    if (spriteNum == 2){image = attackRight2;}
                }
                break;
        }

        if (invincible == true){
            //make player semi-transparent
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        //don't need scale if pre-scaled
        g2.drawImage(image, tempScreenX, tempScreenY, null);

        //RESET ALPHA
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

    }
}

