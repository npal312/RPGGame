package entity;

import java.awt.Rectangle;
import java.util.Random;

import com.example.GamePanel;

public class NPC_Bro extends Entity{

    public NPC_Bro(GamePanel gp){
        super(gp);

        direction = "down";
        speed = 2;
        solidArea = new Rectangle(gp.tileSize / 4, gp.tileSize * 5 / 16, gp.tileSize / 2, gp.tileSize * 11 / 16);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getImage();
        setDialogue();
        name = "bro";

    }

     public void getImage(){
       
        //modified player image grabbing (scales at beginning)
        up1 = setup("/npc/creeper_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/npc/creeper_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/npc/creeper_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/npc/creeper_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/npc/creeper_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/npc/creeper_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/npc/creeper_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/npc/creeper_right_2", gp.tileSize, gp.tileSize);
    }

    public void setDialogue(){

        dialogues[0] = "Good morning.";
        dialogues[1] = "So you've popped up in this area somehow.";
        dialogues[2] = "I hear there's a treasure hidden somewhere\nin the vicinity.";
        dialogues[3] = "If you want to find it, good luck.";

    }

    //essentially acts as AI
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

        }
        else{
            actionLockCounter++;

            /*if (actionLockCounter % 4 == 1){
                direction = "up";
            }
            else if (actionLockCounter % 4 == 2){
                direction = "right";
            }
            else if (actionLockCounter % 4 == 3){
                direction = "down";
            }
            else if (actionLockCounter % 4 == 0){
                direction = "left";
            }*/
            actionLockCounter++;

            if (actionLockCounter == 120){
                
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

    //this makes dialogue customization possible to have this here but call super
    public void speak(){

        super.speak();

        onPath = true;

    }

}
