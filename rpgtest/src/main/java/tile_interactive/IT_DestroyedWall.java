package tile_interactive;

import com.example.GamePanel;

import entity.Entity;

public class IT_DestroyedWall extends InteractiveTile{

    GamePanel gp;

    public IT_DestroyedWall(GamePanel gp, int col, int row) {
        super(gp, col, row);
        this.gp = gp;

        down1 = setup("/tiles_interactive/wall_destroyed");

        //no solidArea so no collision so player can walk through
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 0;
        solidArea.height = 0;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

    }

}
