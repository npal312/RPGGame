package com.example;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ai.PathFinder;
import entity.Entity;
import entity.Player;
import tile.TileManager;
import tile_interactive.InteractiveTile;

public class GamePanel extends JPanel implements Runnable{

    //SCREEN SETTINGS
    //sets panel size for game
    final int originalTileSize = 16;

    //for scaling the 16x16 tiles;
    final int scale = 4;

    //64x64 tiles if scale == 4
    //public so all classes can access
    public final int tileSize = originalTileSize * scale;

    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; //1280
    public final int screenHeight = tileSize * maxScreenRow; //768


    //WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int maxMap = 10;
    public int currentMap = 0;
    //FOR FULL SCREEN
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;
    public boolean fullScreenOn = false;

    //FPS
    int FPS = 60;

    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);

    //SOUND
    //music and sound effects are different
    Sound music = new Sound();
    Sound soundEffect = new Sound();

    //for checking collision
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);

    //UI Instantiation
    public UI ui = new UI(this);

    //Event Handling
    public EventHandler eHandler = new EventHandler(this);

    //Config
    Config config = new Config(this);

    //PathFinder
    public PathFinder pFinder = new PathFinder(this);

    //for letting program continue until closed
    Thread gameThread;


    //ENTITY AND OBJECT
    public Player player = new Player(this, keyH);
    //10 object slots (but 10 at a time, not 10 total)
    //can increase but might not need to
    public Entity obj[][] = new Entity[maxMap][40];
    public Entity npc[][] = new Entity[maxMap][10];
    public Entity monster[][] = new Entity[maxMap][20];
    public InteractiveTile iTile[][] = new InteractiveTile[maxMap][50];
    //I LOVE ArrayLists
    //genuinely happy to use them
    //draw entities in order of their worldY value (to stop overlapping)
    ArrayList<Entity> entityList = new ArrayList<>();
    //for parrying projectiles and stuff I'm commenting the original out
    public Entity projectile[][] = new Entity[maxMap][20];
    //public ArrayList<Entity> projectileList = new ArrayList<>();
    public ArrayList<Entity> particleList = new ArrayList<>();

    //GAME STATE
    //OHHHH GAME STATES CONTROL WHAT GETS DRAWN ON SCREEN AND CHANGES HOW INPUTS GET RECEIVED
    //duh but it's nice to hear it in that manner
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int optionsState = 5;
    public final int gameOverState = 6;
    public final int transitionState = 7;
    public final int tradeState = 8;



    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        //for recognizing key inputs
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame(){

        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        aSetter.setInteractiveTile();

        //starts background music
        //playMusic(0);
        gameState = titleState;

        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        //DON'T CONFUSE THIS g2 WITH THE NORMAL ONE (somehow)
        g2 = (Graphics2D)tempScreen.getGraphics();
        //everything this g2 draws will be recorded to tempScreen
        //now instead of directly drawing on JPanel (onscreen), draw to tempScreen and then to JPanel from there
        //this allows us to change resolution to whatever we want
        
        if (fullScreenOn == true){
            setFullScreen();
        }
    }

    //Retrying keeps level, items, progress, etc.
    //Resets positions, life/mana, monsters/npc's
    public void retry(){

        //keeps objects, tiles, and item statuses the same
        //just resets npc and monster positions
        player.setDefaultPositions();
        player.restoreLifeManaAmmo();
        aSetter.setNPC();
        aSetter.setMonster();

    }

    //Quitting keeps nothing
    public void restart(){

        //reset everything omg
        player.setDefaultValues();
        player.setDefaultPositions();
        player.restoreLifeManaAmmo();
        player.setItems();
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        aSetter.setInteractiveTile();

    }

    public void setFullScreen() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		Main.window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		screenWidth2 = (int) width;
		screenHeight2 = (int) height;
	}


    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start(); //to call run()
    }

    @Override
    public void run() {

        //delta system used to check time and put delay between update and repaint
        //makes it so it doesn't loop thousands of times per second
        //interval between drawing screens based on FPS
        double drawInterval = 1000000000/FPS; //0.01667 seconds at 60 FPS
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(gameThread != null){

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);

            lastTime = currentTime;

            if (delta >= 1){
                // 1 UPDATE: update information, like character positions
                update();
                // 2 DRAW: draw the screen with the updated information
                drawToTempScreen(); //draw everything to tempScreen
                drawToScreen(); //draw the BufferedImage to the screen
                delta--;
                drawCount++;
            }
            
            if (timer >= 1000000000){
                //to print FPS (I think this whole timer, draw count, and if statement are for that)
                //System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
            
        }
        /*long lastTime = System.nanoTime();
        long currentTime;
        double delta = 0;
        double updateInterval = 1.0 / FPS;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / 1000000000.0; // Convert nanoseconds to seconds
            lastTime = currentTime;

            while (delta >= updateInterval) {
                // Update
                update();
                delta -= updateInterval;
            }

            // Draw
            drawToTempScreen(); // Draw game into buffered image
            drawToScreen(); // Draw buffered image to screen
            drawCount++;

            if (keyH.showDebugText && (System.nanoTime() - timer) >= 1000000000) {
                FPS = drawCount;
                drawCount = 0;
                timer = System.nanoTime();
            }
        }*/
    }

    public void update(){
        
        if (gameState == playState){
            //PLAYER
            player.update();
            //NPC
            //1 is for using 2nd dimension's length
            for (int i = 0; i < npc[1].length; i++){
                if (npc[currentMap][i] != null){
                    npc[currentMap][i].update();
                }
            }
            for (int i = 0; i < monster[1].length; i++){
                if (monster[currentMap][i] != null){
                    if (monster[currentMap][i].alive == true && monster[currentMap][i].dying == false){
                        monster[currentMap][i].update();
                    }
                    if (monster[currentMap][i].alive == false){
                        monster[currentMap][i].checkDrop();
                        monster[currentMap][i] = null;
                    }
                }
            }
            for (int i = 0; i < projectile[1].length; i++){
                if (projectile[currentMap][i] != null){
                    if (projectile[currentMap][i].alive == true){
                        projectile[currentMap][i].update();
                    }
                    if (projectile[currentMap][i].alive == false){
                        projectile[currentMap][i] = null;
                    }
                }
            }
            /*for (int i = 0; i < projectileList.size(); i++){
                if (projectileList.get(i) != null){
                    if (projectileList.get(i).alive == true){
                        projectileList.get(i).update();
                    }
                    if (projectileList.get(i).alive == false){
                        projectileList.remove(i);
                    }
                }
            }*/
            for (int i = 0; i < particleList.size(); i++){
                if (particleList.get(i) != null){
                    if (particleList.get(i).alive == true){
                        particleList.get(i).update();
                    }
                    if (particleList.get(i).alive == false){
                        particleList.remove(i);
                    }
                }
            }
            for (int i = 0; i < iTile[1].length; i++){
                if (iTile[currentMap][i] != null){
                    iTile[currentMap][i].update();
                }
            }
        }
        if (gameState == pauseState){
            //do nothing
        }
        
    }

    public void drawToTempScreen(){
        //DEBUG
        long drawStart = 0;
        if (keyH.showDebugText == true){
            drawStart = System.nanoTime();
        }

        //TITLE SCREEN
        if (gameState == titleState){

            ui.draw(g2);

        }
        //OTHERS
        else{
            //draw first bc it's background layer
            //TILE
            tileM.draw(g2);

            //INTERACTIVE TILES
            for (int i = 0; i < iTile[1].length; i++){
                if (iTile[currentMap][i] != null){
                    iTile[currentMap][i].draw(g2);
                }
            }

            //ADD ENTITIES TO THE LIST
            entityList.add(player);

            for (int i = 0; i < npc[1].length; i++){
                if(npc[currentMap][i] != null){
                    entityList.add(npc[currentMap][i]);
                }
            }

            for (int i = 0; i < obj[1].length; i++){
                if(obj[currentMap][i] != null){
                    entityList.add(obj[currentMap][i]);
                }
            }

            for (int i = 0; i < monster[1].length; i++){
                if(monster[currentMap][i] != null){
                    entityList.add(monster[currentMap][i]);
                }
            }

            for (int i = 0; i < projectile[1].length; i++){
                if(projectile[currentMap][i] != null){
                    entityList.add(projectile[currentMap][i]);
                }
            }
            /*for (int i = 0; i < projectileList.size(); i++){
                if(projectileList.get(i) != null){
                    entityList.add(projectileList.get(i));
                }
            }*/

            for (int i = 0; i < particleList.size(); i++){
                if(particleList.get(i) != null){
                    entityList.add(particleList.get(i));
                }
            }

            //SORT
            Collections.sort(entityList, new Comparator<Entity>(){
                
                //to compare entities (will give error if not included)
                //and MAY not tell you to do it lol
                @Override
                public int compare(Entity e1, Entity e2){
                    int result = Integer.compare(e1.worldY, e2.worldY);
                    return result;
                }

            });

            //DRAW ENTITIES
            for (int i = 0; i < entityList.size(); i++){
                entityList.get(i).draw(g2);
            }

            //EMPTY ENTITY LIST
            entityList.clear();
            
            //UI (on top)
            ui.draw(g2);

        }

        //DEBUG
        if (keyH.showDebugText == true){
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.white);
            int x = 10;
            int y = 400;
            int lineHeight = 20;

            g2.drawString("WorldX: " + player.worldX, x, y);
            y += lineHeight;
            g2.drawString("WorldY: " + player.worldY, x, y);
            y += lineHeight;
            g2.drawString("Col: " + (player.worldX + player.solidArea.x) / tileSize, x, y);
            y += lineHeight;
            g2.drawString("Row: " + (player.worldY + player.solidArea.y) / tileSize, x, y);
            y += lineHeight;
            g2.drawString("Draw Time: " + passed, x, y);
            y += lineHeight;
            g2.drawString("entities drawn:", x, y);
            //DEBUG FURTHER
            /*y += lineHeight;
            String names = "";
            for (int i = 0; i < projectileList.size(); i++){
                if (projectileList.get(i) != null){
                    names += projectileList.get(i).name + ", ";
                }
            }
            for (int i = 0; i < entityList.size(); i++){
                if (entityList.get(i) != null){
                    names += entityList.get(i).name + ", ";
                }
            }*/
            /*for (int i = 0; i < monster[1].length; i++){
                if (monster[i] != null){
                    names += monster[i].name + ", ";
                }
            }*/
            /*for (int i = 0; i < npc[1].length; i++){
                if (npc[i] != null){
                    names += npc[i].name + ", ";
                }
            }
            g2.drawString(names, x, y);
            names = "";*/

        }
    }

    public void drawToScreen(){

        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();

    }

    public void playMusic(int i){

        music.setFile(i);
        //BAREBONES MUTE FUNCTION (UPDATE)
        if (!keyH.muteAudio){
            music.play();
            music.loop();
        }

    }

    public void resumeMusic(int i){

        if (!keyH.muteAudio){
            music.play();
            music.loop();
        }

    }

    public void stopMusic(){

        music.stop();

    }

    public void playSE(int i){

        soundEffect.setFile(i);
        if (!keyH.muteAudio){
            soundEffect.play();
        }

    }

    public void stopSE(){

        soundEffect.stop();

    }

}