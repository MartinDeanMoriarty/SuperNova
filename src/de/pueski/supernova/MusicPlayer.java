package de.pueski.supernova;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javazoom.jl.player.Player;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MusicPlayer {

	private static final Log log = LogFactory.getLog(MusicPlayer.class);
	
	private static String inputDir = ".";	
	private ArrayList<File> songs;	
	private int songIndex = 0;	
	private InputStream songInput;	
	private volatile Player player; 
	
	private Timer timer;
	
	public MusicPlayer() {		
				
		timer = new Timer(true);		
		timer.scheduleAtFixedRate(new TimerTask() {			
			@Override
			public void run() {
				if (player != null && player.isComplete()) {
					nextSong();
				}				
			}
		}, new Date(), 10000);
		
		try {
			Properties p = new Properties();
			p.load(new FileInputStream(new File("SuperNova.properties")));
			inputDir = p.getProperty("mp3dir");
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
		
		File root = new File(inputDir);
		
		if (!root.exists() || root.listFiles() == null) {
			root = new File("./resources");
		}
		
		log.info("Loading music from "+root.getAbsolutePath());
		
		File[] files = root.listFiles();
		
		if (files != null) {
			
			songs = new ArrayList<File>();
			
			for (int i=0; i < files.length;i++) {
				if (files[i].getName().endsWith(".mp3")) {
					songs.add(files[i]);
				}
			}
			
			if (songs.size() > 0) {
				// select a random song at the beginning
				Random r = new Random();
				songIndex = r.nextInt(songs.size());
				
				try {
					songInput = new FileInputStream(songs.get(songIndex));
				}
				catch (FileNotFoundException e) {
					log.error("File not found.");
				}
				
			}
			
		}

	}

	public void start() {		
		play();		
	}

	public void close() {
		player.close();
	}
	
	public void playSong(int songIndex) {
		
		try {
			songInput = new FileInputStream(songs.get(songIndex));
		}
		catch (FileNotFoundException e) {
			log.error("File not found.");
		}
		
		play();		
	}
	
	public synchronized void nextSong() {
		
		if (songIndex < songs.size() - 1) {
			songIndex++;
		}
		else {
			songIndex = 0;
		}
		
		if (player != null) {
			player.close();
		}
		
		try {
			songInput = new FileInputStream(songs.get(songIndex));
		}
		catch (FileNotFoundException e) {
			log.error("File not found.");
		}
		
		play();
	}
	
	public synchronized void previousSong() {
		
		if (songIndex > 0) {
			songIndex--;
		}
		else {
			songIndex = songs.size() - 1;
		}
		
		if (player != null) {
			player.close();
		}
		
		try {
			songInput = new FileInputStream(songs.get(songIndex));
		}
		catch (FileNotFoundException e) {
			log.error("File not found.");
		}
		
		play();
	}

	public synchronized void play() {
		new Thread(new Runnable() {
			
			@Override
			public synchronized void run() {
				try {
					if (songInput != null) {
						player = new Player(songInput);			
						player.play();						
					}
					else {
						log.error("Unable to play song input.");
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();		
		
	}
	
	public String getCurrentSongName() {
		try {
			return songs.get(songIndex).getName();			
		}
		catch (IndexOutOfBoundsException ioe) {
			return "no song available";
		}
	}
	
}