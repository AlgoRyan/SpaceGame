/* SWEN20003 Object Oriented Software Development
 * Space Game Engine (Sample Project)
 * Author: Ryan Hodgman <hodgmanr>
 */

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/** Represents the entire game world.
 * (Designed to be instantiated just once for the whole game). */
public class World {
/* The class variables */
    /** The number of game objects to be read in from the data file. */
	public static final int NUM_OBJ = 255;
	
    /** The map containing the terrain tiles. */
    private TiledMap map;
    
    /** The camera. */
    private Camera camera;
    
    /** The status panel. */
    private Panel panel;
    
    /** The player, for rendering purposes only. (Note: Restricts to single player) */
    private Player blank_player = new Player(0, 0);
    
    /** The ArrayList containing all of the game objects. */
    private ArrayList<Game_Object> object_list;
    
/* The constructor(s) */
    /** Create a new world object. */
    public World()
    throws SlickException, FileNotFoundException {
        map = new TiledMap(Game.ASSETS_PATH + "/map.tmx", Game.ASSETS_PATH);
        panel = new Panel();
        // Create and fill two arrays with the data about the initial
        // game objects from a data file. The data file must
        // be in the format "object_type x_coord y_coord/n".
        String[] type = new String[NUM_OBJ];
        double[][] coord = new double[NUM_OBJ][2];
        // Scan the data file.
        Scanner file_scanner = new Scanner(new File(Game.DATA_PATH +"/game_objects_initial.txt"));
        for (int i = 0; i < NUM_OBJ - 1; i++) {
            String line = file_scanner.nextLine();
            String[] temp = line.split(" ");
            type[i] = temp[0];
            coord[i][0] = Double.parseDouble(temp[1]);
            coord[i][1] = Double.parseDouble(temp[2]);
        }
        // Create and fill a new ArrayList with the required game objects.
        object_list = new ArrayList<Game_Object>(NUM_OBJ);
        for (int i = 0; i < NUM_OBJ - 1; i++) {
        	if(type[i].equals("player")) {
        		Player player = new Player(coord[i][0], coord[i][1]);
        		object_list.add(i, player);
                // Create a camera, centered and with the player at the bottom.
                camera = new Camera(this, player);
        	}
        	if(type[i].equals("fighter")) {
        		Fighter fighter = new Fighter(coord[i][0], coord[i][1]);
        		object_list.add(i, fighter);
        	}
        	if(type[i].equals("drone")) {
        		Drone drone = new Drone(coord[i][0], coord[i][1]);
        		object_list.add(i, drone);
        	}
        	if(type[i].equals("asteroid")) {
        		Asteroid asteroid = new Asteroid(coord[i][0], coord[i][1]);
        		object_list.add(i, asteroid);
        	}
        	if(type[i].equals("boss")) {
        		Boss boss = new Boss(coord[i][0], coord[i][1]);
        		object_list.add(i, boss);
        	}
        	if(type[i].equals("repair")) {
        		Repair repair = new Repair(coord[i][0], coord[i][1]);
        		object_list.add(i, repair);
        	}
        	if(type[i].equals("firepower")) {
        		Firepower firepower = new Firepower(coord[i][0], coord[i][1]);
        		object_list.add(i, firepower);
        	}
        	if(type[i].equals("shield")) {
        		Shield shield = new Shield(coord[i][0], coord[i][1]);
        		object_list.add(i, shield);
        	}
        }
    }

/* The getter and setter methods */
    /** Get the width of the game world in pixels. */
    public int getWidth() {
        return map.getWidth() * map.getTileWidth();
    }

    /** Get the height of the game world in pixels. */
    public int getHeight() {
        return map.getHeight() * map.getTileHeight();
    }
    
    /** Get the width of the screen in tiles, rounding up.
     * For a width of 800 pixels and a tile width of 72, this is 12. */
    private int getScreenTileWidth() {
        return (Game.screenwidth / map.getTileWidth()) + 1;
    }

