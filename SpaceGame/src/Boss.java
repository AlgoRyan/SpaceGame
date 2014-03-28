/* SWEN20003 Object Oriented Software Development
 * Space Game Engine
 * Author: Ryan Hodgman <hodgmanr>
 */

import java.util.ArrayList;
import org.newdawn.slick.SlickException;

/** The boss class. */
public class Boss extends Enemy implements Weaponized_Ship {
/* The class variables */
    /** File path of the boss's ship image file. */
    private static final String image_file
        = Game.ASSETS_PATH + "/units/boss.png";
    
    /** Measure of the boss's power level (used to calculate cool-down). */
    private int firepower = 3;
    
    /** Measure of how fast the boss may fire missiles. */
    private int cooldown;
    
    /** Direction the boss is moving in. */
    private int dir = 1;
    
/* The constructor(s) */
    /** Creates a new boss.
     * @param x The boss's start location (x, pixels).
     * @param y The boss's start location (y, pixels). */
    public Boss(double x, double y)
        throws SlickException {
    	setImage(image_file);
    	setX(x);
        setY(y);
        setSpeed(0.2);
        setShield(240);
        setColDamage(100);
    }
    
/* The class methods */  
	/** Deals with the boss movement pattern.
     * @param world The current game state.
     * @param obj The list of game objects.
     * @param delta Time passed since the last frame in milliseconds. */
    public void update(World world, ArrayList<Game_Object> obj, int delta) 
    throws SlickException {
    	// Runs the collision check function.
    	collision(obj);
    	// Checks that the unit is still alive.
    	if(getShield() <= 0) {
    		setExists(false);
    	}        
    	// Calculate the amount of distance to move, based on speed.
        double amount = delta * getSpeed();
        // The new location.
        double x = getX();
        if(x <= 1115) {
        	dir = 1;
        }
        if(x >= 1490) {
        	dir = -1;
        }
        x = x + dir * amount;
    	moveTo(world, x, getY());
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