package qrcode;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MatrixConstructionTest01 {
	

	@Test
	void testInitializePatternsV1() {
		int size = QRCodeInfos.getMatrixSize(1);
		int[][] matrix = new int[size][size];
		MatrixConstruction.addFinderPatterns(matrix);
		MatrixConstruction.addAlignmentPatterns(matrix, 1);
		MatrixConstruction.addTimingPatterns(matrix);
		MatrixConstruction.addDarkModule(matrix);
		assertTrue(Helpers.compare(matrix,"patterns"),"The patterns are not similar. Run Debug.java for more informations");
	}
	
	@Test
	void testInitializePatternsV4() {
		int size = QRCodeInfos.getMatrixSize(4);
		int[][] matrix = new int[size][size];
		MatrixConstruction.addFinderPatterns(matrix);
		MatrixConstruction.addAlignmentPatterns(matrix, 4);
		MatrixConstruction.addTimingPatterns(matrix);
		MatrixConstruction.addDarkModule(matrix);
		assertTrue(Helpers.compare(matrix,"patternsv4"),"The patterns are not similar. Run Debug.java for more informations");
	}

	@Test
	void testAddFormatInformationV1M0() {
		int size = QRCodeInfos.getMatrixSize(1);
		int[][] matrix = new int[size][size];
		MatrixConstruction.addFormatInformation(matrix, 0);
		assertTrue(Helpers.compare(matrix,"formatV1M0"),"The format information is wrong. Run Debug.java for more informations");
	}
	@Test
	void testAddFormatInformationV4M5() {
		int size = QRCodeInfos.getMatrixSize(4);
		int[][] matrix = new int[size][size];
		MatrixConstruction.addFormatInformation(matrix, 5);
		assertTrue(Helpers.compare(matrix,"formatV4M5"),"The format information is wrong. Run Debug.java for more informations");
	}
	
	@Test
	void testConstructMatrix() {
		int[][] matrix = MatrixConstruction.constructMatrix(4, 5);
		assertTrue(Helpers.compare(matrix,"noDataV4M5"),"The format information is wrong. Run Debug.java for more informations");
	}

}
