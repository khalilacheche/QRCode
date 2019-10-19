package qrcode;

public class Main {

	public static final String INPUT =  "Write you message here. Accents are permitted, but not all characters. See the norm ISO/CEI 8859-1 on wikipedia for more info";

	/*
	 * Parameters
	 */
	public static final int VERSION = 4;
	public static final int MASK = 0;
	public static final int SCALING = 20;

	public static void main(String[] args) {

		/*
		 * Encoding
		 */
		boolean[] encodedData = DataEncoding.byteModeEncoding(INPUT, VERSION);
		
		/*
		 * image
		 */
		int[][] qrCode = MatrixConstruction.renderQRCodeMatrix(VERSION, encodedData,MASK);

		/*
		 * Visualization
		 */
		Helpers.show(qrCode, SCALING);
	}

}
