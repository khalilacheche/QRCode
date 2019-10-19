package qrcode;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

class DataEncodingTest {

	private final String message = "Programming is a skill best acquired by practice.";
	private final int[] isoCode = { 80, 114, 111, 103, 114, 97, 109, 109, 105, 110, 103, 32, 105, 115, 32, 97, 32, 115,
			107, 105, 108, 108, 32, 98, 101, 115, 116, 32, 97, 99, 113, 117, 105, 114, 101, 100, 32, 98, 121, 32, 112,
			114, 97, 99, 116, 105, 99, 101, 46 };
	private final int[] byteCodeV1 = { 65, 21, 7, 38, 246, 119, 38, 22, 214, 214, 150, 230, 114, 6, 151, 50, 6, 18, 0 };
	private final int[] filled = { 65, 21, 7, 38, 246, 119, 38, 22, 214, 214, 150, 230, 114, 6, 151, 50, 6, 18, 0,236,17,236,17};
	private final int[] bybetAndError = { 65, 21, 7, 38, 246, 119, 38, 22, 214, 214, 150, 230, 114, 6, 151, 50, 6, 18,
			0, 143, 165, 236, 181, 112, 47, 93 };
	private final boolean[] binaryArray = { false, true, false, false, false, false, false, true, false, false, false,
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
	void testEncode() {
		boolean[] res = DataEncoding.byteModeEncoding(message,1);
		assertArrayEquals(binaryArray, res);
	}

	@Test
	void testEncodeStringVersion1() {
		int[] res = DataEncoding.encodeString(message, 17);

		assertEquals(17, res.length, "The length is of the encoding is not correct");
		assertArrayEquals(Arrays.copyOfRange(isoCode, 0, 17), res);
	}

	@Test
	void testEncodeStringVersion4() {
		int[] res = DataEncoding.encodeString(message, 49);

		assertEquals(49, res.length, "The length is of the encoding is not correct");
		assertArrayEquals(isoCode, res);
	}

	@Test
	void testaddInformations() {
		int[] res =  DataEncoding.addInformations(Arrays.copyOfRange(isoCode, 0, 17));
		assertArrayEquals(byteCodeV1, res);
	}
	
	@Test
	void testFillSequence() {
		int[] res =  DataEncoding.fillSequence(byteCodeV1, 23);
		assertEquals(23, res.length, "The length is of the fillSequence is not correct");
		assertArrayEquals(filled, res);
	}

	@Test
	void testAddErrorCorrection() {
		int[] res = DataEncoding.addErrorCorrection(byteCodeV1, QRCodeInfos.getECCLength(1));
		assertArrayEquals(bybetAndError, res);
	}

	@Test
	void testToBinaryArray() {
		boolean[] res = DataEncoding.bytesToBinaryArray(bybetAndError);
		assertArrayEquals(binaryArray, res);
	}

}
