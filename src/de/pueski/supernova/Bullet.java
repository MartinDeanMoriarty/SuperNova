package de.pueski.supernova;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;

public class Bullet extends Entity {

	public static enum BulletColor {
		RED,
		GREEN,
		VIOLET,
		BLUE
	}

	private static final HashMap<BulletColor, String> imageMap = new HashMap<Bullet.BulletColor, String>();
	
	static {
		imageMap.put(BulletColor.RED, "laser.png");
		imageMap.put(BulletColor.BLUE, "laser_blue.png");
		imageMap.put(BulletColor.GREEN, "laser_green.png");
		imageMap.put(BulletColor.VIOLET, "laser_violet.png");
	}
	
	private static final float speed = 10.0f;
	
	private int texId;
	
	private long index; 
	
	private int direction;
	
	public Bullet(BulletColor color,float xLoc, float yLoc, int direction) {
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		this.index = System.currentTimeMillis();
		this.texId = TextureManager.getInstance().getTexture(imageMap.get(color));	
		this.direction = direction;
		this.size = 5.0f;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		
		if (obj.getClass() == getClass()) {
			Bullet other = (Bullet)obj;
			return (index == other.index);
		}
		
		return false;
	}
	
	public void fly() {
		yLoc += speed*direction;
	}
	
	public void draw() {
		GL11.glColor3f(1.0f,1.0f,1.0f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPushMatrix();
		GL11.glTranslatef(xLoc, yLoc, 0);
		GL11.glScalef(1.5f, 1.5f, 0.0f);		
		GL11.glRotatef(90.0f, 0.0f,0.0f,1.0f);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glVertex2f(-size, -size);
		GL11.glTexCoord2f(0f, 0f);
		GL11.glVertex2f(size, -size);
		GL11.glTexCoord2f(1f, 0f);
		GL11.glVertex2f(size, size);
		GL11.glTexCoord2f(1f, 1f);
		GL11.glVertex2f(-size, size);
		GL11.glTexCoord2f(0f, 1f);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);

	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
	
}
