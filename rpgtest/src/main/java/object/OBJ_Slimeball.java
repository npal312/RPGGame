package object;

import java.awt.Color;

import com.example.GamePanel;

import entity.Entity;
import entity.Projectile;

public class OBJ_Slimeball extends Projectile{ //subclass of projectile which is subclass of entity

    GamePanel gp;

    public OBJ_Slimeball(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_range_mana;
        name = "Slimeball";
        speed = 5;
        maxLife = 80;
        life = maxLife;
        attack = 4;
        useCost = 1;
        alive = false;
        getImage();

    }

    public void getImage(){

        //overloaded function, calling with just imagepath auto sets width and height to tileSize
        up1 = setup("/projectile/slimeball_up_1");
        up2 = setup("/projectile/slimeball_up_2");
        down1 = setup("/projectile/slimeball_down_1");
        down2 = setup("/projectile/slimeball_down_2");
        left1 = setup("/projectile/slimeball_left_1");
        left2 = setup("/projectile/slimeball_left_2");
        right1 = setup("/projectile/slimeball_right_1");
        right2 = setup("/projectile/slimeball_right_2");

    }

    public boolean haveResource(Entity user){

        if (user.mana >= useCost){
            return true;
        }
        else{
            return false;
        }

    }

    public void subtractResource(Entity user){
        user.mana -= useCost;
    }

    public Color getParticleColor(){
        return gp.ui.outerSlime;
    }

    public int getParticleSize(){
        int size = 10; //in pixels
        return size;
    }

    public int getParticleSpeed(){
        int speed = 1;
        return speed;
    }

    public int getParticleMaxLife(){
        int maxLife = 20;
        return maxLife;
    }
}
