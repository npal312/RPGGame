package object;

import com.example.GamePanel;

import entity.Entity;

public class OBJ_Potion_Red extends Entity{
    
    GamePanel gp;

    public OBJ_Potion_Red(GamePanel gp) {
        super(gp);
        this.gp = gp;
        
        type = type_consumable;
        name = "Red Potion";
        value = 5;
        down1 = setup("/objects/red_potion");
        description = "[" + name + "]\nIt heals " + value + " HP.";
        price = 5;

    }

    public void use(Entity entity){

        gp.gameState = gp.dialogueState;
        int healed = value;

        if (entity.maxLife > 0){
            healed = entity.checkMax(value, entity.life, entity.maxLife);
        }

        gp.ui.currentDialogue = "You drink the " + name + ".\n"
            + "Your life has been healed by " + healed + " HP!";
        entity.life += healed;
        gp.playSE(2);

    }

}
