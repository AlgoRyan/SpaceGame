/* SWEN20003 Object Oriented Software Development
 * Space Game Engine
 * Author: Ryan Hodgman <hodgmanr>
 */

import java.util.ArrayList;
import org.newdawn.slick.SlickException;

/** The missile class.  */
public class Missile extends Game_Object {
/* The class variables */
    /** File path of the player-fired missile image. */
    private static final String player_image_file
        = Game.ASSETS_PATH + "/units/missile-player.png";
    
    /** File path of the enemy-fired missile image. */
    private static final String enemy_image_file
        = Game.ASSETS_PATH + "/units/missile-enemy.png";
    
    /** The damage the missile inflicts upon collision. */
    private int damage = 8;
    
    /** The speed of the missile (pixels per frame). */
    private double speed = 1.0;
    
    /** The direction of the missile (up or down). */
    private int direction;
    
/* The constructor(s) */
    /** Creates a new missile.
     * @param x The missile's start location (x, pixels).
     * @param y The missile's start location (y, pixels).
     * @param type Whether fired by an enemy or a player. */
    public Missile(double x, double y, String type)
        throws SlickException {
    	setX(x);
        if (type == "player") {
        	setImage(player_image_file);
        	setDir("up");
            setY(y - 50);
        } 
        if (type == "enemy") {
        	setImage(enemy_image_file);
        	setDir("down");
            setY(y + 50);
        }
    }
    
/* The getter and setter methods */
    /** Returns the speed of the missile in pixels per frame. */
    public double getSpeed() {
        return speed;
    }
    
    /** Sets the speed of the missile in pixels per frame. */
    public void setSpeed(double speed) {
    	this.speed = speed;
    }
    
    /** Sets the direction of the missile to up (-1) or down (1). */
    public void setDir(String dir) {
    	if (dir == "up"){
    		direction = -1;
    	} else if (dir == "down") {
    		direction = 1;
    	} else {
    		System.out.println("Invalid argument to Missile.setDir() method!");
    		System.exit(0);
    	}
    }
    
/* The class methods */  
    /** Move the missile automatically forwards.
     * @param world The current game state.
     * @param cam The camera object.
     * @param obj The list of game objects.
     * @param delta Time passed since the last frame in milliseconds. */
    public void update(World world, Camera cam, ArrayList<Game_Object> obj, int delta) {
    	// Runs the impact check function.
    	impact(obj);
    	// Checks that the missile should still exist.
    	if(hitTerrain(world) || !onScreen(cam)) {
    		setExists(false);
    	}
        /* Calculate the amount to move in each direction, based on speed */
        double amount = delta * speed;
        /* The new location */
        double y = getY() + direction * amount;
        moveTo(world, getX(), y);
    }    
    
    /** Applies the missile damage to the impacted unit and destroys the missile.
     * @param obj The list of game objects. */
    public void impact(ArrayList<Game_Object> obj) {
        for (int i = 0; i < obj.size(); i++) {
        	if(obj.get(i) instanceof Unit) {
        		Unit unit = (Unit) obj.get(i);
        		if(collidesWith(unit)) {
                	unit.damage(damage);
                	setExists(false);
        		}
        	}
        }
    }
    
    /** Calculates just under half the height of the missile, to eliminate dead space. */
    @Override
    public double halfHeight() {
        double height = getImage().getHeight()/2.0 - 15;
    	return height;
    }
    
    /** Calculates just under half the width of the missile, to eliminate dead space. */
    @Override
    public double halfWidth() {
        double width = getImage().getWidth()/2.0 - 15;
    	return width;
    }
}