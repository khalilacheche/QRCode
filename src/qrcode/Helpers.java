package qrcode;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public final class Helpers {
	
	//TODO modify PATH_HEADER if images do not load properly
	private static final String SEP = File.separator;
	private static final String PATH_HEADER = "images" + SEP;
	
	
	private static final int BACKGROUND_COLOR = -1;
	private static final int SCALE = 10;
	private static final int BORDER = 4*SCALE; //quiet zone is 4 module large
	
	private static final int GREEN_BLACK = 0xFF_00_60_00;
	private static final int GREEN_WHITE = 0xFF_90_FF_90;
	private static final int RED = 0xFF_80_00_00;
	
	
	/**
	 * compare a matrix loaded from file with a 2D-array given in arguments.
	 * @param matrix the 2-dimensional array
	 * @param imagePath the path of the image to compare with the matrix
	 * @return true if the 2 images are similar, false otherwise
	 */
	public static boolean compare(int [][] matrix,String imagePath) {
		int[][] expected= readMatrix(imagePath);
		if(expected.length != matrix.length || expected.length!=expected[0].length || matrix.length!=matrix[0].length) {
			throw new IllegalArgumentException("The size of the two QR code does not match: matrix:"+matrix.length+"  image:"+expected.length);
		}
		int[][] diff = new int[expected.length][expected.length];
		boolean similar = true;
		for(int x=0;x<matrix.length;x++) {
			for(int y=0;y<matrix.length;y++) {
				if(matrix[x][y]!=expected[x][y]) {
					diff[x][y] = RED;
					similar = false;
				}else {
					if(expected[x][y]== -1) {
						diff[x][y] = GREEN_WHITE;
					}else if(expected[x][y]== -16777216){
						diff[x][y] = GREEN_BLACK;
					}
				}
			}
		}
		BufferedImage imageExpected = scale(expected, SCALE, BORDER/2);
		BufferedImage imageGiven = scale(matrix, SCALE, BORDER/2);
		BufferedImage imagediff = scale(diff, SCALE, BORDER/2);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame editorFrame = new JFrame("Compare with "+imagePath);
				editorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	
				
				ImageIcon imageIcon1 = new ImageIcon(imageExpected);
				JLabel jLabel1 = new JLabel(imageIcon1);
				JPanel pane1 = new JPanel(new BorderLayout());
				JLabel text1 = new JLabel("Expected matrix");
				text1.setHorizontalAlignment(SwingConstants.CENTER);
				text1.setFont(new Font("",Font.PLAIN, 24));
				pane1.add(jLabel1,BorderLayout.SOUTH);
				pane1.add(text1,BorderLayout.NORTH);
				
				
				ImageIcon imageIcon2 = new ImageIcon(imagediff);
				JLabel jLabel2 = new JLabel();
				jLabel2.setIcon(imageIcon2);
				jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
				JPanel pane2 = new JPanel(new BorderLayout());
				JLabel text2 = new JLabel("Difference between the 2 images");
				text2.setHorizontalAlignment(SwingConstants.CENTER);
				text2.setFont(new Font("",Font.PLAIN, 24));
				pane2.add(jLabel2,BorderLayout.SOUTH);
				pane2.add(text2,BorderLayout.NORTH);
				
				
				ImageIcon imageIcon3 = new ImageIcon(imageGiven);
				JLabel jLabel3 = new JLabel();
				jLabel3.setIcon(imageIcon3);
				JPanel pane3 = new JPanel(new BorderLayout());
				JLabel text3 = new JLabel("Your result");
				text3.setHorizontalAlignment(SwingConstants.CENTER);
				text3.setFont(new Font("",Font.PLAIN, 24));
				pane3.add(jLabel3,BorderLayout.SOUTH);
				pane3.add(text3,BorderLayout.NORTH);
				

				editorFrame.getContentPane().add(pane1, BorderLayout.WEST);
				editorFrame.getContentPane().add(pane2, BorderLayout.SOUTH);
				editorFrame.getContentPane().add(pane3, BorderLayout.EAST);
				
				editorFrame.pack();
				editorFrame.setLocationRelativeTo(null);
				editorFrame.setVisible(true);
			}
		});
		
		return similar;
		
	}

	/**
	 * Shows a matrix in a new window. The matrix is scaled for visualization
	 * 
	 * @param matrix
	 * @param scale 
	 */
	public static void show(int[][] matrix, int scale) {
		BufferedImage qrCode = Helpers.matrixToImage(matrix);
		BufferedImage image = reshape(qrCode, scale, 4*scale);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame editorFrame = new JFrame("QR Code");
				editorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	
				ImageIcon imageIcon = new ImageIcon(image);
				JLabel jLabel = new JLabel();
				jLabel.setIcon(imageIcon);
				editorFrame.getContentPane().add(jLabel, BorderLayout.CENTER);
	
				editorFrame.pack();
				editorFrame.setLocationRelativeTo(null);
				editorFrame.setVisible(true);
			}
		});
	}

	/**
	 * Read an image from a file in the images directory and return the matrix
	 * associated to it
	 * 
	 * @param name
	 *            The name of the image
	 * @return The loaded matrix of the image
	 */
	public static int[][] readMatrix(String name) {
		return imageToMatrix(Helpers.read(name));
	}

	/**
	 * Write the matrix into an image file
	 * @param name
	 *            the name of the file
	 * @param matrix
	 *            the matrix to write
	 */
	public static void writeMatrix(String name, int[][] matrix) {
		write(name, matrixToImage(matrix));
	}
	
	/*
	 * ================================================ 
	 * 				PRIVATE METHODS
	 *   			Cannot be used. 
	 * ================================================
	 */


	/**
	 * Transform an image into a 2 dimensional array with the corresponding pixel
	 * 
	 * @param image
	 *            the image to be transformed into an array
	 * @return the matrix of the image
	 */
	private static int[][] imageToMatrix(BufferedImage image) {
	
		int[][] matrix = new int[image.getWidth()][image.getHeight()];
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				matrix[i][j] = image.getRGB(i, j);
			}
		}
		return matrix;
	}

	/**
	 * Transform a 2 dimensional int array into an image
	 * 
	 * @param matrix
	 *            the 2-dimensional array representing the QR code
	 * @return The image of the QR code
	 */
	private static BufferedImage matrixToImage(int[][] matrix) {
		BufferedImage image = new BufferedImage(matrix.length, matrix[0].length, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < matrix.length; x++) {
			for (int y = 0; y < matrix[0].length; y++) {
				image.setRGB(x, y, matrix[x][y]);
			}
		}

		return image;
	}

	/**
	 * Write an image on a file
	 * @param name
	 *            the name of the file
	 * @param image
	 *            the image to write on disk
	 */
	private static void write(String name, BufferedImage image) {
		String projectPath = System.getProperty("user.dir");
		try {
			// Output file path
			String path = projectPath + SEP +  PATH_HEADER + name;
			if (!name.contains(".png")) {
				path = path + ".png";
			}
			File output_file = new File(path);

			// Writing to file taking type and path as
			ImageIO.write(image, "png", output_file);

			//System.out.println("Writing complete.");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Read an Image from a file from the images directory
	 * 
	 * @param name
	 *            The name of the image
	 * @return The loaded image
	 */
	private static BufferedImage read(String name) {
		String projectPath = System.getProperty("user.dir");
		String path = projectPath + SEP + PATH_HEADER + name;
		try {
			
			if (!name.contains(".png")) {
				path = path + ".png";
			}
			File pathToFile = new File(path);
			BufferedImage image = ImageIO.read(pathToFile);
			return image;
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException("The image '"+path+"' does not exist or could not load");
		}
	}
	
	private static BufferedImage scale(int[][] image, int scale, int borderSize) {
		return reshape(Helpers.matrixToImage(image), scale, borderSize);
	}
	private static BufferedImage reshape(BufferedImage image, int scale, int borderSize) {
		if (image.getHeight() != image.getWidth()) {
			throw new IllegalArgumentException("The image must be squared");
		}
		int previousSize = image.getWidth();
		int size = previousSize * scale + borderSize * 2;
		BufferedImage ehancedIm = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

		/*
		 * Border
		 */
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < borderSize; j++) {
				ehancedIm.setRGB(i, j, BACKGROUND_COLOR);
				ehancedIm.setRGB(i, j + size - borderSize, BACKGROUND_COLOR);
				if (i >= borderSize && i < size - borderSize) {
					ehancedIm.setRGB(j, i, BACKGROUND_COLOR);
					ehancedIm.setRGB(j + size - borderSize, i, BACKGROUND_COLOR);
				}
			}
		}

		/*
		 * Scaling
		 */
		for (int x = 0; x < previousSize; x++) {
			for (int y = 0; y < previousSize; y++) {

				int color = image.getRGB(x, y);
				int startX = x * scale + borderSize;
				int startY = y * scale + borderSize;

				for (int i = startX; i < startX + scale; i++) {
					for (int j = startY; j < startY + scale; j++) {
						ehancedIm.setRGB(i, j, color);
					}
				}
			}
		}

		return ehancedIm;
	}

}
