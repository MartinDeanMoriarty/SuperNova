package de.pueski.supernova;

import java.io.InputStream;
import java.util.HashMap;

import de.pueski.supernova.tools.TextureUtil;

public class TextureManager {

	private static TextureManager INSTANCE = null;

	private final HashMap<String, Integer> textures;
	
	private TextureManager() {
		textures = new HashMap<String, Integer>();
		addTexture("aliensprite.png");
		addTexture("alien3.png");
		addTexture("alien4.png");
		addTexture("F5S4.png");
		addTexture("destroyer.png");
		addTexture("destroyer_glow.png");
		addTexture("explosion0.png");
		addTexture("viper.png");
		addTexture("viper_shadow.png");
		addTexture("spaceship_transp.png");
		addTexture("supernova_fg.png");
		addTexture("starfield.png");
		addTexture("laser.png");
		addTexture("laser_blue.png");
		addTexture("laser_green.png");
		addTexture("laser_violet.png");
		addTexture("mars_01.png");
		addTexture("mars_02.png");
		addTexture("mars_03.png");
		addTexture("mars_04.png");
		addTexture("mars_05.png");
		addTexture("mars_06.png");
		addTexture("healthbar.png");
	}
	
	static TextureManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TextureManager();
		}
		return INSTANCE;
	}
	
	int getTexture(String name) {
		return textures.get(name).intValue();
	}
	
	private void addTexture(String textureLocation) {
		System.out.println("Loading texture from "+textureLocation);
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(textureLocation);		
		int id = TextureUtil.loadTexture(is);
		if (!textures.containsKey(textureLocation)) {
			textures.put(textureLocation,Integer.valueOf(id));			
		}
	}
	
}
