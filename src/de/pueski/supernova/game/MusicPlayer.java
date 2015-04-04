package de.pueski.supernova.game;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javazoom.jl.player.Player;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mpatric.mp3agic.Mp3File;

public class MusicPlayer {

	private static final Log log = LogFactory.getLog(MusicPlayer.class);

	private static String inputDir = ".";
	private ArrayList<File> songs;
	private ArrayList<Mp3File> mp3Files;
	private int songIndex = 0;
	private InputStream songInput;
	private volatile Player player;

	private Timer timer;

	private static final ExecutorService executor = Executors.newFixedThreadPool(1);

	public MusicPlayer() throws Exception {

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
			root = new File("./resources/audio");
		}

		log.info("Loading music from " + root.getAbsolutePath());

		File[] files = root.listFiles();

		if (files != null) {

			songs = new ArrayList<File>();
			mp3Files = new ArrayList<Mp3File>();
			
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().endsWith(".mp3")) {
					songs.add(files[i]);
					mp3Files.add(new Mp3File(files[i]));
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

		executor.execute(

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
						log.error("Something went wrong while playing music.");
					}
	
				}
			})
		);

	}

	public String getCurrentSongName() {
		try {
			
			if (mp3Files.get(songIndex).hasId3v1Tag()) {
				return mp3Files.get(songIndex).getId3v1Tag().getArtist()+ " - " + mp3Files.get(songIndex).getId3v1Tag().getTitle(); 
			}
			else if (mp3Files.get(songIndex).hasId3v2Tag()) {
				return  mp3Files.get(songIndex).getId3v2Tag().getArtist() + " - " + mp3Files.get(songIndex).getId3v2Tag().getTitle();
			}
			else {
				return songs.get(songIndex).getName().substring(0, songs.get(songIndex).getName().lastIndexOf("."));	
			}			
			
		}
		catch (IndexOutOfBoundsException ioe) {
			return "no song available";
		}
	}

}