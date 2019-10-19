package qrcode;

public class MatrixConstruction {
	public static final int W = 0xFF_FF_FF_FF;
	public static final int B = 0xFF_00_00_00;

	
	// ...  MYDEBUGCOLOR = ...;
	// feel free to add your own colors for debugging purposes

	/**
	 * Create the matrix of a QR code with the given data.
	 * 
	 * @param version
	 *            The version of the QR code
	 * @param data
	 *            The data to be written on the QR code
	 * @param mask
	 *            The mask used on the data. If not valid (e.g: -1), then no mask is
	 *            used.
	 * @return The matrix of the QR code
	 */
	public static int[][] renderQRCodeMatrix(int version, boolean[] data, int mask) {

		/*
		 * PART 2
		 */
		int[][] matrix = constructMatrix(version, mask);
		/*
		 * PART 3
		 */
		addDataInformation(matrix, data, mask);

		return matrix;
	}

	/*
	 * =======================================================================
	 * 
	 * ****************************** PART 2 *********************************
	 * 
	 * =======================================================================
	 */

	/**
	 * Create a matrix (2D array) ready to accept data for a given version and mask
	 * 
	 * @param version
	 *            the version number of QR code (has to be between 1 and 4 included)
	 * @param mask
	 *            the mask id to use to mask the data modules. Has to be between 0
	 *            and 7 included to have a valid matrix. If the mask id is not
	 *            valid, the modules would not be not masked later on, hence the
	 *            QRcode would not be valid
	 * @return the qrcode with the patterns and format information modules
	 *         initialized. The modules where the data should be remain empty.
	 */
	public static int[][] constructMatrix(int version, int mask) {
		int [][] matrix = initializeMatrix(version);
		addFinderPatterns(matrix);
		addAlignmentPatterns(matrix,version);
		addTimingPatterns(matrix); 
		addDarkModule(matrix); 
		addFormatInformation(matrix,mask); 
		return matrix;

	}

	/**
	 * Create an empty 2d array of integers of the size needed for a QR code of the
	 * given version
	 * 
	 * @param version
	 *            the version number of the qr code (has to be between 1 and 4
	 *            included
	 * @return an empty matrix
	 */
	public static int[][] initializeMatrix(int version) {
		int size =  QRCodeInfos.getMatrixSize(version);
		int[][] matrix = new int [size][size];
		return matrix;
	}

	/**
	 * Add all finder patterns to the given matrix with a border of White modules.
	 * 
	 * @param matrix
	 *            the 2D array to modify: where to add the patterns
	 */
	public static void addFinderPatterns(int[][] matrix) {
		placePattern(0,0,matrix);
		drawLine(7,0,false,matrix);
		drawLine(0,7,true,matrix);
		
		placePattern(0,matrix.length-7,matrix);
		drawLine(matrix.length-8,0,false,matrix);
		drawLine(matrix.length-8,7,true,matrix);
		
		placePattern(matrix.length-7,0,matrix);
		drawLine(7,matrix.length-8,false,matrix);
		drawLine(0,matrix.length-8,true,matrix);
		
	}
	
	private static void placePattern(int xStart,int yStart,int [][]matrix) {
		boolean colorSwitch=false;
		
		for(int i=0;i<4;++i) {
			if(i!=3)
				colorSwitch=!colorSwitch;
			placeSquareAt(xStart+i,yStart+i,7-2*i,colorSwitch?B:W,matrix);
		}
		
	}
	private static void drawLine(int xStart,int yStart,boolean isHorizental,int[][]matrix) {
		
		if(isHorizental) {			
			for(int x=xStart;x<xStart+8;++x){
				matrix[x][yStart]=W;
			}	
		}else {
			for(int y=yStart;y<yStart+8;++y){
				matrix[xStart][y]=W;
			}	
		}
	}
	private static void placeSquareAt(int xStart,int yStart,int size, int color, int[][] matrix) { 
		for(int x=xStart;x<xStart+size;++x) {
			for(int y =yStart;y<yStart+size;++y) {
				if(y==yStart||y==yStart+size-1) {
					matrix[x][y]=color;
				}else {
					if(x==xStart||x==(xStart+size-1)) {
						matrix[x][y]=color;
					}
				}
			}
		}
	
	}
	
