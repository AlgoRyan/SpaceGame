/* SWEN20003 Object Oriented Software Development
 * Space Game Engine
 * Author: Ryan Hodgman <hodgmanr>
 */

import java.util.ArrayList;
import org.newdawn.slick.SlickException;

/** The asteroid class. */
public class Asteroid extends Enemy {
/* The class variables */
    /** File path of the asteroid's ship image file. */
    private static final String image_file
        = Game.ASSETS_PATH + "/units/asteroid.png";
    
/* The constructor(s) */
    /** Creates a new asteroid.
     * @param x The asteroid's start location (x, pixels).
     * @param y The asteroid's start location (y, pixels). */
    public Asteroid(double x, double y)
        throws SlickException {
    	setImage(image_file);
    	setX(x);
        setY(y);
        setSpeed(0.2);
        setShield(24);
        setColDamage(12);
    }
    
/* The class methods */  
	/** Deals with the asteroid movement pattern.
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
        // The new location.
        double y = getY() + amount;
    	moveTo(world, getX(), y);
    }
}