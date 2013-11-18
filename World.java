import java.util.Random;

import org.newdawn.slick.opengl.Texture;


public class World {

	private int[][] world;
	private int height;

	private Texture[][] texture1;
	private Texture[][] texture2;
	
	private Player player;
	
	public World(int worldSize){
		world = new int[worldSize][worldSize];
		height = 100;

		texture1 = new Texture[worldSize][worldSize];
		texture2 = new Texture[worldSize][worldSize];
	}
	
	public void generate(){
		Random random = new Random();
		
		generateFlat(random);
	}
	
	private void generateFlat(Random random){
		int y = 5;
		int y2 = y;
		
		for(int z = 0; z < world[0].length; z++){
			for(int x = 0; x < world.length; x++){
				world[x][z] = y2;
				y2--;
				
				/*int numberLeft = x > 0 ? world[x-1][z] : 0;
				int numberTop = z > 0 ? world[x][z-1] : 0;
				
				if(numberTop > numberLeft){
					if(numberTop-numberLeft == 2) world[x][z] = numberTop-1;
					else if(numberTop-numberLeft == 1) world[x][z] =/*random.nextInt(2) == 0 ? numberTop : numberLeft* numberTop;
					else world[x][z] = random.nextInt(3) - 1 + numberTop;
				}else if(numberTop < numberLeft){
					if(numberLeft-numberTop == 2) world[x][z] = numberLeft-1;
					else if(numberLeft-numberTop == 1) world[x][z] = /*random.nextInt(2) == 0 ? numberLeft : numberTop* numberLeft;
					else world[x][z] = random.nextInt(3) - 1 + numberTop;
				}else{
					world[x][z] = random.nextInt(3) - 1 + numberTop;
				}*/

				texture1[x][z] = Game.textures_world[0][random.nextInt(Game.textures_world[0].length)];
				texture2[x][z] = Game.textures_world[0][random.nextInt(Game.textures_world[0].length)];
			}
			y2 = y;
		}
	}
	
	public int getHeight(){
		return height;
	}
	
	public void addPlayer(Player player){
		this.player = player;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public int[][] getWorld(){
		return world;
	}
	
	public Texture getTexture1(int x, int z){
		return texture1[x][z];
	}
	
	public Texture getTexture2(int x, int z){
		return texture2[x][z];
	}
	
}
