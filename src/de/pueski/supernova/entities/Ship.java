package de.pueski.supernova.entities;

import org.lwjgl.opengl.GL11;

import de.pueski.supernova.game.SoundManager;
import de.pueski.supernova.game.TextureManager;

public class Ship extends Entity implements IExplodable, IDrawable {
	
	private int energy;
	
	private boolean shielded = false;
	
	private int texId;
	private int shadowTexId;
	
	private long index; 
	
	private int explosionTexId;
	
	private int gridSize = 8;
	
	private int explosionIndex;
	
	private boolean visible;
	
	float x_i = 1f / 8;
	float y_i = 1f / 8;
	
	float shieldAlpha = 1.0f;
	
	private Fade fade = Fade.IN;

	private int shootSound;

	private int explosionSource;
	
	private enum Fade {
		IN,
		OUT
	}
	
	public Ship(float xLoc, float yLoc) {
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		this.energy = 100;
		
		this.texId =  TextureManager.getInstance().getTexture("images/destroyer.png");						
		this.shadowTexId = TextureManager.getInstance().getTexture("images/destroyer_glow.png");		
		this.explosionTexId = TextureManager.getInstance().getTexture("images/explosion0.png");
		this.visible = true;
		this.size = 25.0f;
		this.shootSound = SoundManager.getInstance().getSound("audio/laser.wav");
		this.explosionSource = SoundManager.getInstance().getSound("audio/explosion2.wav");
	}

	public boolean hit() {
		if (shielded) {
			return true;
		}
		
		if (energy >= 10) {
			energy -= 10;
			return true;
		}
		return false;		
	}
	
	/**
	 * @return the energy
	 */
	public int getEnergy() {
		return energy;
	}

	/**
	 * @param energy the energy to set
	 */
	public void setEnergy(int energy) {
		if (shielded) {
			return;
		}
		this.energy = energy;
	}
	
	public int getExplosionIndex() {
		return explosionIndex;
	}

	public void setExplosionIndex(int explosionIndex) {
		this.explosionIndex = explosionIndex;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		
		if (obj.getClass() == getClass()) {
			Ship other = (Ship)obj;
			return (index == other.index);
		}
		
		return false;
	}
	
	public void draw() {
		
		if (!visible)
			return;
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		if (shielded) {
		
			if (fade.equals(Fade.IN)) {
				
				if (shieldAlpha < 1.0f) {
					shieldAlpha += 0.005f;
				}
				else {
					shieldAlpha = 1.0f;
					fade = Fade.OUT;
				}
				
			}
			else {
				if (shieldAlpha > 0) {
					shieldAlpha -= 0.005f;
				}
				else {
					shieldAlpha = 0.0f;
					fade = Fade.IN;
				}				
			}
			
		}
		else {
			shieldAlpha = 0.0f;
		}
		
		GL11.glColor4f(1.0f, 1.0f, 1.0f, shieldAlpha);
		GL11.glPushMatrix();
		GL11.glTranslatef(xLoc, yLoc, 0);
		GL11.glScalef(2.0f, 2.7f, 0.0f);
		GL11.glRotatef(180.0f, 0.0f,0.0f,1.0f);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, shadowTexId);
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

		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glPushMatrix();
		GL11.glTranslatef(xLoc, yLoc, 0);
		GL11.glScalef(1.8f, 2.5f, 0.0f);
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

	@Override
	public void fly() {	
	}

	public void shoot() {
		if (!SoundManager.getInstance().isPlayingSound()) {
			SoundManager.getInstance().playEffect(shootSound);	
		}		
	};
	
	public void explode() {
		if (!SoundManager.getInstance().isPlayingSound()) {
			SoundManager.getInstance().playEffect(explosionSource);
		}
	}
	
	public void addEnergy(Energy e) {
		
		energy += e.getAmount();
		
		if (energy > 100) {
			energy = 100;
		}
		
	}

	public boolean isShielded() {
		return shielded;
	}

	public void setShielded(boolean shielded) {
		this.shielded = shielded;
	}

}
