package de.pueski.supernova;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import de.pueski.supernova.GLBarGraphDisplay.Orientation;
import de.pueski.supernova.IFadeable.Fade;
import de.pueski.supernova.tools.TextureUtil;

public class SuperNova {

	public static final String GAME_TITLE = "SuperNova";

	private static final int WIDTH = 600;
	private static final int HEIGHT = 600;
	private static final int FRAMERATE = 100;
	private static final int MAX_AMMO = 1000;
	
	private static boolean finished;

	static float velocity = 1.0f;
	static float starfieldVelocity = 0.5f;
	static float yOffset = 0.0f;
	static float starfieldYOffset = 0.0f;

	static float xPos = 0;
	static float yPos = 0;
	
	static int ammo = MAX_AMMO;

	static int texId1;
	static int texId2;
	static int texId3;
	static int texId4;
	static int texId5;
	static int texId6;

	static int displayListId;

	static int starfieldTexId;
	static int menuTexId;

	static float shootX;
	static float shootY;

	static long lastShotTime;
	static long lastEnemyTime;
	static long lastEnemyShootTime;

	static Controller defaultController;
	
	static int enemiesShot = 0;

	static Ship ship;

	private static ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private static ArrayList<IExplodable> explodables = new ArrayList<IExplodable>();
	private static ArrayList<IDrawable> drawables = new ArrayList<IDrawable>();
	private static ArrayList<IFadeable> fadeables = new ArrayList<IFadeable>();

	static Random random;

	static int energy = 100;

	static long frameTime;

	static long startTime;
	
	static SoundManager sm;

	static int laserSource;
	static int explosionSource;
	static int energyWarningSource;

	static Text scoreText;
	static Text energyText;
	static Text menuText;
	static Text gameOverText;
	static Text ammoText;
	static Text nowPlaying;
	static Text songName;
	static Text pauseText;
	
	static int score;

	static MusicPlayer musicPlayer;

	static GLBarGraphDisplay energyDisplay;

	static GameState gameState = GameState.MENU;

	private SuperNova() {
	}

	public static void main(String args[]) throws Exception {
		init();
		run();
		cleanup();
		System.exit(0);
	}

