import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;
public class Game {

	public static boolean isRunning = true;
	
	private int width = 1280;
	private int height = 720;
	
	public static float blockHeight = 0.25F;
	
	public static Texture[][] textures_world = new Texture[800][2];
	public static Texture[] textures = new Texture[10];
	
	public static void main(String[] args){
		try {
			new Game().startGame();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Regular game window  */
	public Game(){
		try {
			Display.setTitle("Scratch Creates a Game");
			//Display.setFullscreen(true); //Change to true
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
		} catch (LWJGLException e) {
			Game.isRunning = false;
			System.out.println("Problems Creating The Window!");
		}
	}
	
	public Game startGame() throws IOException{
		loadTextures();
		
		gameLoop();
		return this;
	}
	
	private void loadTextures() throws IOException{
		String path = "res/tile.png";
	    BufferedImage image = ImageIO.read(new File(path));
	    
	    BufferedImage out;
	    File file;
		
		for(int x = 0; x < image.getWidth(null)/33; x++){
			for(int y = 0; y < image.getHeight(null)/33; y++){
				out = image.getSubimage(x*33, y*33, 32, 32);

				ImageIO.write(out, "png", new File("res/image_" + ((y+x*image.getWidth(null)/33)<10?("00" + (y+x*image.getWidth(null)/33)) : (y+x*image.getWidth(null)/33)<100?("0" + (y+x*image.getWidth(null)/33)):(y+x*image.getWidth(null)/33)) + "_1.png"));
				textures_world[y + x*image.getWidth(null)/33][0] = TextureLoader.getTexture("png", new FileInputStream(new File("res/image_" + ((y+x*image.getWidth(null)/33)<10?("00" + (y+x*image.getWidth(null)/33)):(y+x*image.getWidth(null)/33)<100?("0" + (y+x*image.getWidth(null)/33)):(y+x*image.getWidth(null)/33)) + "_1.png")));	
			
				//file = new File("res/image_" + ((y+x*image.getWidth(null)/33)<10?("00" + (y+x*image.getWidth(null)/33)) : (y+x*image.getWidth(null)/33)<100?("0" + (y+x*image.getWidth(null)/33)):(y+x*image.getWidth(null)/33)) + "_1.png");
				//file.delete();

				out = image.getSubimage(x*33+1, y*33+1, 32, 32);
				
				// Flip the image vertically
				AffineTransform tx = AffineTransform.getScaleInstance(-1, -1);
				tx.translate(-out.getWidth(null), -out.getHeight(null));
				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
				out = op.filter(out, null);
				
				ImageIO.write(out, "png", new File("res/image_" + ((y+x*image.getWidth(null)/33)<10?("00" + (y+x*image.getWidth(null)/33)) : (y+x*image.getWidth(null)/33)<100?("0" + (y+x*image.getWidth(null)/33)):(y+x*image.getWidth(null)/33)) + "_2.png"));
				textures_world[y + x*image.getWidth(null)/33][1] = TextureLoader.getTexture("png", new FileInputStream(new File("res/image_" + ((y+x*image.getWidth(null)/33)<10?("00" + (y+x*image.getWidth(null)/33)):(y+x*image.getWidth(null)/33)<100?("0" + (y+x*image.getWidth(null)/33)):(y+x*image.getWidth(null)/33)) + "_2.png")));	
			
				//file = new File("res/image_" + ((y+x*image.getWidth(null)/33)<10?("00" + (y+x*image.getWidth(null)/33)) : (y+x*image.getWidth(null)/33)<100?("0" + (y+x*image.getWidth(null)/33)):(y+x*image.getWidth(null)/33)) + "_2.png");
				//file.delete();
			}
		}
		
		textures[0] = TextureLoader.getTexture("png", new FileInputStream(new File("res/marker.png")));
	}
	
	float f;
	
	public static World world;
	
	public void gameLoop(){
		world = new World(100);
	
		world.generate();
		world.addPlayer(new Player(70, (float)Display.getWidth()/(float)Display.getHeight(), 0.3F, 1000, 0.1F, world.getWorld().length, world.getWorld()[0].length));
		
		Mouse.setCursorPosition(Display.getWidth()/2, Display.getHeight()/2);
		Mouse.setGrabbed(true);
		
		f = (float)Display.getWidth() / (float)Display.getHeight();
		
		while(Game.isRunning){
			if(Keyboard.isKeyDown(Keyboard.KEY_W)) world.getPlayer().moveForward();
			if(Keyboard.isKeyDown(Keyboard.KEY_A)) world.getPlayer().moveLeftward();
			if(Keyboard.isKeyDown(Keyboard.KEY_S)) world.getPlayer().moveBackward();
			if(Keyboard.isKeyDown(Keyboard.KEY_D)) world.getPlayer().moveRightward();
			
			world.getPlayer().rotateX(Mouse.getX());
			world.getPlayer().rotateY(Mouse.getY());
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glClearColor(0.0F, 0.5F, 1.0F, 0.0F);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	        glMatrixMode(GL_MODELVIEW);
	        
			glLoadIdentity();
			
			world.getPlayer().setView();
			
			Display.sync(30); 
			
			glPushMatrix();
				for(int x = 0; x < world.getWorld().length; x++){
					for(int z = 0; z < world.getWorld()[0].length; z++){
						glTranslatef(x, world.getWorld()[x][z]*blockHeight, z);
						
						glDisable(GL_COLOR_MATERIAL);
						glColor4f(0f, 0.7f, 0f, 1f);
						
						/* First Triangle */
						world.getTexture1(x, z).bind();
						glBegin(GL_TRIANGLES);  // draw a cube with 12 triangles
							glTexCoord2f(0, 0);
							glVertex3f(0, 0, 0);
								
							glTexCoord2f(1, 0);
							if(x+1 < world.getWorld().length && world.getWorld()[x+1][z] > world.getWorld()[x][z]) glVertex3f(1, 1*blockHeight, 0);
							else if(x+1 < world.getWorld().length && world.getWorld()[x+1][z] < world.getWorld()[x][z]) glVertex3f(1, -1*blockHeight, 0);
							else glVertex3f(1, 0*blockHeight, 0);
							
							glTexCoord2f(0, 1);
							if(z+1 < world.getWorld()[0].length && world.getWorld()[x][z+1] > world.getWorld()[x][z]) glVertex3f(0, 1*blockHeight, 1);
							else if(z+1 < world.getWorld()[0].length && world.getWorld()[x][z+1] < world.getWorld()[x][z]) glVertex3f(0,-1*blockHeight, 1);
							else glVertex3f(0, 0*blockHeight, 1);
						glEnd();
						
						glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
						
						/* Second Triangle */
						world.getTexture2(x, z).bind();
						glBegin(GL_TRIANGLES);  // draw a cube with 12 triangles
							glTexCoord2f(1, 0);
							if(x+1 < world.getWorld().length && world.getWorld()[x+1][z] > world.getWorld()[x][z]) glVertex3f(1, 1*blockHeight, 0);
							else if(x+1 < world.getWorld().length && world.getWorld()[x+1][z] < world.getWorld()[x][z]) glVertex3f(1,-1*blockHeight, 0);
							else glVertex3f(1, 0*blockHeight, 0);

							glTexCoord2f(0, 1);
							if(z+1 < world.getWorld()[0].length && world.getWorld()[x][z+1] > world.getWorld()[x][z]) glVertex3f(0, 1*blockHeight, 1);
							else if(z+1 < world.getWorld()[0].length && world.getWorld()[x][z+1] < world.getWorld()[x][z]) glVertex3f(0,-1*blockHeight, 1);
							else glVertex3f(0, 0*blockHeight, 1);

							glTexCoord2f(0, 0);
							if(x+1 < world.getWorld().length && z+1 < world.getWorld()[0].length && world.getWorld()[x+1][z+1] == world.getWorld()[x][z]+2) glVertex3f(1, 2*blockHeight, 1);
							else if(x+1 < world.getWorld().length && z+1 < world.getWorld()[0].length && world.getWorld()[x+1][z+1] == world.getWorld()[x][z]+1) glVertex3f(1, 1*blockHeight, 1);
							else if(x+1 < world.getWorld().length && z+1 < world.getWorld()[0].length && world.getWorld()[x+1][z+1] == world.getWorld()[x][z]-1) glVertex3f(1,-1*blockHeight, 1);
							else if(x+1 < world.getWorld().length && z+1 < world.getWorld()[0].length && world.getWorld()[x+1][z+1] == world.getWorld()[x][z]-2) glVertex3f(1,-2*blockHeight, 1);
							else glVertex3f(1, 0*blockHeight, 1);
						glEnd();
						
						int range = 20;
						if((float)(-Math.sqrt((-x-world.getPlayer().getX())*(-x-world.getPlayer().getX())+(-z-world.getPlayer().getZ())*(-z-world.getPlayer().getZ()))+range)/range > 0){
							glEnable(GL_COLOR_MATERIAL);
							glColor3f(0f, 0.827f, 0.165F);
							glDisable(GL_TEXTURE_2D);
							glTranslatef(0, -0.01F, 0);
							glBegin(GL11.GL_LINES);
								glLineWidth(5.0F);
							
								glVertex3f(0, 0, 0);
									
								if(x+1 < world.getWorld().length && world.getWorld()[x+1][z] > world.getWorld()[x][z]){
									glVertex3f(1, 1*blockHeight, 0);
									glVertex3f(1, 1*blockHeight, 0);
								} else if(x+1 < world.getWorld().length && world.getWorld()[x+1][z] < world.getWorld()[x][z]){
									glVertex3f(1,-1*blockHeight, 0); 
									glVertex3f(1,-1*blockHeight, 0);
								} else {
									glVertex3f(1, 0*blockHeight, 0); 
									glVertex3f(1, 0*blockHeight, 0);
								}
								
								if(z+1 < world.getWorld()[0].length && world.getWorld()[x][z+1] > world.getWorld()[x][z]){
									glVertex3f(0, 1*blockHeight, 1);
									glVertex3f(0, 1*blockHeight, 1);
								} else if(z+1 < world.getWorld()[0].length && world.getWorld()[x][z+1] < world.getWorld()[x][z]){
									glVertex3f(0,-1*blockHeight, 1);
									glVertex3f(0,-1*blockHeight, 1);
								} else {
									glVertex3f(0, 0*blockHeight, 1);
									glVertex3f(0, 0*blockHeight, 1);
								}
								
								glVertex3f(0, 0, 0);
							glEnd();
							glTranslatef(0, 0.01F, 0);
							glEnable(GL_TEXTURE_2D);
							
							glEnable(GL_COLOR_MATERIAL);
							glColor4f(0f, 0.7f, 0f, (float)(-Math.sqrt((-x-world.getPlayer().getX())*(-x-world.getPlayer().getX())+(-z-world.getPlayer().getZ())*(-z-world.getPlayer().getZ()))+range)/range);
							glDisable(GL_TEXTURE_2D);
							glTranslatef(0, 0.01F, 0);
							glBegin(GL11.GL_LINES);
								glLineWidth(5.0F);
							
								glVertex3f(0, 0, 0);
									
								if(x+1 < world.getWorld().length && world.getWorld()[x+1][z] > world.getWorld()[x][z]){
									glVertex3f(1, 1*blockHeight, 0);
									glVertex3f(1, 1*blockHeight, 0);
								} else if(x+1 < world.getWorld().length && world.getWorld()[x+1][z] < world.getWorld()[x][z]){
									glVertex3f(1,-1*blockHeight, 0); 
									glVertex3f(1,-1*blockHeight, 0);
								} else {
									glVertex3f(1, 0*blockHeight, 0); 
									glVertex3f(1, 0*blockHeight, 0);
								}
								
								if(z+1 < world.getWorld()[0].length && world.getWorld()[x][z+1] > world.getWorld()[x][z]){
									glVertex3f(0, 1*blockHeight, 1);
									glVertex3f(0, 1*blockHeight, 1);
								} else if(z+1 < world.getWorld()[0].length && world.getWorld()[x][z+1] < world.getWorld()[x][z]){
									glVertex3f(0,-1*blockHeight, 1);
									glVertex3f(0,-1*blockHeight, 1);
								} else {
									glVertex3f(0, 0*blockHeight, 1);
									glVertex3f(0, 0*blockHeight, 1);
								}
								
								glVertex3f(0, 0, 0);
							glEnd();
							glTranslatef(0, -0.01F, 0);
							glEnable(GL_TEXTURE_2D);
						}
					
						glTranslatef(-x, -world.getWorld()[x][z]*blockHeight, -z);		
					}			
				}
				/*
				///
				switchTo2DMode();
				///
				
				textures[0].bind();//marker
				glBegin(GL_QUADS);
					glTexCoord2f(0, 1); glVertex2f(0, 1);
					glTexCoord2f(1, 1); glVertex2f(1, 1);
					glTexCoord2f(1, 0); glVertex2f(1, 0);
					glTexCoord2f(0, 0); glVertex2f(0, 0);
				glEnd();
			glPopMatrix();*/

			Display.update();
			
			if(Display.isCloseRequested()){
				Game.isRunning = false;
			}
		}
		
		Display.destroy();
	}
	
	private void switchTo2DMode(){
		//glClearColor(0.0f, 0.0f, 0.0f, 0.0f);                
	    //glClearDepth(1);
	    glViewport(0, 0, Display.getWidth(), Display.getHeight());
		glMatrixMode(GL_PROJECTION);
		//glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
	    glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}
	
	private void switchTo3DMode(){
		/*glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
       // gluPerspective(45.0f, 4/3.0f, 0.1f, 1000);*/
        glMatrixMode(GL_PROJECTION);
        //glEnable(GL_DEPTH_TEST);
        //glDepthFunc(GL_LEQUAL);
        //glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        
        
       
	}
	
}
