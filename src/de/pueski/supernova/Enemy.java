package de.pueski.supernova;

import org.lwjgl.opengl.GL11;

public class Enemy implements IExplodable {

	private float xLoc;
	private float yLoc;

	private static final float speed = 3.0f;
	private static final float size = 20.0f;

	private int gridSize = 8;

	private int hitPoints;

	private long index;

	private int texId;

	private float x_i = 1f / 8;
	private float y_i = 1f / 8;

	private long lastShotTime;

	private long shotInterval;
	
	private int explosionTexId;

	private int explosionIndex = 0;

	public Enemy(float xLoc, float yLoc, int hitPoints, long shotInterval) {
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		this.hitPoints = hitPoints;
		this.index = System.currentTimeMillis();
		this.texId = TextureManager.getInstance().getTexture("archimedes.png");
		this.explosionTexId = TextureManager.getInstance().getTexture("explosion0.png");
		this.lastShotTime = System.currentTimeMillis();
		this.shotInterval = shotInterval;
	}

	/**
	 * @return the hitPoints
	 */
	public int hit() {
		hitPoints--;
		return hitPoints;
	}

	/**
	 * @return the xLoc
	 */
	public float getXLoc() {
		return xLoc;
	}

	/**
	 * @param xLoc
	 *            the xLoc to set
	 */
	public void setXLoc(float xLoc) {
		this.xLoc = xLoc;
	}

	/**
	 * @return the yLoc
	 */
	public float getYLoc() {
		return yLoc;
	}

	/**
	 * @param yLoc
	 *            the yLoc to set
	 */
	public void setYLoc(float yLoc) {
		this.yLoc = yLoc;
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
	}

	public void draw() {
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPushMatrix();
		GL11.glTranslatef(xLoc, yLoc, 0);
		GL11.glScalef(1.5f, 1.5f, 0.0f);
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

	public boolean collides(Bullet bullet) {
		float x = bullet.getXLoc();
		float y = bullet.getYLoc();
		return (x >= xLoc - size && x <= xLoc + size && y >= yLoc - size && y <= yLoc + size);
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
