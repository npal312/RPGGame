package object;

import com.example.GamePanel;

import entity.Entity;

public class OBJ_Club_Metal extends Entity{

    public OBJ_Club_Metal(GamePanel gp) {
        super(gp);
        
        type = type_club;
        name = "Metal Club";
        down1 = setup("/objects/club_metal");
        attackValue = 2;
        attackArea.width = 40;
        attackArea.height = 50;
        description = "[" + name + "]\nLooks like it hurts.";
        price = 10;
        knockbackPower = 10;
        
    }

}
