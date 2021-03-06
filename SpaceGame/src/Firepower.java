/* SWEN20003 Object Oriented Software Development
 * Space Game Engine
 * Author: Ryan Hodgman <hodgmanr>
 */

import org.newdawn.slick.SlickException;

/** The fire-power item. */
public class Firepower extends Item { 
/* The class variables */
    /** File path of the repair item image file. */
    private static final String image_file
        = Game.ASSETS_PATH + "/items/firepower.png";
    
/* The constructor(s) */
    /** Creates a new repair item.
     * @param x The item's start location (x, pixels).
     * @param y The item's start location (y, pixels). */
    public Firepower(double x, double y)
            throws SlickException {
          	setImage(image_file);
            setX(x);
            setY(y);
    }
    
/* The class methods */
    /** Checks if the item is within the bounds of the camera in any given frame.
     * @param cam The camera used to render the current screen. */
    public void update(Camera cam) {
    	
    }
    
    /** Get the player to pick up this item.
     * @param player The player object. */
    public void pickUp(Player player) {
    	if(player.getFirepower() < 3) {
    		player.setFirepower(player.getFirepower() + 1);
    	}
    	setExists(false);
    }
}