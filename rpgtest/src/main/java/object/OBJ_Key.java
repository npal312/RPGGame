package object;

import com.example.GamePanel;

import entity.Entity;

public class OBJ_Key extends Entity{

    public OBJ_Key(GamePanel gp){

        super(gp);
        name = "Key";
        //sets image as down1 bc default object direction is down
        down1 = setup("/objects/key");
        description = "[" + name + "]\nIt opens a door.";
        price = 20;

    }

}
