/* SWEN20003 Object Oriented Software Development
 * Space Game Engine
 * Author: Ryan Hodgman <hodgmanr>
 */

import org.newdawn.slick.SlickException;

/** The shield item. */
public class Shield extends Item { 
/* The class variables */
    /** File path of the repair item image file. */
    private static final String image_file
        = Game.ASSETS_PATH + "/items/shield.png";
    
/* The constructor(s) */
    /** Creates a new repair item.
     * @param x The item's start location (x, pixels).
     * @param y The item's start location (y, pixels). */
    public Shield(double x, double y)
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
    	player.setMaxShield(player.getMaxShield() + 40);
    	player.setShield(player.getMaxShield());
    	setExists(false);
    }
}