/* SWEN20003 Object Oriented Software Development
 * Space Game Engine
 * Author: Ryan Hodgman <hodgmanr>
 */

public abstract class Item extends Game_Object {  
    /** Gets the player to pick up this item.
     * @param player The player object.
     */
    public abstract void pickUp(Player player);
}