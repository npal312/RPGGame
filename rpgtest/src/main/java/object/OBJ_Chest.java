package object;

import com.example.GamePanel;

import entity.Entity;

public class OBJ_Chest extends Entity{

    public OBJ_Chest(GamePanel gp){

        super(gp);
        name = "Chest";
        //sets image as down1 bc default object direction is down
        down1 = setup("/objects/chest");

    }

}