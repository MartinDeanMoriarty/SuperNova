package de.pueski.supernova;

import java.io.InputStream;

import static org.lwjgl.opengl.GL11.*;

import de.pueski.supernova.tools.TextureUtil;

public class Ammo extends Entity {

	private static final float speed = 2.0f;
	
	private int texId;	
	private long index; 

	private int amount = 1000;

	private float rot = 0.0f;
	
	public Ammo(float xLoc, float yLoc, int direction) {
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		this.index = System.currentTimeMillis();
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("ammo.png");		
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
			Ammo other = (Ammo)obj;
			return (index == other.index);
		}
		
		return false;
	}
	
	public void fly() {
		yLoc -= speed;
	}
	
	public void draw() {

		glColor3f(1.0f,1.0f,1.0f);
		
		glEnable(GL_TEXTURE_2D);	
		
		glPushMatrix();
		glTranslatef(xLoc, yLoc, -1);		
		glRotatef(90.0f, 0.0f,0.0f,1.0f);
		glRotatef(rot, 0.0f, 0.0f, 1.0f);
		glBindTexture(GL_TEXTURE_2D, texId);
		glBegin(GL_POLYGON);
		glVertex3f(-size, -size, 0f);
		glTexCoord2f(0f, 0f);
		glVertex3f(size, -size, 0f);
		glTexCoord2f(1f, 0f);
		glVertex3f(size, size,  0f);
		glTexCoord2f(1f, 1f);
		glVertex3f(-size, size, 0f);
		glTexCoord2f(0f, 1f);
		glEnd();
		
		glPopMatrix();
		
		glDisable(GL_TEXTURE_2D);

		rot += 1.0f;
		
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
}
