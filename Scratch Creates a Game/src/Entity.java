
import static org.lwjgl.util.glu.GLU.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Entity {

	private float x;
	private float y;
	private float z;
	
	private float rotationX;
	private float rotationY;
	/** !DO NOT USE! */
	private float rotationZ;
	
	private float height;
	
	private float fov;
	private float aspect;
	private float near;
	private float far;
	private float sensivity;
	
	public Entity(float fov, float aspect, float near, float far, float sensivity){
		height = 2F;
		
		x = 0;
		y = 0;
		z = 0;
		
		rotationX = 0;
		rotationY = 0;
		rotationZ = 0;
		
		this.fov = fov;
		this.aspect = aspect;
		this.near = near;
		this.far = far;
		this.sensivity = sensivity;
		 
		setProjection();
	}
	
	private void setProjection(){
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(fov, aspect, near, far);
		glMatrixMode(GL_MODELVIEW);
		
		glEnable(GL_DEPTH_TEST);
	}
	
	public void setView(){
		glRotatef(rotationX, 1, 0, 0);
		glRotatef(rotationY, 0, 1, 0);
		glRotatef(rotationZ, 0, 0, 1);
		glTranslatef(x, y, z);
	}
	
	private float speed = 0.2F;
	
	public void setX(float x){
		this.x = x;
	}
	
	public void setY(float y){
		this.y = y;
	}
	
	public void setZ(float z){
		this.z = z;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public float getZ(){
		return z;
	}
	
	public void moveForward(){
		x -= (float) (speed*(Math.sin(Math.toRadians(rotationY))));
		z += (float) (speed*(Math.cos(Math.toRadians(rotationY))));
		
		checkY();
	}
	
	public void moveLeftward(){
		x += (float) (speed*(Math.cos(Math.toRadians(rotationY))));
		z += (float) (speed*(Math.sin(Math.toRadians(rotationY))));
	
		checkY();
	}
	
	public void moveBackward(){
		x += (float) (speed*(Math.sin(Math.toRadians(rotationY))));
		z -= (float) (speed*(Math.cos(Math.toRadians(rotationY))));

		checkY();
	}
	
	public void moveRightward(){
		x -= (float) (speed*(Math.cos(Math.toRadians(rotationY))));
		z -= (float) (speed*(Math.sin(Math.toRadians(rotationY))));

		checkY();
	}
	
	/** Calculates the Y Position */
	public void checkY(){
		/* Left = False : Right = True*/
		boolean side = (x - (int)x) + (z - (int)z) < -1;
		
		double x0 = x;
		double z0 = z;
		
		if(-(int)x0-1 >= 0 && -(int)x0 < Game.world.getWorld().length && -(int)z0-1 >= 0 && -(int)z0 < Game.world.getWorld()[0].length){
			int a = -Game.world.getWorld()[-(int)x0][-(int)z0-1];//Bottom_Left
			int b = -Game.world.getWorld()[-(int)x0-1][-(int)z0];//Top_Right
			int c;//Top_Left / Bottom_Right  <-- has two outputs depends on triangle
			
			double x1 = -(x0 - (int)x0);
			double z1 = -(z0 - (int)z0);
			
			if(side){//Høyre side
				c = -Game.world.getWorld()[-(int)x0-1][-(int)z0-1];
				y = (float) ((b-(((c-(1-x1)*2*(c-a))-(c+(1-z1)*2*(c-b))))/2))*Game.blockHeight-height;
				System.out.println(side);
			}else{
				c = -Game.world.getWorld()[-(int)x0][-(int)z0];
				y = (float) ((b+(((c+(1-z1)*2*(a-c))-(c-(1-x1)*2*(b-c))))/2))*Game.blockHeight-height;
				System.out.println(side);
			}
			
			/* Z
			if(side){//Høyre side
				c = -Game.world.getWorld()[-(int)x0-1][-(int)z0-1];
				y = (float) ((b+(((c-(1-x1)*2*(c-a))-(c-(1-z1)*2*(c-b))))/2))*Game.blockHeight-height;
				System.out.println(side);
			}else{
				c = -Game.world.getWorld()[-(int)x0][-(int)z0];
				y = (float) ((b-(((c-(1-z1)*2*(a-c))-(c-(1-x1)*2*(b-c))))/2))*Game.blockHeight-height;
				System.out.println(side);
			}*/
			
			//System.out.println(-(int)x + ", " + -(int)z + " : " + point_1 + " :" + point_2 + " : " + point_3);*/			
			//System.out.println(z1);
			//System.out.println((int)x + " : " + (int)y + " : " + (int)z);

		}
		
		//y = -Game.world.getWorld()[Math.round(-x)>=0&&Math.round(-x)<Game.world.getWorld().length?Math.round(-x):0][Math.round(-z)>=0&&Math.round(-z)<Game.world.getWorld()[0].length?Math.round(-z):0]*0.25F-height;
	}
	
	/** Pixels on screen, not degrees! */
	public void rotateX(int amount){
		rotationY -= (Display.getWidth() / 2 - amount) * sensivity;
		Mouse.setCursorPosition(Display.getWidth()/2, Mouse.getY());
	}

	/** Pixels on screen, not degrees! */
	public void rotateY(int amount){
		rotationX += (Display.getHeight() / 2 - amount) * sensivity;
		
		if(rotationX > 90){
			rotationX = 90;
		}
		
		if(rotationX < -90){
			rotationX = -90;
		}
		
		Mouse.setCursorPosition(Mouse.getX(), Display.getHeight()/2);
	}
	
}
