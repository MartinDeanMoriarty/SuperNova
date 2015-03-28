package de.pueski.supernova;

import java.io.InputStream;
import java.util.HashMap;

import de.pueski.supernova.tools.TextureUtil;

public class TextureManager {

	private static TextureManager INSTANCE = null;

	private final HashMap<String, Integer> textures;
	
	private TextureManager() {
		textures = new HashMap<String, Integer>();
		addTexture("archimedes.png");
		addTexture("explosion0.png");
		addTexture("viper.png");
		addTexture("viper_shadow.png");
		addTexture("spaceship_transp.png");
		addTexture("supernova.png");
		addTexture("starfield.png");
		addTexture("1_Mars.png");
		addTexture("2_Mars.png");
		addTexture("3_Mars.png");
		addTexture("4_Mars.png");
		addTexture("5_Mars.png");
		addTexture("6_Mars.png");
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
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(textureLocation);		
		int id = TextureUtil.loadTexture(is);
		if (!textures.containsKey(textureLocation)) {
			textures.put(textureLocation,Integer.valueOf(id));			
		}
	}
	
}
