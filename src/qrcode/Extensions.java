package qrcode;

public class Extensions {
	public static int[][] getAlignmentPatterns(int version){
		int length = 2+(version/7);
		int[] centers= new int[length];
		int [][] result = new int [length*length-3][2];
		centers[0]=6;
		int difference=version%7;
		
		if(version<7) {
			centers[1]= 10+ 4*version;
		}else if(version<14){
			centers[1]=22+2*difference;
			centers[2]=38+4*difference;
			
		}else if(version<21) {
			centers[1]=26+4*(difference/3);
			centers[3]=66+4*difference;
			centers[2]= (centers[3]-centers[1])/2 + centers[1];
			
		}else if(version<28) {
			centers[1]= 26+2*(difference/2)+2*(difference%2==0?1:0);
			centers[4]=94+4*difference;
			for(int i=2;i<4;++i) {
				centers[i]=((centers[4]-centers[1])/3)*(i-1)+centers[1];
				
			}
		}else if(version<35) {
			centers[1]= difference%2==0? 26+8*(difference/4):30;
			centers[5]=difference*4+122;
			for(int i=2;i<5;++i) {
				centers[i]=((centers[5]-centers[1])/4)*(i-1)+centers[1];
			}
			
		}else if(version <41) {
			centers[1]= difference%5==0 ? 30:(difference==4?26:(difference+5)*4);
			centers[6]=difference*4+150;
			centers[2]=((centers[6]-centers[1])/5)+centers[1];
			centers[3]=((centers[6]-centers[1])/5)*2+centers[1];
			centers[4]=((centers[6]-centers[1])/5)*3+centers[1];
            centers[5]=((centers[6]-centers[1])/5)*4+centers[1];

			
		}
		int counter=0;
		for(int i=0;i<centers.length;++i) {
			for(int j=0;j<centers.length;++j) {
				if(i==0&&j==0 || i==0 &j==centers.length-1||i==centers.length-1 && j ==0) {
				}else {
					result[counter][0]=centers[i];
					result[counter][1]=centers[j];
					++counter;
				}
			}
			
		}
		return result;
	}
	
	
	
	
	public static int getBestVersion(String input) {
		for(int i=1;i<41;++i) {
			if(QRCodeInfos.getMaxInputLength(i)>input.length()) {
				return i;
			}
		}
		return 40;
	}
	
	
	
	
	
}
