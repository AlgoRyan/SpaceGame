/* SWEN20003 Object Oriented Software Development
 * Space Game Engine
 * Author: Ryan Hodgman <hodgmanr>
 */

import java.util.ArrayList;

import org.newdawn.slick.SlickException;

/** The common interface for units that can fire missiles. */
public interface Weaponized_Ship {
	/** Creates and fires a new missile object.
     * @param world The current game state. 
     * @param obj The list of game objects. */
    public abstract void fire(World world, ArrayList<Game_Object> obj) 
    		throws SlickException;
        
	/** Resets the cool-down timer. */
    public abstract void resetTimer();
}