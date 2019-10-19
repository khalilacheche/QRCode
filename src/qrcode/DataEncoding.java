package qrcode;

import java.nio.charset.StandardCharsets;

import reedsolomon.ErrorCorrectionEncoding;

public final class DataEncoding {

	public static boolean[] byteModeEncoding(String input, int version) {
		int[] encodedString = encodeString(input,QRCodeInfos.getMaxInputLength(version));
		encodedString=addInformations(encodedString);
		encodedString=fillSequence(encodedString,QRCodeInfos.getCodeWordsLength(version));
		encodedString = addErrorCorrection(encodedString,QRCodeInfos.getECCLength(version));
		
		return bytesToBinaryArray(encodedString);
	}
	
	public static int[] encodeString(String input, int maxLength) {
		byte[] tabByte= input.getBytes(StandardCharsets.ISO_8859_1);
		
		int length = (tabByte.length < maxLength) ? tabByte.length : maxLength; //The length of the output is the length of the sequence if maxLength>sequence, else, maxLenght
		int[] output= new int[length];

		for(int i=0;i<length;++i) {
			output[i]= tabByte[i] & 0xFF;
		}

		return output;
	}
	
	public static int[] addInformations(int[] inputBytes) {
		
		int size=inputBytes.length;
		int[] output = new int[size+2];
		int indicator = 0b0100;
		
		output[0]=indicator<<4|size>>4;//First byte consists of indicator + 4 last bits of size
		output[1]=(size&0xF)<<4|inputBytes[0]>>4;//Second byte consists of 4 first bits of size + 4 last bits f first input byte
		
		for(int i=2;i<output.length-1;++i ) {
			int msb=(inputBytes[i-2]& 0xF)<<4;//Getting (only) the first 4 bits from i-1 and shifting them to the left
			int lsb= inputBytes[i-1]>>4;//Getting the 4 last bits from i and shifting them to the right
			output[i] = msb|lsb;//Combining the msb and lsb
		}
		
		output[output.length-1]=(inputBytes[inputBytes.length-1]& 0xF)<<4;//Last byte with terminaison sequence
		
		return output;
	}

	
	public static int[] fillSequence(int[] encodedData, int finalLength) {
		
		//TODO: Check if final length <size
		
		int[] output = new int[finalLength];
		boolean switcher=true; //Variable to switch back and forth between 236 and 17
		
		for(int i=0;i<finalLength;++i) {
			if(i<encodedData.length) {
				output[i]=encodedData[i];
			}else {
				output[i]= switcher? 236 : 17;
				switcher=!switcher;
			}
		}
		
		return output;
	}
	public static int[] addErrorCorrection(int[] encodedData, int eccLength) {
		
		int[] correctionEncoding =ErrorCorrectionEncoding.encode(encodedData,eccLength);
		int[] output = new int[eccLength+encodedData.length];
		
		for(int i=0;i<output.length;++i) {
			if(i<encodedData.length){
				output[i]=encodedData[i];
			}else {
				output[i]= correctionEncoding[i-encodedData.length];
			}			
		}
		
		return output;
	}

	public static boolean[] bytesToBinaryArray(int[] data) {
		boolean[] output = new boolean[data.length*8];
		for(int i=0;i<data.length;++i) {
			int in = data[i];
			for(int j=0;j<8;++j) {
					if(in%2==0) {
						output[8*i+7-j]=false;
						in= (in/2);
					}else {
						output[8*i+7-j]=true;
						in=(in/2);

					}
			}
		}
		return output;
	}

}
