package qrcode;

public final class QRCodeInfos {
	
	private static final int MATRIX_SIZE_VERSION_1 =21;
	private static final int MATRIX_SIZE_STEP =4;
	
	private final static int[] VERSION_CODE_WORDS = { 19, 34, 55, 80 };
	
	private static final int[] LVL_CODE = {1,0,3,2};
	
	public enum CorrectionLvl{
		L,M,Q,H
	}
	
	private static final int[] ERROR_CORRECTION_CODEWORDS = {7,10,15,20};
	
	
	
	/**
	 * Get the size of the matrix for a specific version.
	 * i.e. For version 1, this method return 21 since the matrix has a size of 21x21
	 * @param version 
	 *         version of the QRcode
	 * @return an integer : the size of the matrix for a given QR code version
	 */
	public static int getMatrixSize(int version) {
		if(version>40) {
			throw new IllegalArgumentException("The maximum QR code Version is 40");
		}
		return MATRIX_SIZE_VERSION_1 + MATRIX_SIZE_STEP*(version-1);
	}
	
	
	/**
	 * Get the maximum input length for a given QR code version
	 * @param version
	 *        version of the QRcode
	 * @return the maximum number of bytes of data that can be encoded for the given version
	 */
	public static int getMaxInputLength(int version) {
		if(version>4) {
			throw new UnsupportedOperationException("The version has to be between 1 and 5");
		}
		return VERSION_CODE_WORDS[version-1] -2 ;
	}
	
	/** Get the number of error correction codewords needed for a given version
	 * @param version
	 * 			 version of the QRcode	
	 * @return
	 */
	public static int getECCLength(int version) {
		if(version>4) {
			throw new UnsupportedOperationException("The version has to be between 1 and 5");
		}
		return ERROR_CORRECTION_CODEWORDS[version-1] ;
	}
	
	/**
	 * Get the number of codewords encoding the data for a given version
	 * @param version
	 *          version of the QRcode
	 * @return the number of codewords in the version
	 */
	public static int getCodeWordsLength(int version) {
		if(version>4) {
			throw new UnsupportedOperationException("The version has to be between 1 and 5");
		}
		return VERSION_CODE_WORDS[version-1] ;
	}



	/**
	 * Return the sequence of pixels that encodes the format information related to error correction level and used mask.
	 * The array is a boolean array providing a binary representation of the data, with the most significant bit first
	 * @param mask
	 *        the integer code of the mask to be used (must be between 0 and 7)
	 * @return the array of pixels encoding the format information. Most significant bit first
	 */
	public static boolean[] getFormatSequence(int mask) {
		if(mask>7 || mask <0) {
			throw new IllegalArgumentException("The mask has to be between 0 and 7");
		}
		int errorCorrectionLevel=CorrectionLvl.L.ordinal();
		int code = ((LVL_CODE[errorCorrectionLevel]& 0x3)<<3) | (mask&0x7);
		int current = code<<10;
	
		int poly = 0b10100110111;
		int size = 15;
		while(((0b1<<(size-1)) & current) ==0) {
			size--;
			if(size == 0) {
				throw new IllegalAccessError();
			}
		}
		
		while(size>10) {
			int paddedPoly = poly<<(size-11);
	
			current = paddedPoly^current;
			
			
			while(((0b1<<(size-1)) & current) == 0) {
				size--;
				if(size == 0) {
					throw new IllegalAccessError();
				}
			}
		}
		
		int format = (code<<10 | (current& 0x3FF)) ^ 0b101010000010010;
		
		boolean[] formatPixels = new boolean[15];
		for(int i=0;i<formatPixels.length;i++) {
			formatPixels[i] = !(((format >> (14 - i)) & 0b1) == 0);
		}
		
		return formatPixels;
	}
	
	

}
