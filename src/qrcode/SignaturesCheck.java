package qrcode;

import reedsolomon.ErrorCorrectionEncoding;

public final class SignaturesCheck {
	
	/** =========================
	 *  !!!! DO NOT MODIFY !!!!
	 *  =========================
	 *  
	 *  Any submission for which this file does not compile will not be accepted
	 *  
	 */

	@SuppressWarnings("unused")
	public static final void check() {
		boolean[] a = DataEncoding.byteModeEncoding("",0);
		int [] a2 = DataEncoding.encodeString("", 0);
		int[] b = DataEncoding.addInformations(a2);
		int[] b2 = DataEncoding.fillSequence(b, 0);
		int[] c = DataEncoding.addErrorCorrection(b, 0);
		boolean[] d = DataEncoding.bytesToBinaryArray(b);
		
		int[][] e = MatrixConstruction.renderQRCodeMatrix(0, d, 0);
		int[][] e2 = MatrixConstruction.renderQRCodeMatrix(0, d);
		int f = MatrixConstruction.findBestMasking(0, d);
		MatrixConstruction.addFinderPatterns(e);
		MatrixConstruction.addAlignmentPatterns(e,0);
		MatrixConstruction.addTimingPatterns(e);
		MatrixConstruction.addDarkModule(e);
		int[][] e3 = MatrixConstruction.constructMatrix(0, 0);
		int[][] e4 = MatrixConstruction.initializeMatrix(0);
		
		MatrixConstruction.addDataInformation(e, d, 0);
		int g = MatrixConstruction.maskColor(0, 0, true, 0);
		int g2 = MatrixConstruction.evaluate(e);
		MatrixConstruction.addFormatInformation(e, 0);
		
		int[] h = ErrorCorrectionEncoding.encode(b, 0);
		boolean[] i = QRCodeInfos.getFormatSequence(0);
		int j = QRCodeInfos.getCodeWordsLength(0);
		int k = QRCodeInfos.getMatrixSize(0);
		int l = QRCodeInfos.getMaxInputLength(0);
		
		
	}
}
