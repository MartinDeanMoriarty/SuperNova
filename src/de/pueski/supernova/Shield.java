package de.pueski.supernova;

import java.io.InputStream;

import org.lwjgl.opengl.GL11;

import de.pueski.supernova.tools.TextureUtil;

public class Shield extends Entity {

	private static final float speed = 2.0f;
	
	private int texId;	
	private long index; 
	
	public Shield(float xLoc, float yLoc, int direction) {
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		this.index = System.currentTimeMillis();
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("images/shield.png");		
		this.texId = TextureUtil.loadTexture(is);
		this.size = 32.0f;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		
		if (obj.getClass() == getClass()) {
			Shield other = (Shield)obj;
			return (index == other.index);
		}
		
		return false;
	}
	
	public void fly() {
		yLoc -= speed;
	}
	
	public void draw() {
		GL11.glColor3f(1.0f,1.0f,1.0f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPushMatrix();
		GL11.glTranslatef(xLoc, yLoc, 0);	
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
	
}
