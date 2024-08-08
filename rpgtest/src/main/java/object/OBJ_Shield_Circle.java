package object;

import com.example.GamePanel;

import entity.Entity;

public class OBJ_Shield_Circle extends Entity{

    public OBJ_Shield_Circle(GamePanel gp) {
        super(gp);
        
        type = type_shield;
        name = "Circle Shield";
        down1 = setup("/objects/shield_circle", gp.tileSize, gp.tileSize);
        defenseValue = 2;
        description = "[" + name + "]\nOooo circle.";
        price = 10;
        
    }

}
