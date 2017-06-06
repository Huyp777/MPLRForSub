package plr.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadFile {
	

	/**
	 *  read  contents in *.txt files and delete the class identity
	 * 
	 * @param file
	 *            the file path
	 * @return the contents of file
	 */
	public static Double[] txt2String(File file) {

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
		}// Construct the BufferedReader to read file
		String[] splitedStr = resultstr.split(",");
		for (int j = 1; j < splitedStr.length; j++)
			if (!"".equals(splitedStr[j]))
				datasour.add(Double.parseDouble(splitedStr[j]));
		/*System.out.println("The length of data£º " + datasour.size());
		System.out.println(datasour);*/
		Double datasourArray[] = new Double[datasour.size()];
		datasour.toArray(datasourArray);
		return datasourArray;
	}
	
	/**
	 *  read  contents in *.txt files 
	 * 
	 * @param file
	 *            the file path
	 * @return the contents of file
	 */
	
	public static double[] onetxt2String(File file) {

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
		}// Construct the BufferedReader to read file

		String[] splitedStr = resultstr.split(",");
		for (int j = 0; j < splitedStr.length; j++)
			if (!"".equals(splitedStr[j]))
				datasour.add(Double.parseDouble(splitedStr[j]));
		/*System.out.println("The length of data£º " + datasour.size());
		System.out.println(datasour);*/
		double datasourArray[] = new double[datasour.size()];
		for (int i = 0; i < datasour.size(); i++) {
			datasourArray[i] = datasour.get(i);
		}
		return datasourArray;
	}
	
	/**
	 *  read  contents in longda.txt files from 125 to 136 for Fig 9 and Fig 10  
	 * 
	 * @param file
	 *            the file path
	 * @return the contents of file
	 */
	
	public static double[] txt2StringSpecial(File file) {

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
		}// Construct the BufferedReader to read file

		String[] splitedStr = resultstr.split(",");
		for (int j = 125; j < 137; j++)
			if (!"".equals(splitedStr[j]))
				datasour.add(Double.parseDouble(splitedStr[j]));
		System.out.println("The length of data£º " + datasour.size());
		System.out.println(datasour);
		double datasourArray[] = new double[datasour.size()];
		for (int i = 0; i < datasour.size(); i++) {
			datasourArray[i] = datasour.get(i);
		}
		return datasourArray;
	}

}
