/* SWEN20003 Object Oriented Software Development
 * Space Game Engine (Sample Project)
 * Author: Ryan Hodgman <hodgmanr>
 */

/** The camera, the rectangular region on the map through which the player
 * sees the world. */
public class Camera {
/* The class variables */
    /** The left x coordinate of the camera (pixels). */
    private double left;
    
    /** The top y coordinate of the camera (pixels). */
    private double top;
    
    /** The speed at which the camera automatically moves upwards (pixels/millisecond). */
    private double auto_speed = 0.25;

/* The getter and setter methods */
    /** The left x coordinate of the camera (pixels). */
    public double getLeft() {
        return left;
    }
 
    /** The right x coordinate of the camera (pixels). */
    public double getRight() {
        return left + Game.playwidth();
    }
    
    /** The top y coordinate of the camera (pixels). */
    public double getTop() {
        return top;
    }
    
    /** The bottom y coordinate of the camera (pixels). */
    public double getBottom() {
        return top + Game.playheight();
    }
    
    /** The number of pixels the camera automatically moves per millisecond. */
    public double getCamSpeed() {
        return auto_speed;
    }

/* The constructor(s) */
    /** Creates a new camera such that the given game object is at the bottom of
     * the screen, and the camera is centered horizontally.
     * @param world The world, to calculate the map dimensions.
     * @param obj The game object, to get the object's location. */
    public Camera(World world, Game_Object obj) {
        centerOnObject(world, obj);
    }

    /** Creates a new camera at the given coordinates.
     * @param left Initial left x coordinate of the camera (pixels).
     * @param top Initial top y coordinate of the camera (pixels). */
    public Camera(double left, double top) {
        moveto(left, top);
    }

    
/* The class methods */ 
    /** Run an update on the camera (move vertically where appropriate).
     * @param delta Time passed since last frame (milliseconds). */
    public void update(int delta) {
        double amount = delta * getCamSpeed();
        moveto(left, top - amount);
    }
    
    /** Make the camera follow a game object and then bound to the map 
     * (don't show dead space).
     * @param obj The object to follow. */
    public void followUnit(Game_Object obj) {
    	int left = (int) obj.getX() - (Game.playwidth() / 2);
        moveto (left, top);
    }

    /** Move the camera such that the given game object is at the bottom of the
     * screen, and the camera is centered horizontally.
     * @param world The world, to calculate the map dimensions.
     * @param obj The game object, to get the object's location. */
    public void centerOnObject(World world, Game_Object obj) {
        // Center horizontally in the world
        double left = (world.getWidth() - Game.playwidth()) / 2;
        // Place vertically such that the player is 142 pixels from the bottom
        // of the screen
        double top = obj.getY() - (double) (Game.playheight() - 142);
        moveto(left, top);
    }

    /** Update the camera's x and y coordinates.
     * Prevents the camera from moving past the top edge of the screen.
     * @param world The world the camera is on (to check blocking).
     * @param left New left x coordinate of the camera (pixels).
     * @param top New top y coordinate of the camera (pixels). */
    private void moveto(double left, double top) {
        if (top < 0)
            top = 0;
        this.left = left;
        this.top = top;
    }
}