    /** Get the height of the screen in tiles, rounding up.
     * For a height of 600 pixels and a tile height of 72, this is 9. */
    private int getScreenTileHeight() {
        return (Game.screenheight / map.getTileHeight()) + 1;
    }
    
/* The class methods */
    /** True if the camera has reached the top of the map. */
    public boolean reachedTop() {
        return camera.getTop() <= 0;
    }

    /** Update the game state for a frame.
     * @param dir_x The player's movement in the x axis (-1, 0 or 1).
     * @param dir_y The player's movement in the y axis (-1, 0 or 1).
     * @param fire Whether or not the player should fire a missile.
     * @param delta Time passed since last frame (milliseconds). */
    public void update(double dir_x, double dir_y, Boolean fire, int delta)
    throws SlickException {
        // Move the camera automatically.
        camera.update(delta);
        object_list.trimToSize();
        for (int i = 0; i < object_list.size(); i++) {
            // Checks that all game objects in the array still exist.
       		if(!object_list.get(i).getExists()) {
       			object_list.remove(i);
            	if(i >= object_list.size()) {
            		i--;
            	}
       		}
            // Update missiles automatically based upon their AI pattern.
        	if(object_list.get(i) instanceof Missile) {
        		Missile missile = (Missile) object_list.get(i);
           		missile.update(this, camera, object_list, delta);
           		object_list.set(i, missile);
       		}
            // Move any players automatically, and manually (by dir_x, dir_y).
        	if(object_list.get(i) instanceof Player) {
        		blank_player = (Player) object_list.get(i);
        		blank_player.update(this, object_list, camera, panel, 
        				dir_x, dir_y, fire, delta);
        	    // Center the camera (in x-axis) over the player and bound to map.
        	    camera.followUnit(blank_player);
        		object_list.set(i, blank_player);
        	}
            // Update enemies automatically based upon their AI pattern.
        	if(object_list.get(i) instanceof Enemy) {
        		Enemy enemy = (Enemy) object_list.get(i);
        		if(enemy.onScreen(camera)) {
        			enemy.update(this, object_list, delta);
           			object_list.set(i, enemy);
        		}
       		}
        }
    }

    /** Render the entire screen, so it reflects the current game state.
     * @param g The Slick graphics object, used for drawing.
     * @param textrenderer A TextRenderer object. */
    public void render(Graphics g)
    throws SlickException {
        // Calculate the camera location (in tiles) and offset (in pixels)
        int cam_tile_x = (int) camera.getLeft() / map.getTileWidth();
        int cam_offset_x = (int) camera.getLeft() % map.getTileWidth();
        int cam_tile_y = (int) camera.getTop() / map.getTileHeight();
        int cam_offset_y = (int) camera.getTop() % map.getTileHeight();
        // Render w+1 x h+1 tiles (where w and h are 12x9; add one tile extra
        // to account for the negative offset).
        map.render(-cam_offset_x, -cam_offset_y, cam_tile_x, cam_tile_y,
            getScreenTileWidth()+1, getScreenTileHeight()+1);
    	// Render all of the game objects.
        for (int i = 0; i < object_list.size(); i++) {
        	object_list.get(i).render(g, camera);
        }
        // Render the panel.
        panel.render(g, blank_player.getShield(), blank_player.getMaxShield(), 
        		blank_player.getFirepower());
    }

    /** Determines whether a particular map location blocks movement due to
     * terrain.
     * @param position[] A two-dimensional array that holds x and y coordinates.
     * @return true if the location blocks movement due to terrain. */
    public boolean terrainBlocks(double[] position) {
        int tile_x = (int) position[0] / map.getTileWidth();
        int tile_y = (int) position[1] / map.getTileHeight();
        // Check if the location is off the map. If so, assume it doesn't
        // block (potentially allowing ships to fly off the map).
        if (tile_x < 0 || tile_x >= map.getWidth()
            || tile_y < 0 || tile_y >= map.getHeight())
            return false;
        // Get the tile ID and check whether it blocks movement.
        int tileid = map.getTileId(tile_x, tile_y, 0);
        String block = map.getTileProperty(tileid, "block", "0");
        return Integer.parseInt(block) != 0;
    }
}