	private static void init() throws Exception {
		Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
		Display.setTitle("SuperNova");
		Display.setFullscreen(true);
		// Display.setVSyncEnabled(true);
		Display.create();
		Keyboard.create();
		Mouse.create();
		Mouse.setGrabbed(true);

		Controllers.create();		
		Controllers.poll();
		
		for (int i = 0; i < Controllers.getControllerCount();i++) {
			Controller controller = Controllers.getController(i);			
			if (controller.getName().startsWith("XBOX")) {
				defaultController = controller;				
			}
		}

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, Display.getDisplayMode().getWidth(), 0.0D, Display.getDisplayMode().getHeight(), -1D, 1.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glViewport(0, 0, Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		loadTextures();
		lastShotTime = System.currentTimeMillis();
		lastEnemyTime = System.currentTimeMillis();
		lastEnemyShootTime = System.currentTimeMillis();
		frameTime = System.currentTimeMillis();
		random = new Random();
		ship = new Ship(300.0f, 100.0f);
		xPos = ship.getXLoc();
		yPos = ship.getYLoc();
		sm = new SoundManager();
		sm.initialize(256);
		laserSource = sm.addSound("laser.wav");
		explosionSource = sm.addSound("explosion.wav");
		energyWarningSource = sm.addSound("energywarning.wav");

		sm.adjustAllVolumes(0.4f);
		sm.adjustVolume(2, 2.0f);

		scoreText = new Text(10, 570, "Score " + score);
		energyText = new Text(270, 570, "Energy");
		menuText = new Text(190, 300, "Press Space to start.");
		gameOverText = new Text(190, 300, "Game over! Score : " + score);
		ammoText = new Text(10, 550, "Ammo " + ammo);
		nowPlaying = new Text(10, 40, "Now playing");
		songName = new Text(10, 20, "");
		pauseText = new Text(280, 300, "Pause");

		nowPlaying.setOpacity(0.0f);
		songName.setOpacity(0.0f);

		energyDisplay = new GLBarGraphDisplay(100, 1.5f, 180f, 405f, true, true);
		energyDisplay.setScale(2.0f);
		energyDisplay.setYscale(1.4f);
		energyDisplay.setOrientation(Orientation.HORIZONTAL);
		energyDisplay.setBlinkInterval(250);

		drawables.add(energyDisplay);

		musicPlayer = new MusicPlayer();

		displayListId = GL11.glGenLists(1);
		GL11.glNewList(displayListId, GL11.GL_COMPILE);
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		TextureUtil.drawTexturedSquare(0, 300, 3900, 600, texId1);
		TextureUtil.drawTexturedSquare(0, 300, 3300, 600, texId6);
		TextureUtil.drawTexturedSquare(0, 300, 2700, 600, texId5);
		TextureUtil.drawTexturedSquare(0, 300, 2100, 600, texId4);
		TextureUtil.drawTexturedSquare(0, 300, 1500, 600, texId3);
		TextureUtil.drawTexturedSquare(0, 300, 900, 600, texId2);
		TextureUtil.drawTexturedSquare(0, 300, 300, 600, texId1);
		GL11.glPopMatrix();
		GL11.glEndList();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	private static void loadTextures() {
		texId1 = TextureManager.getInstance().getTexture("mars_06.png");
		texId2 = TextureManager.getInstance().getTexture("mars_05.png");
		texId3 = TextureManager.getInstance().getTexture("mars_04.png");
		texId4 = TextureManager.getInstance().getTexture("mars_03.png");
		texId5 = TextureManager.getInstance().getTexture("mars_02.png");
		texId6 = TextureManager.getInstance().getTexture("mars_01.png");
		menuTexId = TextureManager.getInstance().getTexture("supernova.png");
		starfieldTexId = TextureManager.getInstance().getTexture("starfield.png");
	}

	private static void run() {

		musicPlayer.start();
		songName.setText(musicPlayer.getCurrentSongName());

		do {
			if (finished)
				break;
			Display.update();
			if (Display.isCloseRequested()) {
				finished = true;
			}
			else if (Display.isActive()) {
				logic();
				render();
				Display.sync(FRAMERATE);
			}
			else {
				try {
					Thread.sleep(100L);
				}
				catch (InterruptedException e) {
				}
				logic();
				if (Display.isVisible() || Display.isDirty()) {
					render();
				}
			}

		} while (true);
	}

	private static void cleanup() {
		sm.destroy();
		Display.destroy();
		Keyboard.destroy();
		Mouse.destroy();
		Controllers.destroy();
	}

	private static void logic() {

		switch (gameState) {

		case RUNNING:
			processRunningStateLogic();
			break;
		case PAUSE: 
			processPauseLogic();
			break;
		case GAME_OVER:
			processGameOverLogic();
			break;
		case MENU:
			processMenuLogic();
			break;
		case LEVEL_END:
			processLevelEndLogic();
			break;
		default:
			break;

		}

	}

	private static void processPauseLogic() {		
		if (Keyboard.isKeyDown(Keyboard.KEY_P)) {			
			gameState = GameState.RUNNING;
			Keyboard.enableRepeatEvents(true);
		}
	}

	private static void processLevelEndLogic() {
	}

	private static void processMenuLogic() {

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) || ((defaultController != null) && defaultController.isButtonPressed(0))) {
			musicPlayer.nextSong();
			score = 0;
			ship.setEnergy(100);
			energyDisplay.setBlinking(false);
			energyDisplay.setValue(100);
			scoreText.setText("Score : " + score);
			gameState = GameState.RUNNING;
			startTime = System.currentTimeMillis();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			finished = true;
		}

		yOffset += velocity;

		if (yOffset >= HEIGHT)
			yOffset = 0;

	}

