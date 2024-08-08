package entity;

import java.awt.Rectangle;

import com.example.GamePanel;

import object.OBJ_Club_Metal;
import object.OBJ_Key;
import object.OBJ_Potion_Red;
import object.OBJ_Shield_Circle;
import object.OBJ_Shield_Normal;
import object.OBJ_Sword_Normal;

public class NPC_Rockshop extends Entity{

    public NPC_Rockshop(GamePanel gp){
        super(gp);

        direction = "down";
        speed = 1;
        solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getImage();
        setDialogue();
        setItems();
        name = "rockshop";

    }

     public void getImage(){
       
        //modified player image grabbing (scales at beginning)
        up1 = setup("/npc/rockshop_down_1", gp.tileSize, gp.tileSize);
        up2 = setup("/npc/rockshop_down_2", gp.tileSize, gp.tileSize);
        down1 = setup("/npc/rockshop_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/npc/rockshop_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/npc/rockshop_down_1", gp.tileSize, gp.tileSize);
        left2 = setup("/npc/rockshop_down_2", gp.tileSize, gp.tileSize);
        right1 = setup("/npc/rockshop_down_1", gp.tileSize, gp.tileSize);
        right2 = setup("/npc/rockshop_down_2", gp.tileSize, gp.tileSize);
    
    }

    public void setDialogue(){

        dialogues[0] = "Oh, you've found me. I didn't think anyone could\nbreak through the walls to visit. Anyways, I have\nsome items for sale if you're interested.";

    }

    public void setItems(){

        inventory.add(new OBJ_Potion_Red(gp));
        inventory.add(new OBJ_Key(gp));
        inventory.add(new OBJ_Sword_Normal(gp));
        inventory.add(new OBJ_Club_Metal(gp));
        inventory.add(new OBJ_Shield_Normal(gp));
        inventory.add(new OBJ_Shield_Circle(gp));

    }

    public void speak(){

        super.speak();
        gp.gameState = gp.tradeState;
        gp.ui.merchant = this;

    }

    //maybe workshop later
    //do this when you want a character to animate without moving
    public void setAction(){
        animationCanceled = false;
    }

}
