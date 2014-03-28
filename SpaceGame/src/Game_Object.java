/* SWEN20003 Object Oriented Software Development
 * Space Game Engine
 * Author: Ryan Hodgman <hodgmanr>
 */

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/** Contains the common attributes of every game object. */
public class Game_Object {
/* The class variables */
    /** Image of the object. */
    private Image img;
    
    /** The x coordinate of the object, relative to the map (pixels). */
    private double x;
    
    /** The y coordinate of the object, relative to the map (pixels). */
    private double y;
    
    /** Contains the state of the object. */
    private Boolean exists = true;

/* The getter and setter methods */    
    /** Returns the x coordinate of the object. */
    public double getX() {
        return x;
    }
    
    /** Sets the x coordinate of the object. */
    public void setX(double x) {
        this.x = x;
    }

    /** Returns the y coordinate of the object. */
    public double getY() {
        return y;
    }
    
    /** Sets the y coordinate of the object. */
    public void setY(double y) {
        this.y = y;
    }
    
    /** Returns the state of the object. */
    public Boolean getExists() {
        return exists;
    }

    /** Sets the state of the object. */
    public void setExists(Boolean exists) {
        this.exists = exists;
    }
    
    /** Gets the image of the object. */
    public Image getImage() {
        return img;
    }
    
    /** Sets the image of the object. */
    public void setImage(String path) 
    	throws SlickException {
        img = new Image(path);
    }
    
/* The class methods */
    /** Draw the object to the screen at the correct place.
     * @param g The current Graphics context.
     * @param cam Camera used to render the current screen. */
    public void render(Graphics g, Camera cam) {
        // Calculate the object's on-screen location from the camera.
        int screen_x, screen_y;
        screen_x = (int) (x - cam.getLeft());
        screen_y = (int) (y - cam.getTop());
        // Checks that the object is visible before drawing it.
        if(onScreen(cam)) {
            img.drawCentered(screen_x, screen_y);
        }
    }
    
    /** Determines if the object is on the screen. 
     * @param cam The camera object. */
    public Boolean onScreen(Camera cam) {
        if(x < (cam.getLeft() - halfWidth()) || x > (cam.getRight() + halfWidth())
        		|| y < (cam.getTop() - halfHeight()) || y > cam.getBottom() + halfHeight()) {
        	return false;
        } else {
            return true;
        }
    }
    
    /** Calculates half of the height of the object. */
    public double halfHeight() {
        double height = img.getHeight()/2.0 - 1;
    	return height;
    }
    
    /** Calculates half of the width of the object. */
    public double halfWidth() {
        double width = img.getWidth()/2.0 - 1;
    	return width;
    }
    
    /** Calculates the x-value of the left edge of the object. */
    public double leftEdgeX() {
        double x = this.x - halfWidth();
    	return x;
    }
    
    /** Calculates the x-value of the right edge of the object. */
    public double rightEdgeX() {
        double x = this.x + halfWidth();
    	return x;
    }
    
    /** Calculates the y-value of the top edge of the object. */
    public double topEdgeY() {
        double y = this.y - halfHeight();
    	return y;
    }
    
    /** Calculates the y-value of the bottom edge of the object. */
    public double bottomEdgeY() {
        double y = this.y + halfHeight();
    	return y;
    }
    
    /** Determines whether this object is within the x-bounds 
     * of another game object. 
     * @param obj The game object to be checked against. */
    public Boolean xIntersect(Game_Object obj) {
        if(obj.leftEdgeX() < this.rightEdgeX() && obj.leftEdgeX() > this.leftEdgeX()) {
        	return true;
        }
        if(obj.rightEdgeX() < this.rightEdgeX() && obj.rightEdgeX() > this.leftEdgeX()) {
        	return true;
        } 
        return false;
    }
    
