package de.pueski.supernova;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MusicPlayer {

	private static String inputDir = ".";	
	private ArrayList<File> songs;	
	private int songIndex = 0;	
	private InputStream songInput;	
	private Player player; 
	
	public MusicPlayer() {		
		
		try {
			Properties p = new Properties();
			p.load(new FileInputStream(new File("SuperNova.properties")));
			inputDir = p.getProperty("mp3dir");
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
		
		File root = new File(inputDir);
		
		File[] files = root.listFiles();

		songs = new ArrayList<File>();
		
		for (int i=0; i < files.length;i++) {
			if (files[i].getName().endsWith(".mp3")) {
				songs.add(files[i]);
			}
		}

		// select a random song at the beginning
		Random r = new Random();
		songIndex = r.nextInt(songs.size());
		
		try {
			songInput = new FileInputStream(songs.get(songIndex));
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
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
			e.printStackTrace();
		}

		
		play();		
	}
	
	public void nextSong() {
		
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
			e.printStackTrace();
		}
		
		play();
	}
	
	public void previousSong() {
		
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
			e.printStackTrace();
		}
		
		play();
	}

	public void play() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					player = new Player(songInput);			
					player.play();
				}
				catch (JavaLayerException e) {
					e.printStackTrace();
				}
			}
		}).start();		
	}
	
	public String getCurrentSongName() {
		return songs.get(songIndex).getName();
	}
	
}