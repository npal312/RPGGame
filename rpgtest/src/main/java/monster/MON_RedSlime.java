package monster;

import java.awt.Rectangle;
import java.util.Random;

import com.example.GamePanel;

import entity.Entity;
import object.OBJ_Ball;
import object.OBJ_Balls;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_Mana;

public class MON_RedSlime extends Entity{

    GamePanel gp;

    public MON_RedSlime(GamePanel gp) {
        super(gp);

        this.gp = gp;
        
        type = type_monster;
        name = "Red Slime";
        defaultSpeed = 1;
        speed = defaultSpeed;
        maxLife = 4;
        life = maxLife;
        attack = 5;
        defense = 0;
        exp = 2;
        projectile = new OBJ_Ball(gp);

        solidArea = new Rectangle(gp.tileSize * 3 / 16, gp.tileSize * 7 / 16, gp.tileSize * 10 / 16, gp.tileSize * 9 / 16);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
        setAction();

    }

    public void getImage(){

        up1 = setup("/monster/redslime_down_1");
        up2 = setup("/monster/redslime_down_2");
        down1 = setup("/monster/redslime_down_1");
        down2 = setup("/monster/redslime_down_2");
        left1 = setup("/monster/redslime_down_1");
        left2 = setup("/monster/redslime_down_2");
        right1 = setup("/monster/redslime_down_1");
        right2 = setup("/monster/redslime_down_2");

    }

    public void update(){

        //for keeping original update
        super.update();

        int xDistance = Math.abs(worldX - gp.player.worldX);
        int yDistance = Math.abs(worldY - gp.player.worldY);
        int tileDistance = (xDistance + yDistance) / gp.tileSize;

        //for distance based aggro
        if (onPath == false && tileDistance < 5){

            //to make sure it doesn't always aggro the second you get in range
            int i = new Random().nextInt(100) + 1;
            if (i > 50){
                onPath = true;
            }
        }
        //so it doesn't aggro forever if you move far enough away
        if (onPath == true && tileDistance > 10){
            onPath = false;
        }
    }

    public void setAction(){

        if (onPath == true){

            /* FOR PATHFINDING TO COORDS */
            //end down (is fine)
            //int goalCol = 5;
            //int goalRow = 16;

            //end up
            //int goalCol = 14;
            //int goalRow = 12;

            //end left
            //int goalCol = 12;
            //int goalRow = 14;

            //end right (is fine)
            //int goalCol = 13;
            //int goalRow = 23;

            /* FOR PATHFINDING TO PLAYER */
            int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize;

            //searchPath(goalCol, goalRow);
            searchPathPlayer(goalCol, goalRow);

            int i = new Random().nextInt(100) + 1;
            if (i > 99 && projectile.alive == false && shotAvailableCounter == 30){
                
                projectile.set(worldX, worldY, direction, true, this);
                //gp.projectileList.add(projectile);
                //CHECK VACANCY
                for (int j = 0; j < gp.projectile[1].length; j++){
                    if (gp.projectile[gp.currentMap][j] == null){
                        gp.projectile[gp.currentMap][j] = projectile;
                        break;
                    }
                }
                shotAvailableCounter = 0;
                
            }

        }
        else{
            actionLockCounter++;

            if (actionLockCounter == 90){
                
                //could also add a section for sitting still
                Random random = new Random();
                int i = random.nextInt(100) + 1; //num 1 - 100
    
                if (i <= 25){
                    direction = "up";
                }
                else if (i <= 50){
                    direction = "down";
                }
                else if (i <= 75){
                    direction = "left";
                }
                else if (i <= 100){
                    direction = "right";
                }
    
                actionLockCounter = 0;
                
            }

        }

    }

    public void damageReaction(){

        actionLockCounter = 0;
        //for running away from player
        //direction = gp.player.direction;

        //for aggro when attacked
        onPath = true;

    }

    public void checkDrop(){

        //CAST A DIE
        int i = new Random().nextInt(100) + 1;

        //SET THE MONSTER DROP
        if (i < 55){
            dropItem(new OBJ_Coin_Bronze(gp));
        }
        else if (i < 70){
            dropItem(new OBJ_Heart(gp));
        }
        else if (i < 85){
            dropItem(new OBJ_Mana(gp));
        }
        else if (i <= 100){
            dropItem(new OBJ_Balls(gp));
        }

    }

}
