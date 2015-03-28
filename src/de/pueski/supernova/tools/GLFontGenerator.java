/**

	BlackBoard breadboard designer
	Written and maintained by Matthias Pueski 
	
	Copyright (c) 2010-2011 Matthias Pueski
	
	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

*/
package de.pueski.supernova.tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class GLFontGenerator {

	private static JFrame frame;
	
	public static void main(String[] args) throws Exception {
		
		frame = new JFrame("Test");
		frame.setSize(new Dimension(800,600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setBackground(Color.WHITE);

		BufferedImage[] imageArray = new BufferedImage[256];
		
		int gridSize = 12;
		int letterSize = 24;
		
		int count = 0;
		
		for (int i = 0x21;i < 0x7F;i++ ) {
			imageArray[count] = TextureUtil.createImageFromFont(String.valueOf((char)i), new Font("Monospace",Font.PLAIN,18), Color.WHITE);			
			System.out.println(imageArray[count].getWidth()+"x"+imageArray[count].getHeight());
			System.out.print((char)i);
			count++;
		}
		
		System.out.println();
		
		BufferedImage fontImage = new BufferedImage(gridSize*letterSize, gridSize*letterSize, BufferedImage.TYPE_INT_ARGB);
		
		count = 0;
		
		
		for (int y = 0; y < gridSize; y++) {
			for (int x = 0; x < gridSize; x++) {				
				if (imageArray[count] != null) {
					// center letter
					int correction = (letterSize-imageArray[count].getWidth())/2;
					fontImage.getGraphics().drawImage(imageArray[count], x*letterSize+correction, y*letterSize, frame);
					System.out.println("letterLocations.put(\""+(char)(count+0x21)+"\", new Glyph("+x+","+y+", 1.0f));");
				}
				count++;
			}

		}
		
		ImageIO.write(fontImage, "PNG", new File("c:\\devel\\fonts.png"));
		
//		ImageIcon icon = new ImageIcon(fontImage);	
//		JLabel imageLabel = new JLabel(icon);
//		
//		frame.add(imageLabel, BorderLayout.CENTER);
//		frame.setLocationRelativeTo(frame.getRootPane());
//		frame.setVisible(true);
		
	}
	
	
	
}
