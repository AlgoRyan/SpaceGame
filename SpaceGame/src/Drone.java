/* SWEN20003 Object Oriented Software Development
 * Space Game Engine
 * Author: Ryan Hodgman <hodgmanr>
 */

import java.util.ArrayList;
import org.newdawn.slick.SlickException;

/** The drone class. */
public class Drone extends Enemy {
/* The class variables */
    /** File path of the asteroid's ship image file. */
    private static final String image_file
        = Game.ASSETS_PATH + "/units/drone.png";
    
/* The constructor(s) */
    /** Creates a new drone.
     * @param x The drone's start location (x, pixels).
     * @param y The drone's start location (y, pixels). */
    public Drone(double x, double y)
        throws SlickException {
    	setImage(image_file);
    	setX(x);
        setY(y);
        setSpeed(0.2);
        setShield(16);
        setColDamage(8);
    }
    
/* The class methods */  
	/** Deals with the drone movement pattern.
     * @param world The current game state.
     * @param obj The list of game objects.
     * @param delta Time passed since the last frame in milliseconds. */
    public void update(World world, ArrayList<Game_Object> obj, int delta) {
    	// Runs the collision check function.
    	collision(obj);
    	// Checks that the unit is still alive.
    	if(getShield() <= 0 || hitTerrain(world)) {
    		setExists(false);
    	}
        // Calculate the amount of distance to move, based on speed.
        double amount = delta * getSpeed();
        double dx = 0;
        double dy = 0;
        // Finds the player object to calculate distance from.
        for (int i = 0; i < obj.size(); i++) {
        	if(obj.get(i) instanceof Player) {
                double dist_x = Math.abs(getX() - obj.get(i).getX());
                double dist_y = Math.abs(getY() - obj.get(i).getY());
                double dist_total = Math.sqrt(Math.pow(dist_x, 2) + Math.pow(dist_y, 2));
                dy = ((dist_y / dist_total) * amount);
                // Determine the direction the player is in and calculates the 
                // distance to move.
                if(obj.get(i).getX() < getX()) {
                	dx = -((dist_x / dist_total) * amount);
                } else {
                	dx = (dist_x / dist_total) * amount;
                }
                if(obj.get(i).getY() < getY()) {
                	dy = -((dist_y / dist_total) * amount);
                } else {
                	dy = (dist_y / dist_total) * amount;
                }
        	}
        }
        // The new location.
        double x = getX() + dx;
        double y = getY() + dy;
    	moveTo(world, x, y);
    }
}