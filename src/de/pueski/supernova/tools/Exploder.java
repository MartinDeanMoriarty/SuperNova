package de.pueski.supernova.tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

public class Exploder {
	
	public static void main(String[] args) throws Exception {

		final JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setSize(new Dimension(640, 150));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("ABG - Animation billboard generator" );

		JPanel inputFilePanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		
		JLabel inputFileLabel = new JLabel("Input directory");		
		final JTextField inputPathField = new JTextField(40);
		JButton browseInputButton = new JButton("Browse");
		
		browseInputButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(new File("."));
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = chooser.showOpenDialog(frame);				
				if (result == JFileChooser.APPROVE_OPTION) {
					inputPathField.setText(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		inputFilePanel.add(inputFileLabel);
		inputFilePanel.add(inputPathField);
		inputFilePanel.add(browseInputButton);
		
		JPanel outputFilePanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		
		JLabel outputFileLabel = new JLabel("Output directory");
		final JTextField outputPathField = new JTextField(39);
		JButton browseOutputButton = new JButton("Browse");
		
		browseOutputButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(new File("."));
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = chooser.showOpenDialog(frame);				
				if (result == JFileChooser.APPROVE_OPTION) {
					outputPathField.setText(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});

		
		outputFilePanel.add(outputFileLabel);
		outputFilePanel.add(outputPathField);
		outputFilePanel.add(browseOutputButton);
		
		JLabel tileSizeLabel = new JLabel("Tile size(px)");
		final JSpinner tileSizeSpinner = new JSpinner();
		JLabel billboardSizeLabel = new JLabel("Billboard size(px)");
		final JSpinner billboardSizeSpinner = new JSpinner();
		
		tileSizeSpinner.setValue(256);
		billboardSizeSpinner.setValue(2048);
		
		JButton generateButton = new JButton("Generate");
		
		generateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					generateBillboard(new File(inputPathField.getText()), new File(outputPathField.getText()), (Integer)tileSizeSpinner.getValue(), (Integer)billboardSizeSpinner.getValue());
				}
				catch (Exception e1) {
					e1.printStackTrace();
				}
				
			}
		});

		JPanel settingsPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		
		settingsPanel.add(tileSizeLabel);
		settingsPanel.add(tileSizeSpinner);
		settingsPanel.add(billboardSizeLabel);
		settingsPanel.add(billboardSizeSpinner);
		settingsPanel.add(generateButton);
		
		frame.add(inputFilePanel, BorderLayout.NORTH);
		frame.add(outputFilePanel, BorderLayout.CENTER);
		frame.add(settingsPanel, BorderLayout.SOUTH);
		
		frame.setVisible(true);
	}

	private static void generateBillboard(File inputDir, File outputDir, int tileSize, int billboardSize) throws Exception {	
		
		int numTiles = billboardSize / tileSize;
		
		BufferedImage[] explosion = new BufferedImage[numTiles*numTiles];
		
		File[] listFiles = inputDir.listFiles(); 
		
		if (listFiles == null) {
			System.out.println("No input files.");
			return;
		}
		
		ArrayList<File> inputFiles = new ArrayList<File>();
		
		for (File file : listFiles) {
			if (file.getName().endsWith("png") || file.getName().endsWith("PNG")) {
				inputFiles.add(file);
			}
		}
		
		Collections.sort(inputFiles, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				return o1.getName().compareTo(o2.getName());
			}

		});
		
		for (File file : inputFiles) {
			System.out.println(file.getAbsolutePath());
		}
		
		int i = 0;
		
		for (File file : inputFiles) {
			explosion[i++] = ImageIO.read(file);
		}

		BufferedImage output = new BufferedImage(billboardSize, billboardSize, BufferedImage.TYPE_INT_ARGB);
		
		int tile = 0;
			
		Graphics2D g = output.createGraphics();
		
		for (int y = 0; y < output.getHeight();y+=tileSize) {
			for (int x = 0; x < output.getWidth();x+=tileSize) {
				g.drawImage(explosion[tile++], x, y, null);
			}			
		}
		
		ImageIO.write(output, "PNG", new File(outputDir,"out.png"));
	}
}
