package reedsolomon;

public final class ErrorCorrectionEncoding {
	
	
	
	
	
	
	
	private static final int[] ANTILOG_TABLE = antilogTableGenerator();
	private static final int[] LOG_TABLE = logTableGenerator();
	
	private static final int[] antilogTableGenerator() {
		int[] table = new int[256];
		int n = 1;
		for(int i=0;i<256;i++) {

			if(table[n]==0) {
				table[n]= i;
			}
			
			n*=2;
			if(n>255) {
				n= n^285;
			}
		}
		
		table[0] = -1;
		return table;
	}
	
	
	
	
	private static int[] logTableGenerator() {
		int[] table = new int[256];
		int n = 1;
		for(int i=0;i<256;i++) {
			table[i] = n;
			n*=2;
			if(n>255) {
				n= n^285;
			}
		}
		
		
		return table;
	}


	/**
	 * Generate a given number of error correction codewords (ECC) for the given sequence of bytes
	 * @param messageCodeWords the sequence of bytes from which the ECC are generated
	 * @param errorCorrectionCodewords the number of ECC to generate
	 * @return An array of ECC bytes for messageCodeWords
	 */
	public static int[] encode(int[] messageCodeWords,int errorCorrectionCodewords) {
		
		/*
		 * Generate the message polynomial
		 */
		GalloisNumber[] messagePoly = new GalloisNumber[messageCodeWords.length];
		int i= messageCodeWords.length-1;
		for(int word:messageCodeWords) {
			messagePoly[i--] =GalloisNumber.getGalloisNumber( word & 0xFF); 
		}
		
		/*
		 * Create the generator polynomial
		 */
		GalloisNumber[] generatorPoly = polynomialGenerator(errorCorrectionCodewords);
		
		GalloisNumber[] remainder = messagePoly;
		
		for(int step=0;step<messageCodeWords.length;step++ ) {
			int remainderSize = errorCorrectionCodewords<remainder.length? remainder.length-1:errorCorrectionCodewords;
			GalloisNumber[] nextRemainder = new  GalloisNumber[remainderSize];
			
			
			/*
			 * multiply the generator polynomial
			 */
			GalloisNumber[] divisor = new GalloisNumber[generatorPoly.length];
			GalloisNumber mul = remainder[remainder.length-1];
			i = 0;
			for(GalloisNumber coeff:generatorPoly) {
				divisor[i++] = coeff.mul(mul);
			}
			
			/*
			 * XOR result with remainder
			 */
			for(int j=1;j<=remainderSize;j++) {
				GalloisNumber temp = null;
				if(divisor.length-j-1 >=0 && remainder.length-j-1>=0) {
					temp = divisor[divisor.length-j-1].add(remainder[remainder.length-j-1]);
				}else if(remainder.length-j-1>=0){
					temp = remainder[remainder.length-j-1];
				}else {
					temp = divisor[divisor.length-j-1];
				}
				
				nextRemainder[remainderSize-j] = temp;
			}
			
			remainder=nextRemainder;
			
		}
		
		
		/*
		 * Generate error correction codewords
		 */
		int[] errorCodewords = new int[errorCorrectionCodewords];
		i= errorCorrectionCodewords-1;
		for(GalloisNumber coeff:remainder) {
			errorCodewords[i--] =  0xFF & coeff.represent();
		}
		return errorCodewords;
	}
	
	
	private final static class GalloisNumber{
		final int p;
		
		public GalloisNumber(int power) {
			p=power;
		}
		
		public static GalloisNumber getGalloisNumber(int number) {
			if(number>255) {
				number=number^285;
			}
			return new GalloisNumber(ANTILOG_TABLE[number]);
		}
		
		public GalloisNumber add(GalloisNumber n2) {
			int res = this.represent() ^ n2.represent();
			return getGalloisNumber(res);
		}
		
		public GalloisNumber mul(GalloisNumber n2) {
			if(p<0 || n2.p<0) {
				return new GalloisNumber(-1);
			}
			return new GalloisNumber((n2.p +p)%255 );
		}
		
		public int represent() {
			if(p<0) {
				return 0;
			}
			return LOG_TABLE[p];
		}
		
		public String toString() {
			if(p<0) {
				return "0";
			}
			return "a^"+p;
		}
		
	}

	private final static GalloisNumber[] polynomialGenerator(int n) {
		if(n == 1) {
			GalloisNumber[] poly = {new GalloisNumber(0),new GalloisNumber(0)};
			return poly;
		}
		GalloisNumber[] left = polynomialGenerator(n-1);
		GalloisNumber[] right = {new GalloisNumber(n-1),new GalloisNumber(0)};
		
		GalloisNumber[] poly = new GalloisNumber[n+1];
		poly[0] = right[0].mul(left[0]);
		poly[n] = right[1];
		
		for(int i=1;i<n;i++) {
			poly[i] = right[0].mul(left[i]).add(right[1].mul(left[i-1]));
		}
		
		return poly;
		
		
	}
}
