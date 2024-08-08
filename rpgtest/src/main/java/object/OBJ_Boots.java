package object;

import com.example.GamePanel;

import entity.Entity;

public class OBJ_Boots extends Entity{

    public OBJ_Boots(GamePanel gp){

        super(gp);
        name = "Boots";
        //sets image as down1 bc default object direction is down
        down1 = setup("/objects/boots");

    }
    
}
