package qrcode;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MatrixConstructionTest02 {
	private final boolean[] data = { false, true, false, false, false, false, false, true, false, false, false,
			true, false, true, false, true, false, false, false, false, false, true, true, true, false, false, true,
			false, false, true, true, false, true, true, true, true, false, true, true, false, false, true, true, true,
			false, true, true, true, false, false, true, false, false, true, true, false, false, false, false, true,
			false, true, true, false, true, true, false, true, false, true, true, false, true, true, false, true, false,
			true, true, false, true, false, false, true, false, true, true, false, true, true, true, false, false, true,
			true, false, false, true, true, true, false, false, true, false, false, false, false, false, false, true,
			true, false, true, false, false, true, false, true, true, true, false, false, true, true, false, false,
			true, false, false, false, false, false, false, true, true, false, false, false, false, true, false, false,
			true, false, false, false, false, false, false, false, false, false, true, false, false, false, true, true,
			true, true, true, false, true, false, false, true, false, true, true, true, true, false, true, true, false,
			false, true, false, true, true, false, true, false, true, false, true, true, true, false, false, false,
			false, false, false, true, false, true, true, true, true, false, true, false, true, true, true, false,
			true };

	@Test
	void testInitMatrixQRCodeMatrixM0() {
		int[][] res = MatrixConstruction.renderQRCodeMatrix(1, data, 0);
		assertEquals(21,res.length);
		assertEquals(21,res[0].length);
	}
	@Test
	void testRenderQRCodeMatrixV1() {
		int[][] res = MatrixConstruction.renderQRCodeMatrix(1, data, 0);
		assertTrue(Helpers.compare(res, "testV1M0"),"Matrix is not what expected. Run Debug.java for more informations");
	}

	@Test
	void testMaskColor() {
		assertEquals(-1 , MatrixConstruction.maskColor(2, 0, true, 0));
		assertEquals(-1 , MatrixConstruction.maskColor(1, 1, false, 1));
		assertEquals(-16777216 , MatrixConstruction.maskColor(0, 2, false, 2));
	}

	@Test
	void testAddDataInformationOnEmptyMat() {
		int[][] matrix = new int[41][41];
		MatrixConstruction.addDataInformation(matrix, data, 0);
		assertTrue(Helpers.compare(matrix, "dataMatEmpty"),"The data bit are not placed as expected. Use Debug.java for more informations");
	}
	
	@Test
	void testAddDataInformationWithNoData() {
		int[][] matrix = Helpers.readMatrix("NoDataV1M0");
		boolean[] a = {};
		MatrixConstruction.addDataInformation(matrix, a, 0);
		assertTrue(Helpers.compare(matrix, "emptyDataV1M0.png"),"The data bit are not placed as expected. Use Debug.java for more informations");
	}
	
	@Test
	void testAddDataInformation() {
		int[][] matrix = Helpers.readMatrix("NoDataV1M0");
		MatrixConstruction.addDataInformation(matrix, data, 0);
		assertTrue(Helpers.compare(matrix, "testV1M0"),"The data bit are not placed as expected. Use Debug.java for more informations");
	}

}
