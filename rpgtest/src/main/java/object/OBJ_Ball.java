package object;

import java.awt.Color;

import com.example.GamePanel;

import entity.Entity;
import entity.Projectile;

//Okay so projectiles cannot be item pickups
public class OBJ_Ball extends Projectile{

    GamePanel gp;

    public OBJ_Ball(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_range_phys;
        name = "Ball";
        speed = 12;
        maxLife = 80;
        life = maxLife;
        attack = 2;
        useCost = 1;
        alive = false;
        getImage();

    }

    public void getImage(){

        //overloaded function, calling with just imagepath auto sets width and height to tileSize
        up1 = setup("/projectile/ball_down");
        up2 = setup("/projectile/ball_down");
        down1 = setup("/projectile/ball_down");
        down2 = setup("/projectile/ball_down");
        left1 = setup("/projectile/ball_down");
        left2 = setup("/projectile/ball_down");
        right1 = setup("/projectile/ball_down");
        right2 = setup("/projectile/ball_down");

        image = setup("/objects/ball_full");
        image2 = setup("/objects/ball_blank");

    }

    public boolean haveResource(Entity user){

        if (user.ammo >= useCost){
            return true;
        }
        else{
            return false;
        }

    }

    public void subtractResource(Entity user){
        user.ammo -= useCost;
    }

    public Color getParticleColor(){
        return new Color(134, 131, 122);
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
