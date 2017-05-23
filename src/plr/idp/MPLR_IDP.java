package plr.idp;

import java.util.ArrayList;
import java.util.List;

public class MPLR_IDP {
	private int[] point;
	private double[] eui;
	private List<Line> lineListforIDP;
	private double data[];

	public MPLR_IDP(Double[] oriData) {
		int length = oriData.length;
		point = new int[length];
		eui = new double[length];
		data = new double[length];
		for (int i = 0; i < length; i++) {
			point[i] = 0;// 默认未选取
			eui[i] = Double.MAX_VALUE;
			data[i] = oriData[i];
		}
		lineListforIDP = new ArrayList<Line>();
	}

	/**
	 * 按阈值分段
	 * 
	 * @param threshold
	 *            分段误差
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int[] choosePointIDPByThreshold(double threshold,int count) {
		point[0] = 1;
		point[data.length - 1] = 1;
//		int count = 10;                // multiplication
		updataInfo(0, data.length - 1, count);
		Line line;
		do  {
			line = lineListforIDP.get(0);
			point[line.getPmax()] = 1;
			updataInfo(line.getBegin(), line.getPmax(), count);
			updataInfo(line.getPmax(), line.getEnd(), count);
			lineListforIDP.remove(0);
			lineListforIDP.sort(new LineComparatorIDP());
			line = lineListforIDP.get(0);
		}while(line.getWeight() >= threshold);
		return point;

	}

	/**
	 * 按个数
	 * 
	 * @param number
	 *            分段个数
	 * @return
	 */
	/*public int[] choosePointIDPByNumber(int number) {
		point[0] = 1;
		point[data.length - 1] = 1;
		updataInfo(0, data.length - 1);
		lineListforIDP.sort(new LineComparatorIDP());
		int pointNumber = 2;
		while (pointNumber < number) {
			pointNumber++;
			Line line = lineListforIDP.get(0);
			point[line.getPmax()] = 1;
			updataInfo(line.getBegin(), line.getPmax());
			updataInfo(line.getPmax(), line.getEnd());
			lineListforIDP.remove(0);
			lineListforIDP.sort(new LineComparatorIDP());
		}
		return point;

	}*/

	// 更新信息
	public void updataInfo(int begin, int end, int mul) {
		int pmax = begin;
		double dist = 0;
		double distmax = 0;
		eui[begin] = 0;
		eui[end] = 0;
		for (int i = begin + 1; i < end; i++) {
			eui[i] = dist(begin + 1, data[begin], end + 1, data[end], i + 1,
					data[i]);
			dist += eui[i];
			if (eui[i] > distmax) {
				pmax = i;
				distmax = eui[i];
			}
		}
		// weight计算PLR_IDP和PLR_SIP不同
		//double weight = 2 * distmax > dist ? 2 * distmax : dist;
		double weight = mul * distmax > dist ? mul * distmax : dist;
		Line line = new Line(begin, end, dist, distmax, pmax, weight);
		lineListforIDP.add(line);

	}

	// 计算点到直线的距离
	public double dist(double x1, double y1, double x2, double y2, double x0,
			double y0) {
		return Math.abs((x0 - x1) * (y2 - y1) / (x2 - x1) + y1 - y0);
	}

	// 按阈值返回索引
	public int[] getIDPIndexByThreshold(double threshold,int count) {
		int[] IDP = choosePointIDPByThreshold(threshold, count);
		List<Integer> list = new ArrayList<Integer>();
		int number = 0;
		for (int i = 0; i < IDP.length; i++) {
			if (IDP[i] == 1) {
				number++;
				list.add(i);
			}
		}
		int[] IDPindex = new int[number];
		for (int i = 0; i < list.size(); i++) {
			IDPindex[i] = list.get(i);
		}
		return IDPindex;
	}
	// 计算MPLR拟合误差
	public double calcError(int[] seg) {
		double sum_Err = 0.0;
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.000000");
		int begin = 0;
		int last = data.length;
		for (int i = 0; i < seg.length; i++) {
			begin = seg[i];
			if (i + 1 < seg.length) {
				int end = seg[i + 1];
				for (int j = begin + 1; j < end; j++) {
					sum_Err += dist(begin, data[begin], end, data[end], j,
							data[j]);
				}
			}

		}
		for (int j = begin + 1; j < last; j++) {
			sum_Err += dist(begin, data[begin], last, data[last], j,
					data[j]);
			sum_Err = Double.parseDouble(df.format(sum_Err));
		}
		return sum_Err;
	}
	// 按个数返回索引
	/*public int[] getIDPIndexByNumber(int number) {
		int[] IDP = choosePointIDPByNumber(number);
		
		int[] IDPindex = new int[number];
		int index = 0;
		for (int i = 0; i < IDP.length; i++) {
			if (IDP[i] == 1) {
				IDPindex[index]=i;
				index++;
			}
		}
		return IDPindex;
	}*/
	/*public static void main(String[] args){
		Double[] a={10.1,12.3,12.0,15.0,17.0,18.0,12.3,14.4,12.3,5.6,12.4,8.6};
		MPLR_IDP p=new MPLR_IDP(a);
		//int[] b=p.getIDPIndexByNumber(5);
		int[] c=p.getIDPIndexByThreshold(3);
		System.out.print(1);
	}*/

}