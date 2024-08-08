package tile_interactive;

import java.awt.Graphics2D;

import com.example.GamePanel;

import entity.Entity;

public class InteractiveTile extends Entity{

    GamePanel gp;
    public boolean destructible = false;

    public InteractiveTile(GamePanel gp, int col, int row) {
        super(gp);
        this.gp = gp;

        this.worldX = gp.tileSize * col;
        this.worldY = gp.tileSize * row;
    }

    //placeholder (do not be alarmed)
    public boolean isCorrectItem(Entity entity){
        return false;
    }

    public void playSE(){}

    public InteractiveTile getDestroyedForm(){
        InteractiveTile tile = null;
        return tile;
    }

    public void update(){

        if (invincible == true){
            invincibleCounter++;
            if (invincibleCounter > 20){
                invincible = false;
                invincibleCounter = 0;
            }
        }

    }

    public void draw(Graphics2D g2){
        //puts object in correct spot by figuring out where it should be on the screen in reference to the player (both in the world and given that they're in the middle)
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        
        //ensures only objects shown on screen get drawn instead of every single object in the map, even ones offscreen
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && 
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY){
            
            g2.drawImage(down1, screenX, screenY, null);

        }
    }

}
