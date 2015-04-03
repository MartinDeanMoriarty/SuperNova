package de.pueski.supernova.entities;


public abstract class Entity implements IDrawable {

	protected float xLoc;
	protected float yLoc;
	
	protected int energy;
	
	protected float size;
	private boolean visible = true;

	/**
	 * @return the xLoc
	 */
	public float getXLoc() {
		return xLoc;
	}

	/**
	 * @param xLoc the xLoc to set
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
	 * @param yLoc the yLoc to set
	 */
	public void setYLoc(float yLoc) {
		this.yLoc = yLoc;
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
		this.energy = energy;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}	
	
	public boolean isVisible() {
		return visible ;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	
	public boolean collides(Entity entity) {
		float x = entity.getXLoc();
		float y = entity.getYLoc();
		return (x >= xLoc - size && x <= xLoc + size && y >= yLoc - size && y <= yLoc + size);
	}
	
	public abstract void fly();
}
