/* SWEN20003 Object Oriented Software Development
 * Space Game Engine
 * Author: Ryan Hodgman <hodgmanr>
 */

import java.util.ArrayList;

/** Contains the common attributes of the player and the enemies.  */
public class Unit extends Game_Object {
/* The class variables */
    /** Current shield level of the unit. */
    private int shield;
    
    /** Current shield capacity of the unit. */
    private int max_shield;
    
    /** Amount of damage the unit deals upon colliding with another unit. */
    private int collision_damage;
    
    /** Current speed of the unit (pixels per frame). */
    private double speed;
    
/* The getter and setter methods */
    /** Returns the shield level of the unit. */
    public int getShield() {
        return shield;
    }
    
    /** Sets the shield level of the unit. */
    public void setShield(int shield) {
        this.shield = shield;
    }

    /** Returns the maximum shield level of the unit. */
    public int getMaxShield() {
        return max_shield;
    }
    
    /** Sets the maximum shield level of the unit. */
    public void setMaxShield(int max_shield) {
        this.max_shield = max_shield;
    }
    
    /** Returns the speed of the unit in pixels per frame. */
    public double getSpeed() {
        return speed;
    }
    
    /** Sets the speed of the unit in pixels per frame. */
    public void setSpeed(double speed) {
    	this.speed = speed;
    }
    
    /** Returns the damage dealt by the object upon collision. */
    public int getColDamage() {
        return collision_damage;
    }
    
    /** Returns the damage dealt by the object upon collision. */
    public void setColDamage(int collision_damage) {
        this.collision_damage = collision_damage;
    }
    
/* The class methods */  
    /** Applies an amount of damage to this unit.
     * @param amount The amount of damage to be applied. */
    public void damage(int amount) {
    	shield = shield - amount;
    }
    
    /** Check for collision with all other units and applies damage.
     * @param obj The list of game objects. */
    public void collision(ArrayList<Game_Object> obj) {
        for (int i = 0; i < obj.size(); i++) {
        	if(collidesWith(obj.get(i)) && (obj.get(i) instanceof Unit)) {
        		Unit unit = (Unit) obj.get(i);
        		damage(unit.getColDamage());
        	}
        }
    }
}