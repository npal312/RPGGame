package tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import com.example.GamePanel;
import com.example.UtilityTool;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][][];
    boolean drawPath = true;

    public TileManager(GamePanel gp){

        this.gp = gp;

        //num = enough tiles to fill screen
        tile = new Tile[10];
        mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();

        loadMap("/maps/world01.txt", 0);
        loadMap("/maps/interior01.txt", 1);

    }

    public void getTileImage(){

        //modified instantiation of tiles
        //walkable tile (if collision is false)
        setup(0, "grass", false);
        setup(1, "wall", true);
        setup(2, "water", true);
        setup(3, "earth", false);
        setup(4, "tree", true);
        setup(5, "sand", false);
        setup(6, "houselol", false);
        setup(7, "tablelol", true);
        setup(8, "woodfloor", false);

        //NOTES FOR FULL GAME
        //keep indexes consistent in digit num (all single digits, all double digits, all triple digits, etc.)
        //makes it less confusing and easier to read
        //for all indexes before that num (if starting at 10, for 1-9), put placeholder imageName (same as first one)
        //stops errors from occurring as easily
        
    }

    public void setup(int index, String imageName, boolean collision){

        UtilityTool uTool = new UtilityTool();

        try{

            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;

        } catch(IOException e){
            e.printStackTrace();
        }

    }

    public void loadMap(String filePath, int map){
        try{
            //import text file of map
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gp.maxWorldCol && row < gp.maxWorldRow){
                String line = br.readLine();

                while (col < gp.maxWorldCol){
                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[map][col][row] = num;
                    col++;
                }
                if (col == gp.maxWorldCol){
                    col = 0;
                    row++;
                }
            }
            br.close();

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2){

        int worldCol = 0;
        int worldRow = 0;

        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow){

            int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow];

            //returns tile position
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            //puts tile in correct spot by figuring out where it should be on the screen in reference to the player (both in the world and given that they're in the middle)
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
            
            //ensures only tiles shown on screen get drawn instead of every single tile in the map, even ones offscreen
            //put drawImage back on top of worldCol++ to reverse this
            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && 
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY){
                //get rid of scale bc pre-scaled
                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }

            worldCol++;

            if (worldCol == gp.maxWorldCol){
                worldCol = 0;
                worldRow++;
            }
        }

        if (drawPath == true){
            g2.setColor(new Color(255, 0, 0, 70));

            for (int i = 0; i < gp.pFinder.pathList.size(); i++){

                int worldX = gp.pFinder.pathList.get(i).col * gp.tileSize;
                int worldY = gp.pFinder.pathList.get(i).row * gp.tileSize;
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;

                g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);

            }
        }
    }
}
