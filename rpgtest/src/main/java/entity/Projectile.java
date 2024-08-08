package entity;

import com.example.GamePanel;

public class Projectile extends Entity{

    Entity user;

    public Projectile(GamePanel gp) {
        super(gp);
    }

    //Entity parameter for if you want other entity types to shoot projectiles
    public void set(int worldX, int worldY, String direction, boolean alive, Entity user){
        
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.life = this.maxLife;

    }

    public void update(){

        if (user == gp.player){
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            if (monsterIndex != 999){
                gp.player.damageMonster(monsterIndex, attack, knockbackPower);
                generateParticle(user.projectile, gp.monster[gp.currentMap][monsterIndex]);
                alive = false;
            }
        }
        if (user != gp.player){
            boolean contactPlayer = gp.cChecker.checkPlayer(this);
            if (gp.player.invincible == false && contactPlayer == true){
                damagePlayer(attack);
                generateParticle(user.projectile, gp.player);
                alive = false;
            }
        }

        switch(direction){
            case "up": worldY -= speed; animationCanceled = false; break;
            case "down": worldY += speed; animationCanceled = false; break;
            case "left": worldX -= speed; animationCanceled = false; break;
            case "right": worldX += speed; animationCanceled = false; break;
        }

        //life determines frames it's alive for
        life--;
        if (life <= 0){
            alive = false;
        }

        spriteCounter++;
        if (spriteCounter > 30){
            if (spriteNum == 1){
                spriteNum = 2;
            }
            else if (spriteNum == 2){
                spriteNum = 1;
            }
        }
        
    }

    //always override, just here to appease Java gods
    public boolean haveResource(Entity user){return false;}

    public void subtractResource(Entity user){}

}