    /** Determines whether this object is within the y-bounds 
     * of another game object. 
     * @param obj The game object to be checked against. */
    public Boolean yIntersect(Game_Object obj) {
        if(obj.topEdgeY() < this.bottomEdgeY() && obj.topEdgeY() > this.topEdgeY()) {
        	return true;
        }
        if(obj.bottomEdgeY() < this.bottomEdgeY() && obj.bottomEdgeY() > this.topEdgeY()) {
        	return true;
        } 
        return false;
    }
    
    /** Determines whether the object has collided with any terrain. */
    public Boolean hitTerrain(World world) {
        /* The coordinates of the four corners of the object. */
        double[] top_left = {leftEdgeX() - 2, topEdgeY() - 2};
        double[] top_right = {rightEdgeX() + 2, topEdgeY() - 2};
        double[] bot_left = {leftEdgeX() - 2, bottomEdgeY() + 2};
        double[] bot_right = {rightEdgeX() + 2, bottomEdgeY() + 2};
        
        /* Determine whether each position is blocked or not. */
        Boolean tl = world.terrainBlocks(top_left);
        Boolean tr = world.terrainBlocks(top_right);
        Boolean bl = world.terrainBlocks(bot_left);
        Boolean br = world.terrainBlocks(bot_right);
        
        if(tl || tr || bl || br) {
        	return true;
        } else {
            return false;
        }
    }
    
    /** Update the object's x and y coordinates.
     * Prevents the object from moving onto blocking terrain.
     * @param world The world the object is on (to check blocking).
     * @param x New x coordinate.
     * @param y New y coordinate. */
    public void moveTo(World world, double x, double y) {        
        /* The new coordinates of the four corners of the object, 
         * respective to the world and the direction of movement. 
         * The format is (direction-of-movement)_(neutral-edge). */
        double[] top_left = {leftEdgeX(), y - halfHeight()};
        double[] top_right = {rightEdgeX(), y - halfHeight()};
        double[] bot_left = {leftEdgeX(), y + halfHeight()};
        double[] bot_right = {rightEdgeX(), y + halfHeight()};
        double[] left_top = {x - halfWidth(), topEdgeY()};
        double[] left_bot = {x - halfWidth(), bottomEdgeY()};
        double[] right_top = {x + halfWidth(), topEdgeY()};
        double[] right_bot = {x + halfWidth(), bottomEdgeY()};
        
        /* Determine whether each position/direction is blocked or not. */
        Boolean tl_blocked = world.terrainBlocks(top_left);
        Boolean tr_blocked = world.terrainBlocks(top_right);
        Boolean bl_blocked = world.terrainBlocks(bot_left);
        Boolean br_blocked = world.terrainBlocks(bot_right);
        Boolean lt_blocked = world.terrainBlocks(left_top);
        Boolean lb_blocked = world.terrainBlocks(left_bot);
        Boolean rt_blocked = world.terrainBlocks(right_top);
        Boolean rb_blocked = world.terrainBlocks(right_bot);
        
        /* Determine whether each edge is blocked or not. */
        Boolean top_blocked = tl_blocked || tr_blocked;
        Boolean bot_blocked = bl_blocked || br_blocked;
        Boolean left_blocked = lt_blocked || lb_blocked;
        Boolean right_blocked = rt_blocked || rb_blocked;
        
        // Try moving forwards.
        if(!top_blocked && y < this.y) {
        	this.y = y;
        }        
        // Try moving backwards.
        if(!bot_blocked && y > this.y) {
        	this.y = y;
        }        
        // Try moving left.
        if(!left_blocked && x < this.x) {
        	this.x = x;
        }
        // Try moving right.
        if(!right_blocked && x > this.x) {
        	this.x = x;
        }
    }
    
    /** Check whether this object has collided with another game object.
     * @param obj The game object to be checked against. */
    public Boolean collidesWith(Game_Object obj) {
    	if(xIntersect(obj) && yIntersect(obj)) {
    		return true;
    	}
    	return false;
    }
}