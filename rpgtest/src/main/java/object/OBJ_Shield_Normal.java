package object;

import com.example.GamePanel;

import entity.Entity;

public class OBJ_Shield_Normal extends Entity{

    public OBJ_Shield_Normal(GamePanel gp) {
        super(gp);
        
        type = type_shield;
        name = "Normal Shield";
        down1 = setup("/objects/shield_normal", gp.tileSize, gp.tileSize);
        defenseValue = 1;
        description = "[" + name + "]\nAn old shield.";
        price = 5;
        
    }

}
