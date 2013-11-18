import org.lwjgl.opengl.Display;


public class Player extends Entity{

	public Player(float fov, float aspect, float near, float far, float sensivity, int x, int z) {
		super(fov, aspect, near, far, sensivity);
		
		this.rotateX(Display.getWidth());
		this.rotateY(200);
		
		this.setX(-x/2);
		this.setZ(-z/2);
		
		checkY(); //y = -0.5F-Game.world.getHeight()*0.25F; /* opengl makes you start at the bottom of a square.  0.5F to center, then add 2 for your height */
	}

}
