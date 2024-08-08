package tile_interactive;

import java.awt.Color;

import com.example.GamePanel;

import entity.Entity;

public class IT_CrackedWall extends InteractiveTile{

    GamePanel gp;

    public IT_CrackedWall(GamePanel gp, int col, int row) {
        super(gp, col, row);
        this.gp = gp;

        down1 = setup("/tiles_interactive/wall_cracked");
        destructible = true;
        life = 3;

    }

    public boolean isCorrectItem(Entity entity){
        if (entity.currentWeapon.type == type_club){
            return true;
        }
        else{
            return false;
        }
    }

    public void playSE(){
        gp.playSE(11);
    }

    public InteractiveTile getDestroyedForm(){
        return new IT_DestroyedWall(gp, worldX / gp.tileSize, worldY / gp.tileSize);
    }

    public Color getParticleColor(){
        return new Color(43, 44, 43);
    }

    public int getParticleSize(){
        int size = 6; //in pixels
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
