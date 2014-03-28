/* SWEN20003 Object Oriented Software Development
 * Space Game Engine
 * Author: Ryan Hodgman <hodgmanr>
 */

import java.util.ArrayList;

import org.newdawn.slick.SlickException;

/** Contains the common attributes of the enemy units.  */
public abstract class Enemy extends Unit {
/* The class methods */  
	/** Deals with enemy movement.
     * @param world The current game state.
     * @param obj The list of game objects.
     * @param delta Time passed since the last frame in milliseconds. */
    public abstract void update(World world, ArrayList<Game_Object> obj, int delta) 
    throws SlickException;
}