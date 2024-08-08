package object;

import java.awt.Rectangle;

import com.example.GamePanel;

import entity.Entity;

public class OBJ_Door extends Entity{

    public OBJ_Door(GamePanel gp){

        super(gp);
        name = "Door";
        //sets image as down1 bc default object direction is down
        down1 = setup("/objects/door");
        collision = true;
        description = "[" + name + "]\nHow did you pick\nthis up?";

        //could do solidArea as one line
        solidArea.x = 0;
        solidArea.y = gp.tileSize / 4;
        solidArea.width = gp.tileSize;
        solidArea.height = gp.tileSize * 3 / 4;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

    }

}
