package object;

import com.example.GamePanel;

import entity.Entity;

public class OBJ_Heart extends Entity{

    GamePanel gp;

    public OBJ_Heart(GamePanel gp){
        super(gp);
        this.gp = gp;

        type = type_pickup_item;
        name = "Heart";
        value = 2;
        down1 = setup("/objects/heart_full");
        //sets image as down1 bc default object direction is down
        image = setup("/objects/heart_full");
        image2 = setup("/objects/heart_half");
        image3 = setup("/objects/heart_blank");

    }

    public void use(Entity entity){

        gp.playSE(2);
        int healed = value;

        if (entity.maxLife > 0){
            healed = entity.checkMax(value, entity.life, entity.maxLife);
        }

        gp.ui.addMessage("Life + " + healed);
        entity.life += healed;

    }

}
