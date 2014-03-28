/* SWEN20003 Object Oriented Software Development
 * Space Game Engine (Sample Project)
 * Author: Ryan Hodgman <hodgmanr>
 */

import java.util.ArrayList;
import org.newdawn.slick.SlickException;

/** The ship which the player controls. */
public class Player extends Unit implements Weaponized_Ship{
/* The class variables */
    /** File path of the player's ship image file. */
    private static final String image_file
        = Game.ASSETS_PATH + "/units/player.png";
    
    /** Measure of the player's power level (used to calculate cool-down). */
    private int firepower = 0;
    
    /** Measure of how fast the player may fire missiles. */
    private int cooldown;
    
    /** How quickly the player automatically moves forward (pixels per frame). */
    private double auto_speed = 0.25;
    
    /** Tracks whether the player is still alive or not. */
    private Boolean is_alive = true;
    
    /** Last checkpoint that the player passed. */
    private double last_checkpoint = 13716;

/* The constructor(s) */
    /** Creates a new player object.
     * @param x The player's start location (x, pixels).
     * @param y The player's start location (y, pixels). */
    public Player(double x, double y)
        throws SlickException {
    	setImage(image_file);
    	setX(x);
        setY(y);
        setSpeed(0.4);
        setShield(100);
        setMaxShield(100);
        setColDamage(10);
    }

/* The getter and setter methods */
    /** Gets the fire-power of the player. */
    public int getFirepower() {
        return firepower;
    }
    
    /** Sets the fire-power of the player. */
    public void setFirepower(int firepower) {
        this.firepower = firepower;
    }
    
    /** Returns the speed at which the player moves forward in pixels per frame. */
    private double getAutoSpeed() {
        return auto_speed;
    }

/* The class methods */
    /** Move the player automatically forwards, as well as (optionally) in a
     * given direction. Prevents the player from moving onto blocking terrain,
     * or outside of the screen (camera) bounds.
     * @param world The world the player is on (to check blocking).
     * @param obj The list of game objects (to check collision).
     * @param cam The current camera (to check blocking).
     * @param panel The panel object (to check screen bounds).
     * @param dir_x The player's movement in the x axis (-1, 0 or 1).
     * @param dir_y The player's movement in the y axis (-1, 0 or 1).
     * @param delta Time passed since the last frame in milliseconds. 
     * @throws SlickException */
    public void update(World world, ArrayList<Game_Object> obj, Camera cam, Panel panel, 
    		double dir_x, double dir_y, Boolean fire, int delta) 
    throws SlickException {
        // Calculate the amount to move in each direction, based on speed.
        double amount = delta * getSpeed();
        // The new location.
        double x = getX() + dir_x * amount;
        double y = getY() + dir_y * amount;
        Boolean forced = false;
        if(!world.reachedTop()) {
            y -= delta * getAutoSpeed();
        }
        // Check if the player is off the screen, and push back in.
        if(x < cam.getLeft() + halfWidth()) {
           x = cam.getLeft() + halfWidth();
        }
        if(x > cam.getRight() - halfWidth() - 1) {
           x = cam.getRight() - halfWidth() - 1;
        }
        if(y < cam.getTop() + halfHeight()) {
           y = cam.getTop() + halfHeight();
        }
        if(y > cam.getBottom() - panel.getHeight() - halfHeight() - 1) {
           y = cam.getBottom() - panel.getHeight() - halfHeight() - 1;
           forced = true;
        }
        moveTo(world, x, y);
        /* Checks whether the player has been forced off of the screen by terrain. */
        double[] top_left = {leftEdgeX(), topEdgeY() - 3};
        double[] top_right = {rightEdgeX(), topEdgeY() - 3};
        Boolean tl = world.terrainBlocks(top_left);
        Boolean tr = world.terrainBlocks(top_right);
        if((tl || tr) && forced) {
        	is_alive = false;
        }
        // Runs the collision check function.
        collision(obj);
    	// Checks that the player has shields left.
    	if(getShield() <= 0) {
    		is_alive = false;
    	}
    	// Record of the checkpoints (SHOULD BE READ IN FROM A FILE!). 
    	double checkpoints[] = {13716, 9756, 7812, 5796, 2844, 1296, 0};
    	for (int i = 0; i < checkpoints.length; i++) {
			if((getY() <= checkpoints[i]) && (getY() > checkpoints[i + 1])) {
				last_checkpoint = checkpoints[i];
			}
		}
    	// Deal with player death. 
    	if(is_alive == false) {
    		moveTo(world, 1296, last_checkpoint);
    		setShield(getMaxShield());
    		cam.centerOnObject(world, this);
    		is_alive = true;
    	}
        // Checks for player input and fires a missile if able.
        cooldown = cooldown - delta;
        if(fire && cooldown <= 0) {
        	fire(world, obj);
        	resetTimer();
        }
        System.out.println(getX() + " " + getY() + " " + last_checkpoint);
    }
    
    /** Overrides the Unit class collision method to account for items.
     * @param obj The list of game objects. */
    @Override
    public void collision(ArrayList<Game_Object> obj) {
        for (int i = 0; i < obj.size(); i++) {
        	if(collidesWith(obj.get(i)) && (obj.get(i) instanceof Unit)) {
        		Unit unit = (Unit) obj.get(i);
        		damage(unit.getColDamage());
        		System.out.println("Collision!");
        	}
        	if(collidesWith(obj.get(i)) && (obj.get(i) instanceof Item)) {
        		Item item = (Item) obj.get(i);
        		item.pickUp(this);
        	}
        }
    }
    
    /** Creates and fires a new player missile.
     * @param world The current game state. 
     * @param obj The list of game objects. */
    public void fire(World world, ArrayList<Game_Object> obj) 
    throws SlickException {
    	Missile missile = new Missile(getX(), getY(), "player");
    	obj.add(missile);
    }
    
	/** Resets the cool-down timer. */
    public void resetTimer() {
        cooldown = 300 - (80 * firepower);
    }    
}
