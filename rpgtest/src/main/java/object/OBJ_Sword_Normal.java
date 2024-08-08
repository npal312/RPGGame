package object;

import com.example.GamePanel;

import entity.Entity;

public class OBJ_Sword_Normal extends Entity{

    public OBJ_Sword_Normal(GamePanel gp) {
        super(gp);
        
        type = type_sword;
        name = "Normal Sword";
        down1 = setup("/objects/sword_normal", gp.tileSize, gp.tileSize);
        attackValue = 1;
        attackArea.width = 48;
        attackArea.height = 64;
        description = "[" + name + "]\nAn old sword.";
        price = 5;
        knockbackPower = 3;
        
    }

}
