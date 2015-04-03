package de.pueski.supernova.entities;

public interface IFadeable {
	
	public enum Fade {
		IN,
		OUT
	}
	
	public void setOpacity(float opacity);
	
	public float getOpacity();

	public Fade getMode();
	
	public void setMode(Fade mode);
	
}
