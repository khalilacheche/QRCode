package qrcode;

public class Main {

	public static final String INPUT =  "יציצהüהüה\r\n\n\n\n\nhivhds" ;
			

	/*
	 * Parameters
	 */
	public static final int VERSION = Extensions.getBestVersion(INPUT);
	public static final int MASK = 0;
	public static final int SCALING = 10;

	public static void main(String[] args) {

		System.out.println(VERSION);
		/*
		 * Encoding
		 */
		
		
		boolean[] encodedData = DataEncoding.byteModeEncoding(INPUT, VERSION);
		
		/*
		 * image
		 */
		int[][] qrCode = MatrixConstruction.renderQRCodeMatrix(VERSION, encodedData,4);

		/*
		 * Visualization
		 */
		Helpers.show(qrCode, SCALING);
	}

}