	private static void processGameOverLogic() {

		createRandomEnemies();
		enemiesShoot();
		moveBullets();
		moveEnemies();

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			musicPlayer.nextSong();
			enemies.clear();
			bullets.clear();
			gameState = GameState.MENU;
		}

		yOffset += velocity;

		if (yOffset >= HEIGHT)
			yOffset = 0;

		starfieldYOffset += starfieldVelocity;

		if (starfieldYOffset >= HEIGHT)
			starfieldYOffset = 0;

	}

	private static void enemiesShoot() {
		for (Enemy enemy : enemies) {
			if (System.currentTimeMillis() - enemy.getLastShotTime() > enemy.getShotInterval()) {
				bullets.add(new Bullet(enemy.getXLoc(), enemy.getYLoc(), -1));
				enemy.setLastShotTime(System.currentTimeMillis());
				if (!sm.isPlayingSound())
					sm.playEffect(laserSource);
			}
		}
	}

	private static void createRandomEnemies() {
		if (System.currentTimeMillis() - lastEnemyTime > 500 && System.currentTimeMillis() - startTime > 5000) {
			float f = random.nextFloat();
			long t = random.nextInt(500);
			enemies.add(new Enemy(WIDTH * f, HEIGHT, 4, 1000 + t));
			lastEnemyTime = System.currentTimeMillis();
		}
	}

	private static void processRunningStateLogic() {

		createRandomEnemies();
		enemiesShoot();

		shoot();

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			gameState = GameState.MENU;
			enemies.clear();
			bullets.clear();
			musicPlayer.nextSong();
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			yPos += 2.0f;
			velocity += 0.05f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			yPos -= 2.0f;
			velocity -= 0.05f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
			xPos -= 5.0f;
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
			xPos += 5.0f;
		if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
			sm.playEffect(energyWarningSource);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_N)  || ((defaultController != null) && defaultController.isButtonPressed(4))) {
			musicPlayer.previousSong();
			songName.setText(musicPlayer.getCurrentSongName());
			nowPlaying.setMode(Fade.IN);
			songName.setMode(Fade.IN);
			fadeables.add(nowPlaying);
			fadeables.add(songName);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_M)  || ((defaultController != null) && defaultController.isButtonPressed(5))) {
			musicPlayer.nextSong();
			songName.setText(musicPlayer.getCurrentSongName());
			nowPlaying.setMode(Fade.IN);
			songName.setMode(Fade.IN);
			fadeables.add(nowPlaying);
			fadeables.add(songName);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_K)) {
			if (!explodables.contains(ship)) {
				explodables.add(ship);
				ship.setVisible(false);
				sm.playEffect(explosionSource);
			}
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
			Keyboard.enableRepeatEvents(false);
			gameState = GameState.PAUSE;			
		}
		
		if (defaultController != null) {
			if (Math.abs(defaultController.getXAxisValue()) > 0.3) {
				xPos += defaultController.getXAxisValue() * 5;
			}
			if (Math.abs(defaultController.getYAxisValue()) > 0.3) {
				yPos -= defaultController.getYAxisValue() * 5;
			}
			
		}
		else {
			xPos = Mouse.getX();
			yPos = Mouse.getY();			
		}

		if (xPos < 60) {
			xPos = 60;
		}
		if (xPos > 540) {
			xPos = 540;
		}

		if (yPos < 60) {
			yPos = 60;
		}
		if (yPos > 540) {
			yPos = 540;
		}

		
		System.out.println(yPos);
		
		shootX = xPos;
		shootY = yPos;

		moveBullets();
		moveEnemies();

		for (Iterator<Bullet> bulletIt = bullets.iterator(); bulletIt.hasNext();) {

			Bullet bullet = bulletIt.next();

			// Any enemy hit ?
			for (Iterator<Enemy> it = enemies.iterator(); it.hasNext();) {
				Enemy enemy = it.next();
				if (bullet.getDirection() == 1 && enemy.collides(bullet)) {
					if (enemy.hit() == 0) {
						it.remove();
						explodables.add(enemy);
						sm.playEffect(explosionSource);
						enemiesShot++;
						score = enemiesShot * 1000;
						scoreText.setText("Score : " + score);
					}
				}

			}
			// Am I hit by a bullet
			if (bullet.getDirection() == -1 && ship.collides(bullet)) {
				if (!ship.hit()) {
					if (ship.getEnergy() == 0) {
						if (!explodables.contains(ship)) {
							sm.playEffect(explosionSource);
							explodables.add(ship);
							gameOverText.setText("Game over! Score : " + score);
							score = 0;
							ship.setEnergy(100);
							energyDisplay.setValue(100);
							scoreText.setText("Score : " + score);
							gameState = GameState.GAME_OVER;
						}
					}
				}
				else {
					bulletIt.remove();
					energyDisplay.setValue(ship.getEnergy());
					if (ship.getEnergy() == 20) {
						sm.playEffect(energyWarningSource);
					}
				}
			}
		}

		// Am I hit by an enemy ?

		for (Iterator<Enemy> it = enemies.iterator(); it.hasNext();) {

			Enemy enemy = it.next();

			if (ship.collides(enemy)) {

				if (!ship.hit()) {
					if (!explodables.contains(ship)) {
						sm.playEffect(explosionSource);
						explodables.add(ship);
						gameOverText.setText("Game over! Score : " + score);
						score = 0;
						scoreText.setText("Score : " + score);
						ship.setEnergy(100);
						energyDisplay.setValue(100);
						gameState = GameState.GAME_OVER;
					}
				}
				else {
					it.remove();
					explodables.add(enemy);
					sm.playEffect(explosionSource);
					enemiesShot++;
					energyDisplay.setValue(ship.getEnergy());
					if (ship.getEnergy() == 20) {
						sm.playEffect(energyWarningSource);
					}

				}
			}
		}

		yOffset += velocity;

		if (yOffset >= 3600)
			yOffset = 0;

		starfieldYOffset += starfieldVelocity;

		if (starfieldYOffset >= HEIGHT)
			starfieldYOffset = 0;

		ship.setXLoc(xPos);
		ship.setYLoc(yPos);

		energyDisplay.setBlinking(ship.getEnergy() <= 20);

	}

	private static void moveEnemies() {
		for (Iterator<Enemy> it = enemies.iterator(); it.hasNext();) {

			Enemy enemy = it.next();

			if (enemy.getYLoc() < 0) {
				it.remove();
			}
			else {
				enemy.fly();
			}

		}
	}

	private static void moveBullets() {
		for (Iterator<Bullet> it = bullets.iterator(); it.hasNext();) {

			Bullet bullet = it.next();

			if (bullet.getYLoc() > 600 && bullet.getYLoc() < 0) {
				it.remove();
			}
			else {
				bullet.fly();
			}

		}
	}

	private static void shoot() {
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) || Mouse.isButtonDown(0) || ((defaultController != null) && defaultController.isButtonPressed(0))) {
			if (ammo > 0) {
				if (System.currentTimeMillis() - lastShotTime > 200) {
					bullets.add(new Bullet(shootX + 8, shootY, 1));
					bullets.add(new Bullet(shootX + -8, shootY, 1));
					bullets.add(new Bullet(shootX + 43, shootY - 8, 1));
					bullets.add(new Bullet(shootX + -43, shootY - 8, 1));
					lastShotTime = System.currentTimeMillis();
					if (!sm.isPlayingSound())
						sm.playEffect(laserSource);
				}
				ammo--;
				ammoText.setText("Ammo " + ammo);				
			}
		}
	}

	private static void render() {

		switch (gameState) {

			case RUNNING:
				processRunningStateRender();
				break;
			case PAUSE:
				processPauseRender();
				break;
			case GAME_OVER:
				processGameOverRender();
				break;
			case MENU:
				processMenuRender();
				break;
			case LEVEL_END:
				processLevelEndRender();
				break;
			default:
				break;

		}

	}

	private static void processPauseRender() {

		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glColor3f(1.0f, 1.0f, 1.0f);


		GL11.glPushMatrix();
		GL11.glTranslatef(0, -yOffset, 0);
		GL11.glCallList(displayListId);
		GL11.glPopMatrix();

		for (Bullet bullet : bullets) {
			bullet.draw();
		}
		for (Enemy enemy : enemies) {
			enemy.draw();
		}

		ship.draw();
		scoreText.draw();
		energyText.draw();
		ammoText.draw();
		pauseText.draw();

		for (IDrawable drawable : drawables) {
			drawable.draw();
		}
		
	}

	private static void processLevelEndRender() {
		// TODO Auto-generated method stub

	}

	private static void processMenuRender() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		TextureUtil.drawTexture(0, 300, 300, 300, menuTexId);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	private static void processGameOverRender() {

		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glPushMatrix();
		GL11.glTranslatef(0, -yOffset, 0);
		GL11.glCallList(displayListId);
		GL11.glPopMatrix();

		// GL11.glDisable(GL11.GL_TEXTURE_2D);

		for (Enemy enemy : enemies) {
			enemy.draw();
		}

		scoreText.draw();
		energyText.draw();

		for (Iterator<IExplodable> it = explodables.iterator(); it.hasNext();) {
			IExplodable explodable = it.next();

			if (explodable.getExplosionIndex() < 63) {
				explodable.setExplosionIndex(explodable.getExplosionIndex() + 1);
				explodable.drawExplosion(explodable.getExplosionIndex());
			}
			else {
				it.remove();
			}

		}

		for (IDrawable drawable : drawables) {
			drawable.draw();
		}

		gameOverText.draw();

	}

	private static void processRunningStateRender() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glPushMatrix();
		GL11.glTranslatef(0, -yOffset, 0);
		GL11.glCallList(displayListId);
		GL11.glPopMatrix();

		for (Bullet bullet : bullets) {
			bullet.draw();
		}
		for (Enemy enemy : enemies) {
			enemy.draw();
		}

		ship.draw();

		for (Iterator<IExplodable> it = explodables.iterator(); it.hasNext();) {
			IExplodable explodable = it.next();

			if (explodable.getExplosionIndex() < 63) {
				explodable.setExplosionIndex(explodable.getExplosionIndex() + 1);
				explodable.drawExplosion(explodable.getExplosionIndex());
			}
			else {
				if (explodable instanceof Ship) {
					gameState = GameState.GAME_OVER;
					explodables.remove(ship);
				}
				it.remove();
			}

		}

		float fadeInSpeed = 0.001f;
		float fadeOutSpeed = 0.001f;

		for (Iterator<IFadeable> it = fadeables.iterator(); it.hasNext();) {

			IFadeable fadeable = it.next();

			if (fadeable.getMode() == Fade.IN) {

				if (fadeable.getOpacity() < 1.0f) {
					fadeable.setOpacity(fadeable.getOpacity() + fadeInSpeed);
				}
				else {
					fadeable.setMode(Fade.OUT);
				}

			}
			else {
				if (fadeable.getOpacity() > 0.0f) {
					fadeable.setOpacity(fadeable.getOpacity() - fadeOutSpeed);
				}
				else {
					it.remove();
				}

			}

		}

		scoreText.draw();
		energyText.draw();
		ammoText.draw();
		nowPlaying.draw();
		songName.draw();

		for (IDrawable drawable : drawables) {
			drawable.draw();
		}

	}

	public static int getEnemiesShot() {
		return enemiesShot;
	}

	public static Ship getShip() {
		return ship;
	}

}
