/* SWEN20003 Object Oriented Software Development
 * Space Game Engine
 * Author: Ryan Hodgman <hodgmanr>
 */

import java.util.ArrayList;
import org.newdawn.slick.SlickException;

/** The fighter class.  */
public class Fighter extends Enemy implements Weaponized_Ship {
/* The class variables */
    /** File path of the fighter's ship image file. */
    private static final String image_file
        = Game.ASSETS_PATH + "/units/fighter.png";
    
    /** Measure of the fighter's power level (used to calculate cool-down). */
    private int firepower = 0;
    
    /** Measure of how fast the fighter may fire missiles. */
    private int cooldown;
    
/* The constructor(s) */
    /** Creates a new fighter.
     * @param x The fighter's start location (x, pixels).
     * @param y The fighter's start location (y, pixels). */
    public Fighter(double x, double y)
        throws SlickException {
    	setImage(image_file);
    	setX(x);
        setY(y);
        setSpeed(0.2);
        setShield(24);
        setColDamage(9);
    }
    
/* The class methods */  
	/** Deals with the fighter movement pattern.
     * @param world The current game state.
     * @param obj The list of game objects.
     * @param delta Time passed since the last frame in milliseconds. 
	 * @throws SlickException */
    public void update(World world, ArrayList<Game_Object> obj, int delta) 
    throws SlickException {
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
    	// Fires a missile if able.
        cooldown = cooldown - delta;
        if(cooldown <= 0) {
        	fire(world, obj);
        	resetTimer();
        }
    }
    
    /** Creates and fires a new enemy missile.
     * @param world The current game state. 
     * @param obj The list of game objects. */
    public void fire(World world, ArrayList<Game_Object> obj) 
    throws SlickException {
    	Missile missile = new Missile(getX(), getY(), "enemy");
    	obj.add(missile);
    }
        
	/** Resets the cool-down timer. */
    public void resetTimer() {
        cooldown = 300 - (80 * firepower);
    }    
}