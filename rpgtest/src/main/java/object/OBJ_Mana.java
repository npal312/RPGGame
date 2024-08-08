package object;

import com.example.GamePanel;

import entity.Entity;

public class OBJ_Mana extends Entity{

    GamePanel gp;

    public OBJ_Mana(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_pickup_item;
        name = "Mana";
        value = 2;
        //literally needs to be called or else it doesn't spawn
        down1 = setup("/objects/mana_full");
        image = setup("/objects/mana_full");
        image2 = setup("/objects/mana_blank");

    }

    public void use(Entity entity){

        gp.playSE(2);
        int gained = value;

        if (entity.maxMana > 0){
            gained = entity.checkMax(value, entity.mana, entity.maxMana);
        }

        gp.ui.addMessage("Mana + " + gained);
        entity.mana += gained;

    }

}