	public static void addAlignmentPatterns(int[][] matrix, int version) {
		if(version==1)
			return;
		int pos = matrix.length-7;
		boolean colorSwitch=true;
		for(int i=0;i<3;++i) {
			placeSquareAt(pos+i-2,pos+i-2, 5-2*i, colorSwitch ? B:W,matrix);
			colorSwitch=!colorSwitch;
		}
	}

	/**
	 * Add the timings patterns
	 * 
	 * @param matrix
	 *            The 2D array to modify
	 */
	public static void addTimingPatterns(int[][] matrix) {
		// TODO Implementer
		boolean  switcher = true; 
		// Create horizontal timing pattern 
		for(int i=8;i<matrix.length-8;++i) {
			matrix[i][6]= (switcher) ? B : W ; 
			switcher = !switcher; 	
		}
		switcher = true; 
		// Create Vertical timing pattern 
		for(int i=8 ; i<matrix[6].length-8; ++i) {
			matrix[6][i]=  (switcher) ? B : W ;
			switcher = !switcher; 	

		}
	}

	/**
	 * Add the dark module to the matrix
	 * 
	 * @param matrix
	 *            the 2-dimensional array representing the QR code
	 */
	public static void addDarkModule(int[][] matrix) {
		// TODO Implementer
		matrix[8][matrix[8].length-8] = 0xFF_00_00_00 ;
	}

	/**
	 * Add the format information to the matrix
	 * 
	 * @param matrix
	 *            the 2-dimensional array representing the QR code to modify
	 * @param mask
	 *            the mask id
	 */
	public static void addFormatInformation(int[][] matrix, int mask) {
		// TODO Implementer
		boolean[]  fs = QRCodeInfos.getFormatSequence(mask); 
       int j =0 ; 
		// horizontal Format Information
		for(int i =0 ; i<matrix.length ; ++i) {
			if(i==6 || (i>=8 && i <matrix.length-8)) {
				continue; 
			}else {
			matrix[i][8] = fs[j] ? B : W ;
			++j;
			}
		}
		
		j=0;
		// Vertical Format Information
		for (int i = matrix[8].length-1;i>=0;--i) {
			if((i<=8 || i>matrix[8].length-8) && (i!=6)) {
			matrix[8][i] = fs[j] ? B : W ;
			++j;
			}else {continue;}
		}
		

	}
	/*
	 * =======================================================================
	 * ****************************** PART 3 *********************************
	 * =======================================================================
	 */

	/**
	 * Choose the color to use with the given coordinate using the masking 0
	 * 
	 * @param col
	 *            x-coordinate
	 * @param row
	 *            y-coordinate
	 * @param color
	 *            : initial color without masking
	 * @return the color with the masking
	 */
	public static int maskColor(int col, int row, boolean dataBit, int masking) {
		// TODO Implementer
		return 0;
	}

	/**
	 * Add the data bits into the QR code matrix
	 * 
	 * @param matrix
	 *            a 2-dimensionnal array where the bits needs to be added
	 * @param data
	 *            the data to add
	 */
	public static void addDataInformation(int[][] matrix, boolean[] data, int mask) {
		// TODO Implementer

	}

	/*
	 * =======================================================================
	 * 
	 * ****************************** BONUS **********************************
	 * 
	 * =======================================================================
	 */

	/**
	 * Create the matrix of a QR code with the given data.
	 * 
	 * The mask is computed automatically so that it provides the least penalty
	 * 
	 * @param version
	 *            The version of the QR code
	 * @param data
	 *            The data to be written on the QR code
	 * @return The matrix of the QR code
	 */
	public static int[][] renderQRCodeMatrix(int version, boolean[] data) {

		int mask = findBestMasking(version, data);

		return renderQRCodeMatrix(version, data, mask);
	}

	/**
	 * Find the best mask to apply to a QRcode so that the penalty score is
	 * minimized. Compute the penalty score with evaluate
	 * 
	 * @param data
	 * @return the mask number that minimize the penalty
	 */
	public static int findBestMasking(int version, boolean[] data) {
		// TODO BONUS
		return 0;
	}

	/**
	 * Compute the penalty score of a matrix
	 * 
	 * @param matrix:
	 *            the QR code in matrix form
	 * @return the penalty score obtained by the QR code, lower the better
	 */
	public static int evaluate(int[][] matrix) {
		//TODO BONUS
	
		return 0;
	}

}