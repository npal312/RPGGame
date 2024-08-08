package object;

import com.example.GamePanel;

import entity.Entity;

//STRICTLY for refilling Ammo for player
//works same as heart and mana for pickup object stuff
//one ball is used for UI tho
public class OBJ_Balls extends Entity{

    GamePanel gp;

    public OBJ_Balls(GamePanel gp) {
        super(gp);
        this.gp = gp;
        
        type = type_pickup_item;
        name = "Balls";
        value = 4;
        down1 = setup("/objects/balls");

    }

    public void use(Entity entity){

        gp.playSE(2);
        int gained = value;

        if (entity.maxMana > 0){
            gained = entity.checkMax(value, entity.ammo, entity.maxAmmo);
        }

        gp.ui.addMessage("Ammo + " + gained);
        entity.ammo += gained;

    }

}
