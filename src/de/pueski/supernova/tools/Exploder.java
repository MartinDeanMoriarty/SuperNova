package de.pueski.supernova.tools;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Exploder {
	
	public static void main(String[] args) throws Exception {

		BufferedImage[] explosion = new BufferedImage[64];
		
		for (int i= 1; i < 65;i++){			
			
			String zeros = "";
			
			if ( i < 10) {
				zeros = "00";
			}
			else zeros = "0";
			
			File input = new File("c:\\Users\\mpue\\Desktop\\Explosion_878\\Combined PNG\\Explosion_"+zeros+i+".png");
			System.out.println(input.getAbsolutePath());
			explosion[i - 1] = ImageIO.read(input);
		}

		BufferedImage output = new BufferedImage(2048, 2048, BufferedImage.TYPE_INT_ARGB);
		
		int tile = 0;
			
		Graphics2D g = output.createGraphics();
		
		for (int y = 0; y < output.getHeight();y+=256) {
			for (int x = 0; x < output.getWidth();x+=256) {
				g.drawImage(explosion[tile++], x, y, null);
			}			
		}
		
		ImageIO.write(output, "PNG", new File("c:\\Users\\mpue\\Desktop\\xpl\\Explosion.png"));
	}
}
