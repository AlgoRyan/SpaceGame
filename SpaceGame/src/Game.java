/* SWEN20003 Object Oriented Software Development
 * Space Game Engine
 * Author: Matt Giuca <mgiuca>
 */

import java.io.FileNotFoundException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Input;

/** Main class for the Role-Playing Game engine.
 * Handles initialization, input and rendering.
 */
public class Game extends BasicGame {
    /** Location of the "assets" directory. */
    public static final String ASSETS_PATH = "assets";
    
    /** Location of the "data" directory. */
    public static final String DATA_PATH = "data";

    /** The game state. */
    private World world;

    /** Screen width, in pixels. */
    public static final int screenwidth = 1100;
    
    /** Screen height, in pixels. */
    public static final int screenheight = 600;

    /** Width of the play area, in pixels. */
    public static int playwidth() {
        return screenwidth;
    }
    
    /** Height of the play area, in pixels. */
    public static int playheight() {
        return screenheight;
    }

    /** Create a new Game object. */
    public Game() {
        super("Space Game");
    }

    /** Initialize the game state.
     * @param gc The Slick game container object.
     */
    @Override
    public void init(GameContainer gc)
    throws SlickException{
        try {
			world = new World();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Couldn't find the data file!");
			System.exit(0);
		}
    }

    /** Update the game state for a frame.
     * @param gc The Slick game container object.
     * @param delta Time passed since last frame (milliseconds).
     */
    @Override
    public void update(GameContainer gc, int delta)
    throws SlickException {
        // Get data about the current input (keyboard state).
        Input input = gc.getInput();

        // Update the player's movement direction based on keyboard presses.
        double dir_x = 0;
        double dir_y = 0;
        Boolean fire = false;
        if (input.isKeyDown(Input.KEY_DOWN))
            dir_y += 1;
        if (input.isKeyDown(Input.KEY_UP))
            dir_y -= 1;
        if (input.isKeyDown(Input.KEY_LEFT))
            dir_x -= 1;
        if (input.isKeyDown(Input.KEY_RIGHT))
            dir_x += 1;
        if (input.isKeyDown(Input.KEY_SPACE))
            fire = true;

        // Let World.update decide what to do with this data.
        world.update(dir_x, dir_y, fire, delta);
    }

    /** Render the entire screen, so it reflects the current game state.
     * @param gc The Slick game container object.
     * @param g The Slick graphics object, used for drawing.
     */
    @Override
    public void render(GameContainer gc, Graphics g)
    throws SlickException {
        // Let World.render handle the rendering.
        world.render(g);
    }

    /** Start-up method. Creates the game and runs it.
     * @param args Command-line arguments (ignored).
     */
    public static void main(String[] args)
    throws SlickException {
        AppGameContainer app = new AppGameContainer(new Game());
        // setShowFPS(true), to show frames-per-second.
        app.setShowFPS(false);
        app.setDisplayMode(screenwidth, screenheight, false);
        app.start();
    }
}
