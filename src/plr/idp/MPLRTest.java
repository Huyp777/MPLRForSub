package plr.idp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MPLRTest {

	public static void main(String[] args) {
		File file = new File("D:/DATASET/ECG200.txt");
		//File file = new File("D:/DATASET/ECG5000_TRAIN.txt");
		//File file = new File("D:/DATASET/CinC_ECG_torso_TRAIN.txt");
		//File file = new File("D:/DATASET/InlineSkate_TRAIN.txt");
		//File file = new File("D:/DATASET/Phoneme_TRAIN.txt");
		//File file = new File("D:/DATASET/Haptics_TRAIN.txt");
		//File file = new File("D:/DATASET/Worms_TRAIN.txt");
		Double[] a = txt2String(file);
		/*
		 * for (int i = 0; i < a.length; i++) { System.out.println(a[i]); }
		 */
		int errortest[] = {1,a.length-1};
		PLR_SW_BU swbu = new PLR_SW_BU(a);
		MPLR_IDP mplr = new MPLR_IDP(a);
		MPLR_IDP plr = new MPLR_IDP(a);
		double Maxcalcerr = swbu.calcError(errortest);
		System.out.println("The initial max error: "+Maxcalcerr);
		double thre1 = 0.1*Maxcalcerr;
		double thre2 = 0.1*Maxcalcerr;
		int count = 2;
		// PLR_CSTP p3 = new PLR_CSTP(a);
		List<Segments> segBU = swbu.segmentByBU(thre1);
		//List segSW = swbu.segmentBySW(thre1);
		int[] c1 = plr.getIDPIndexByThreshold(thre2,1);
		int[] c2 = mplr.getIDPIndexByThreshold(thre2,count);
		double errorSWAB = swbu.calcErrorforBU(segBU);
		double errorPLR = mplr.calcError(c1);
		double errorMPLR = mplr.calcError(c2);
		System.out
				.println("*************************************************************");
		/*System.out.println("The points of the segments by SW is ");
		System.out.println(segSW);
		System.out.println("The number of the segments by SW is "
				+ swbu.getSeg_SW_num());
		System.out.println("The Total error of the entire segments by SW is "
				+ swbu.calcErrorforSW(segSW));*/
		System.out.println("The points of the segments by PLR-IDP is ");
		for (int i = 0; i < c1.length; i++){
			System.out.print(c1[i] + " "); 	 
		}
		System.out.println();
		System.out.println("The number of the segments by PLR-IDP is "
				+ c1.length);
		System.out.println("The threshold:"+thre2+" The Total error of the entire segments by PLR-IDP is "
				+ errorPLR);
		System.out
				.println("*************************************************************");
		System.out.println("The points of the segments by SWAB is: ");
		Segments sfirst = (Segments) segBU.get(0);
		System.out.print(sfirst.getBegin() + ", " + sfirst.getEnd() + ", ");
		for (int i = 1; i < segBU.size(); i++) {
			Segments snext = (Segments) segBU.get(i);
			System.out.print(snext.getEnd() + ", ");
		}
		System.out.println();
		System.out.println("The number of the segments by SWAB is "
				+ swbu.getSeg_SWAB_num());
		
		 System.out.println("The threshold:"+thre1+"  The Total error of the entire segments by SWAB is "+ errorSWAB);
		 
		System.out
				.println("*************************************************************");
		while(errorSWAB <= errorMPLR){
			System.out.println("The count is:"+count+"  and The points of the segments by MPLR_IDP is: ");
			for (int i = 0; i < c2.length; i++){
				System.out.print(c2[i] + " "); 	 
			}
			System.out.println(); 
			System.out.println("The number of the segments by IDP is " + c2.length);
			System.out.println("The threshold:"+thre2+" The Total error of the entire segments by IDP is "
					+ errorMPLR+" compared with "+errorSWAB);
			System.out.println("----------------------------------------------");
			count++;
			c2 = mplr.getIDPIndexByThreshold(thre2,count);
			errorMPLR = mplr.calcError(c2);
		}
		System.out
		.println("*************************The END Result************************************");
		System.out.println("The count is:"+count+"  and The points of the segments by MPLR_IDP is: ");
		for (int i = 0; i < c2.length; i++){
			System.out.print(c2[i] + " "); 	 
		}
		System.out.println(); 
		System.out.println("The number of the segments by IDP is " + c2.length);
		System.out.println("The threshold:"+thre2+" The Total error of the entire segments by IDP is "
				+ errorMPLR+" compared with "+errorSWAB);
		/*
		 * double swabErr = p1.calcError(segSWAB); // while (p1.calcError(c) >
		 * swabErr){ // mul = mul * 2; // c=p1.getIDPIndexByThreshold(200.0,
		 * mul); // } int max = 128, min = 1; while (max > min) { int mid = (max
		 * + min) / 2; c = p1.getIDPIndexByThreshold(thre2, mid); for (int i =
		 * 0; i < c.length; i++) System.out.print(c[i] + " ");
		 * System.out.println(); double err = p1.calcError(c); if (err >
		 * swabErr) { min = mid + 1; } else { max = mid; } System.out
		 * .println("*************************************************************"
		 * ); System.out.println("The number of the segments by IDP is " +
		 * c.length); System.out
		 * .println("The Total error of the entire segments by IDP is " +
		 * p1.calcError(c)); System.out.println("The mul is " + mid); } c =
		 * p1.getIDPIndexByThreshold(thre2, max); System.out.println(max);
		 * System.out
		 * .println("*************************************************************"
		 * ); System.out.println("The number of the segments by IDP is " +
		 * c.length);
		 * System.out.println("The Total error of the entire segments by IDP is "
		 * + p1.calcError(c)); System.out.println("The mul is " + max);
		 */
		/*
		 * System.out.print(segFSW); System.out.println();
		 * System.out.println("The number of the segments by FSW is "+
		 * p2.getSeg_FSW_num());
		 * System.out.println("The Total error of the entire segments by FSW is "
		 * + p2.calcError(segFSW) ); System.out.println(
		 * "*************************************************************");
		 * System.out.print(segSFSW); System.out.println();
		 * System.out.println("The number of the segments by SFSW is "+
		 * p2.getSeg_SFSW_num());
		 * System.out.println("The Total error of the entire segments by SFSW is "
		 * + p2.calcError(segSFSW) ); System.out.println(
		 * "*************************************************************");
		 * System.out.print("[");
		 */
		/*
		 * for (int i = 0; i < c.length; i++) { System.out.print(c[i]+", "); }
		 * System.out.println("]");
		 * System.out.println("The number of the segments by IDP is "+
		 * c.length);
		 * System.out.println("The Total error of the entire segments by IDP is "
		 * + p2.calcError(c) );
		 */

	}

	/**
	 * 读取txt文件的内容
	 * 
	 * @param file
	 *            想要读取的文件对象
	 * @return 返回文件内容
	 */
	public static Double[] txt2String(File file) {
		// StringBuilder result = new StringBuilder();
		// try {
		// BufferedReader br = new BufferedReader(new FileReader(file));//
		// 构造一个BufferedReader类来读取文件
		// String s = null;
		// while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
		// result.append(System.lineSeparator() + s);
		// }
		// br.close();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// String[] datastr = new String[10957313];
		// Double[] datasour = new Double[10957313];
		// //String[] resultstr = result.toString().replaceAll("\\s*",
		// "").split(",");
		// String[] resultstr = result.toString().split("\r\n");
		// int xloc = 0;
		// for (int i = 0; i < resultstr.length; i++) {
		// //resultstr[i].replace("\r\n", "");
		// //String regex = "e?-\\d+$";
		// //resultstr[i].;
		// if (resultstr[i].compareTo("")== 0) {
		// continue;
		// }
		// // datasour[i] = Double.parseDouble(resultstr[i].replaceAll("\\s*",
		// ""));
		// datastr = (resultstr[i].split(","));
		// /*if(xloc + datastr.length <= 1000){
		// for (int j = 0; j < datastr.length; j++) {
		// datasour[xloc] = Double.parseDouble(datastr[j]);
		// xloc++;
		// }
		// }
		// else{break; }*/
		// //datasour[i] = Double.parseDouble(resultstr[i].replaceAll(regex,
		// ""));
		// /*System.out.print(datasour[i]+" ");
		// if(i%10 == 0){
		// System.out.println();
		// }*/
		// }
		ArrayList<Double> datasour = new ArrayList<Double>();
		String resultstr = "";
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			do {
				resultstr = br.readLine();
			} while (resultstr.length() < 1);
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 构造一个BufferedReader类来读取文件

		String[] splitedStr = resultstr.split(",");
		for (int j = 1; j < splitedStr.length; j++)
			if (!"".equals(splitedStr[j]))
				datasour.add(Double.parseDouble(splitedStr[j]));
		System.out.println("读入数据的长度为： " + datasour.size());
		System.out.println(datasour);
		Double datasourArray[] = new Double[datasour.size()];
		datasour.toArray(datasourArray);
		return datasourArray;
	}

}
