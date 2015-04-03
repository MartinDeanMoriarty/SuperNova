package de.pueski.supernova.game;

import java.io.InputStream;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.pueski.supernova.tools.TextureUtil;

public class TextureManager {

	private static final Log log = LogFactory.getLog(TextureManager.class);
	
	private static TextureManager INSTANCE = null;

	private final HashMap<String, Integer> textures;
	
	private TextureManager() {
		textures = new HashMap<String, Integer>();
		addTexture("images/aliensprite.png");
		addTexture("images/alien3.png");
		addTexture("images/alien4.png");
		addTexture("images/F5S4.png");
		addTexture("images/destroyer.png");
		addTexture("images/destroyer_glow.png");
		addTexture("images/explosion0.png");
		addTexture("images/viper.png");
		addTexture("images/explosion1.png");
		addTexture("images/viper_shadow.png");
		addTexture("images/spaceship_transp.png");
		addTexture("images/supernova_fg.png");
		addTexture("images/starfield.png");
		addTexture("images/laser.png");
		addTexture("images/laser_blue.png");
		addTexture("images/laser_green.png");
		addTexture("images/laser_violet.png");
		addTexture("images/mars_01.png");
		addTexture("images/mars_02.png");
		addTexture("images/mars_03.png");
		addTexture("images/mars_04.png");
		addTexture("images/mars_05.png");
		addTexture("images/mars_06.png");
		addTexture("images/healthbar.png");
	}
	
	public static TextureManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TextureManager();
		}
		return INSTANCE;
	}
	
	public int getTexture(String name) {
		return textures.get(name).intValue();
	}
	
	private void addTexture(String textureLocation) {
		log.info("Loading texture from "+textureLocation);
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(textureLocation);		
		int id = TextureUtil.loadTexture(is);
		if (!textures.containsKey(textureLocation)) {
			textures.put(textureLocation,Integer.valueOf(id));			
		}
	}
	
}
