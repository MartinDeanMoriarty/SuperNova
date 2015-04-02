package de.pueski.supernova;

import org.lwjgl.opengl.GL11;

public class Enemy extends Entity implements IExplodable {

	private static final float speed = 3.0f;

	private int enemyClass = 0;
	
	private int gridSize = 8;

	private int hitPoints;

	private long index;

	private int texId;

	private float x_i = 1f / 8;
	private float y_i = 1f / 8;

	private long lastShotTime;

	private long shotInterval;

	private float rot = 180.0f;

	private int explosionTexId;

	private int explosionIndex = 0;
	
	private float angle = 0.0f;

	public Enemy(float xLoc, float yLoc, int hitPoints, long shotInterval, int enemyClass) {
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		this.hitPoints = hitPoints;
		this.index = System.currentTimeMillis();
		
		if (enemyClass == 0) {
			this.texId = TextureManager.getInstance().getTexture("images/alien3.png");			
		}
		else if (enemyClass == 1) {
			this.texId = TextureManager.getInstance().getTexture("images/aliensprite.png");
		}
		else if (enemyClass == 2){
			this.texId = TextureManager.getInstance().getTexture("images/alien4.png");
		}
		
		this.enemyClass = enemyClass;
		
		this.explosionTexId = TextureManager.getInstance().getTexture(
				"explosion0.png");
		this.lastShotTime = System.currentTimeMillis();
		this.shotInterval = shotInterval;
		this.size = 20.0f;
	}

	/**
	 * @return the hitPoints
	 */
	public int hit() {
		hitPoints--;
		return hitPoints;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;

		if (obj.getClass() == getClass()) {
			Enemy other = (Enemy) obj;
			return (index == other.index);
		}

		return false;
	}

	public void fly() {
		yLoc -= speed;		
		xLoc += (2 * Math.sin(Math.toRadians(angle)));
		angle += 2.0f;
	}

	public void draw() {
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPushMatrix();
		GL11.glTranslatef(xLoc, yLoc, 0);
		if (enemyClass == 2){
			GL11.glScalef(2.0f, 2.0f, 0.0f);
		}
		else {			
			GL11.glScalef(1.5f, 2.0f, 0.0f);
		}
		GL11.glRotatef(rot, 0.0f, 0.0f, 1.0f);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glTexCoord2f(0f, 0f);
		GL11.glVertex2f(-size, -size);
		GL11.glTexCoord2f(1f, 0f);
		GL11.glVertex2f(size, -size);
		GL11.glTexCoord2f(1f, 1f);
		GL11.glVertex2f(size, size);
		GL11.glTexCoord2f(0f, 1f);
		GL11.glVertex2f(-size, size);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	public void drawExplosion(int frame) {

		int column = frame % gridSize;
		int row = frame / gridSize;

		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPushMatrix();
		GL11.glTranslatef(xLoc, yLoc, 0);
		GL11.glScalef(1.5f, 1.5f, 0.0f);
		GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, explosionTexId);
		GL11.glBegin(GL11.GL_POLYGON);
		// bottom left
		GL11.glTexCoord2f(0f + x_i * column, (1f / 8) + y_i * row);
		GL11.glVertex2f(-size * 2, -size * 2);

		// bottom right
		GL11.glTexCoord2f((1f / 8) + x_i * column, (1f / 8) + y_i * row);
		GL11.glVertex2f(size * 2, -size * 2);

		// top right
		GL11.glTexCoord2f((1f / 8) + x_i * column, 0f + y_i * row);
		GL11.glVertex2f(size * 2, size * 2);

		// top left
		GL11.glTexCoord2f(0f + x_i * column, 0f + y_i * row);
		GL11.glVertex2f(-size * 2, size * 2);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);

	}

	public int getExplosionIndex() {
		return explosionIndex;
	}

	public void setExplosionIndex(int explosionIndex) {
		this.explosionIndex = explosionIndex;
	}

	public long getLastShotTime() {
		return lastShotTime;
	}

	public void setLastShotTime(long lastShotTime) {
		this.lastShotTime = lastShotTime;
	}

	public long getShotInterval() {
		return shotInterval;
	}

	public void setShotInterval(long shotInterval) {
		this.shotInterval = shotInterval;
	}

}
