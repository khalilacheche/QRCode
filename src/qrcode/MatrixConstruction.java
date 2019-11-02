package qrcode;

import javax.swing.plaf.synth.SynthSeparatorUI;

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


	public static int[][] initializeMatrix(int version) {
		int size =  QRCodeInfos.getMatrixSize(version);
		int[][] matrix = new int [size][size];
		return matrix;
	}

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
		
		for(int i=0;i<4;++i) { // Draw squares from the biggest to the smallest with alternating colors
			if(i!=3)
				colorSwitch=!colorSwitch;
			placeSquareAt(xStart+i,yStart+i,7-2*i,colorSwitch?B:W,matrix);   
		}
		
	}
	
	private static void drawLine(int xStart,int yStart,boolean isHorizontal,int[][]matrix) {//Draw vertical or horizontal white line 
		
		if(isHorizontal) {			
			for(int x=xStart;x<xStart+8;++x){
				matrix[x][yStart]=W;
			}	
		}else {
			for(int y=yStart;y<yStart+8;++y){
				matrix[xStart][y]=W;
			}	
		}
	}
	private static void placeSquareAt(int xStart,int yStart,int size, int color, int[][] matrix) {// draw empty square (size*size) with border "color" 
		for(int x=xStart;x<xStart+size;++x) {                                                     //   and top left corner with coordinates(xStart,yStart)
			for(int y =yStart;y<yStart+size;++y) {
				if(y==yStart||y==yStart+size-1|| x==xStart||x==(xStart+size-1)) {
					matrix[x][y]=color;
				}                                                      
			}
		}
	
	}
	
	public static void addAlignmentPatterns(int[][] matrix, int version) {
		if(version==1)
			return;
		int pos = matrix.length-7;
		boolean colorSwitch=true;
		for(int i=0;i<3;++i) {  // Draw squares from the biggest to the smallest with alternating colors
			placeSquareAt(pos+i-2,pos+i-2, 5-2*i, colorSwitch ? B:W,matrix);    
			colorSwitch=!colorSwitch;
		}
	}

	public static void addTimingPatterns(int[][] matrix) {
		boolean  switcher = true; 
		// Create horizontal timing pattern 
		for(int i=8;i<matrix.length-8;++i) {
			matrix[i][6]= (switcher) ? B : W ; 
			switcher = !switcher; 	
		}
		switcher = true; 
		// Create Vertical timing pattern 
		for(int i=8 ; i<matrix.length-8; ++i) {
			matrix[6][i]=  (switcher) ? B : W ;
			switcher = !switcher; 	

		}
	}

	
	public static void addDarkModule(int[][] matrix) {
		matrix[8][matrix.length-8] = B ;
	}

	
	public static void addFormatInformation(int[][] matrix, int mask) {
		boolean[]  fs = QRCodeInfos.getFormatSequence(mask); 
		int j =0 ; 
		int ml= matrix.length;
		// horizontal Format Information
		for(int i =0 ; i<ml ; ++i) {
			if(i==6) {
				continue; //Ommit this pixel 
			}else if(i==8 ){
				i=ml-9;	
			}else {
				matrix[i][8] = fs[j] ? B : W ;
				++j;
			}
		}
		
		j=0;
		// Vertical Format Information
		for (int i = ml-1;i>=0;--i) {
			if( i==ml-8){
				i=9;
			}else if(i==6) {
			    continue; //Ommit this pixel
		    }
			else{
				matrix[8][i] = fs[j] ? B : W ;
				++j;
			}
			
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
		
		switch(masking) {
			case 0:
				if((col + row) % 2 == 0)
					dataBit=!dataBit;
				break;
			case 1:
				if(row % 2 == 0)
					dataBit=!dataBit;
				
				break;
			case 2:
				if(col % 3 == 0)
					dataBit=!dataBit;
				
				break;
			case 3:
				if((col + row) % 3 == 0)
					dataBit=!dataBit;
				
				break;
			case 4:
				if((col/3 + row/2) % 2 == 0)
					dataBit=!dataBit;
				
				break;
			case 5:
				if(((col * row) % 2) +((col * row) % 3) == 0)
					dataBit=!dataBit;
				
				break;
			case 6:
				if((((col * row) % 2) +((col * row) % 3)) % 2 == 0)
					dataBit=!dataBit;
				
				break;
			case 7:
				if((((col + row) % 2) +((col * row) % 3)) % 2 == 0)
					dataBit=!dataBit;
				
				break;
			default:
				break;

		
		}
		
		return dataBit ? B : W ;
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
		int ml=matrix.length; // matrix length
		int y=ml-1;   // initialize line start
		int x = ml-1;  // initialize  column start 
		int i=0;  // data counter
		boolean zigzag=false ; // true when we fill horizontally false when vertically 
		boolean isAscending = true;  // true when we fill from bottom to top
		boolean isEmpty=false;   // true if module is empty
		
		if (data.length==0 ) {              // fill the matrix with empty data 
			for(int j=0 ; j<ml;++j) {
				if(j==6) continue;   // skip the vertical timing pattern 
				for(int k=0 ; k<ml; ++k) {
					isEmpty = matrix[j][k] == 0;
					matrix[j][k]= isEmpty ? maskColor(j,k,false,mask) : matrix[j][k]; 
				}
			}
		}
		while(x>0) {
			if ( x == 6 ) x=x-1 ; // skip the vertical timing pattern 
			
							while(y>=0 && y<=ml-1 && i<data.length) {
								
								if (zigzag) { // if we fill horizontally 

									isEmpty = matrix[x-1][y] == 0; // checks if module is empty 
									matrix[x-1][y] = isEmpty ?  maskColor(x-1,y,data[i],mask) : matrix[x-1][y] ; // put data mask if  empty
									}
								else { // if we fill vertically 
									isEmpty = matrix[x][y] == 0;
									matrix[x][y] = isEmpty ? maskColor(x,y,data[i],mask) : matrix[x][y] ;
								}
								i = isEmpty ? i+1 : i ;  // if we did not data module i stays the same 
								
								zigzag=!zigzag; 
								
								// if we fill horizontally line number stays the same else we change it according to our direction in lines 
								y = zigzag ? y : (isAscending ? y-1 : y+1 );   
									
								
							}
							
							
		if (i==data.length ) {   // if there is no more data we continue to fill the rest of the matrix   
			
			for(int j=0 ; j<=x;++j) { // j stops at the number of column whe we ran out of data 
				
				if(j==6) continue; // skip vertical timing pattern 
					for(int k=0 ; k<=ml-1; ++k) {
						
						isEmpty = matrix[j][k] == 0;  
	
						matrix[j][k]= isEmpty ? maskColor(j,k,false,mask) : matrix[j][k]; 
					}
			}
			break; // We break out of the loop because all of it is filled
		}
		
		
		x-=2;  // change x to fill the next to columns 
		isAscending =!isAscending;  // change the direction of the filling
		y = isAscending ? ml-1 : 0 ; // initialize y according to the direction
		}
		
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
		System.out.println("Mask 0");
		int lowestEvaluation=evaluate(renderQRCodeMatrix(version,data,0));
		System.out.println(lowestEvaluation);
		int evaluation;
		int bestMasking=0;
		for(int i=1;i<8;++i) {
			System.out.println("Mask "+i);
			evaluation=evaluate(renderQRCodeMatrix(version,data,i));
			System.out.println(evaluation);
			if(evaluation < lowestEvaluation) {
				lowestEvaluation = evaluation;
				bestMasking=i;
			}
		}
		System.out.println("\nbest " + bestMasking);
		return bestMasking;
	}

	/**
	 * Compute the penalty score of a matrix
	 * 
	 * @param matrix:
	 *            the QR code in matrix form
	 * @return the penalty score obtained by the QR code, lower the better
	 */
	public static int evaluate(int[][] matrix) {
		int ml= matrix.length;
		int consecutive=0;
		int penalties=0;
		
		int[] sequence1 = {W, W, W, W, B, W, B, B, B, W, B};
		int[] sequence2 = {B, W, B, B, B, W, B, W, W, W, W};
		
		int blackModules=0;
		
		int numberConsecutive=0;
		
		//Checking for consecutives on the columns
		for(int x=0;x<ml;++x) {
			for(int y=1;y<ml;++y) {
					
				if(matrix[x][y]==matrix[x][y-1]) {
					++consecutive;
				}else {
					if(consecutive>=4) {
						penalties+= consecutive-1;
						numberConsecutive++;
					}
					consecutive = 0;
				}
				if(y==ml-1) {
					if(consecutive>=4) {
						penalties+= consecutive-1;
						numberConsecutive++;
					}
					consecutive = 0;
				}
					
			}
		}
		consecutive=0;
		numberConsecutive=0;
		
		
		//Checking for consecutives on the rows
		
		for(int y=0;y<ml;++y) {
			for(int x=1;x<ml;++x) {
				if(matrix[x][y]==matrix[x-1][y]) {
					++consecutive;
				}else {
					if(consecutive>=4) {
						penalties+= consecutive-1;
						numberConsecutive++;
					}
					consecutive = 0;
				}
				if(x==ml-1) {
					if(consecutive>=4) {
						penalties+= consecutive-1;
						numberConsecutive++;
					}
					consecutive = 0;
				}
			}
		}
		//Checking for 2x2 Squares of same color
		for(int x=0; x<ml-1;++x) {
			for(int y=0; y<ml-1;++y) {
				if(  (matrix[x][y]==matrix[x+1][y]) && (matrix[x][y]==matrix[x][y+1]) && (matrix[x][y]==matrix[x+1][y+1]) ) {
					penalties +=3;
				}
			}
		}
		
		
		
		//Checking for finder-like patterns
		
		
		
			//looping through the y axis
		for(int x=1; x<ml-1;++x) {
			for(int y=0; y<ml-sequence1.length+1;++y) {

				if(matrix[x][y]==W) {
					
					for(int i=1;i<sequence1.length;++i) {
						if(matrix[x][y+i]!=sequence1[i]) {
							break;
							
						} 
						if(i==sequence1.length-1) {
							penalties+=40;
							//System.out.println("W V "+x+" "+y);
						}
					}
					
					
				}else {
					for(int i=1;i<sequence2.length;++i) {
						if(matrix[x][y+i]!=sequence2[i]) 
							break;
						if(i==sequence2.length-1) {
							//System.out.println("B V "+x+" "+y);
							penalties+=40;							
						}
					}
				}
			}
		}
		
			//looping through the x axis
		for(int y=1; y<ml-1;++y) {
			for(int x=0; x<ml-sequence1.length+1;++x) {
				
				if(matrix[x][y]==W) {
					for(int i=1;i<sequence1.length;++i) {
						if(matrix[x+i][y]!=sequence1[i]) 
							break;
						if(i==sequence1.length-1) {
							//System.out.println("W H "+x+" "+y);
							penalties+=40;							
						}
					}
					
					
				}else {
					for(int i=1;i<sequence2.length;++i) {
						if(matrix[x+i][y]!=sequence2[i]) 
							break;
						if(i==sequence2.length-1) {
							//System.out.println("B H "+x+" "+y);
							penalties+=40;							
						}
					}
				}
			}
		}

		
		
		//Black modules penalty
		for(int x=0;x<ml;++x) {
			for(int y=0;y<ml;++y) {
					if(matrix[x][y]==B)
							++blackModules;
			}
		}
		
		int percentage = blackModules*100/(ml*ml);
		int firstPercentage = (percentage/5) *5;
		int secondPercentage = firstPercentage +5;
		firstPercentage = Math.abs(firstPercentage-50);
		secondPercentage = Math.abs(secondPercentage-50);
		penalties += firstPercentage>secondPercentage? secondPercentage*2:firstPercentage *2;
		
		
		
	
		return penalties;
	}

}
