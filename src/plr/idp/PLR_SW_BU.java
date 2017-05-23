package plr.idp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class PLR_SW_BU {
	private int[] point;
	private int seg_SW_num;
	private int seg_BU_num;
	private int seg_Back_num;
	private int dataLen;
	private double[] eui;
	private List<Integer> segSWList;
	private List<Integer> segBackList;
	private List<Integer> segStepList;
	private List<Segments> segmentsList;
	private double data[];
	private List<Integer> segtemp;
	private List<Line> lineList;
	private List<Segments> prioList;

	// private SegmentsComparator segCompara;

	public PLR_SW_BU(Double[] oriData) {
		dataLen = oriData.length;
		seg_SW_num = 0;
		seg_BU_num = 0;
		seg_Back_num = 0;
		point = new int[dataLen];
		eui = new double[dataLen];
		data = new double[dataLen];
		for (int i = 0; i < dataLen; i++) {
			point[i] = 0;// 默认未选取
			eui[i] = Double.MAX_VALUE;
			if (oriData[i] != null) {
				data[i] = oriData[i];
			} else {
				break;
			}
		}
		segmentsList = new ArrayList<Segments>();
		segSWList = new ArrayList<Integer>();
		segBackList = new ArrayList<Integer>();
		segStepList = new ArrayList<Integer>();
		segtemp = new ArrayList<Integer>();
		lineList = new ArrayList<Line>();
		// segCompara = new SegmentsComparator();
		prioList = new ArrayList<Segments>();
	}

	public int getSeg_Back_num() {
		return seg_Back_num;
	}

	public int getSeg_SW_num() {
		return seg_SW_num;
	}

	public void setSeg_SW_num(int seg_num) {
		this.seg_SW_num = seg_num;
	}

	public int getSeg_SWAB_num() {
		return seg_BU_num;
	}

	public void setSeg_SWAB_num(int seg_num) {
		this.seg_BU_num = seg_num;
	}

	public List<Integer> getSegtemp() {
		return segtemp;
	}

	public void setSegtemp(List<Integer> segtemp) {
		this.segtemp = segtemp;
	}

	/**
	 * 按照滑动窗口SW分段
	 * 
	 * @param threshold
	 *            分段误差
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List segmentBySW(double threshold) {
		int current_csp, i;
		int start_pos = current_csp = 0;
		segSWList.clear();
		segSWList.add(start_pos);
		current_csp = i = start_pos + 1;
		while (current_csp < data.length - 1) {
			while (calcError(start_pos, i, threshold) != 0) {
				current_csp = i;
				i++;
			}
			if (current_csp <= data.length - 1) {
				start_pos = current_csp;
				current_csp++;
				i = current_csp + 1;
				segSWList.add(start_pos);
				seg_SW_num++;
			}
		}
		if (current_csp == data.length - 1) {
			segSWList.add(current_csp);
			seg_SW_num++;
		}

		return segSWList;

	}

	/**
	 * 按照滑动窗口SWAB中的BU进行分段
	 * 
	 * @param threshold
	 *            分段误差
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List segmentByBU(double threshold) {
		int loc = 0;
		int end = 0;
		Segments segment;
		int segbegin = 0;
		double mercost = Double.MAX_VALUE;
		double mincost = 0.0;
		initPrioList(0, dataLen - 1);
		int sig = 0;
		if (prioList.size() > 0) {
			do {
				sig = calcMerge(prioList, threshold);
			} while (sig != 1);
		}
		seg_BU_num = prioList.size() + 1;
		return prioList;

		/*
		 * System.out.print("[ "); for (Segments i : prioList) {
		 * System.out.print(i.getBegin()+"--"+i.getEnd()+"###  "); }
		 * System.out.println(" ]");
		 */

	}

	public void initPrioList(int loc, int end) {
		if (loc < end) {
			Segments segment;
			for (int k = loc; k < end; k++) {
				segment = new Segments(k, k + 1, 0);
				prioList.add(segment);
			}
		}
	}

	public int calcMerge(List<Segments> prio, double threshold) {
		double mincost = Double.MAX_VALUE;
		double cost = Double.MAX_VALUE;
		int minloc = 0;
		int i = 0;
		while (prio.size() > 0) {
			mincost = cost = Double.MAX_VALUE;
			for (i = 0; i < prio.size() - 1; i++) {
				cost = mergeNext(prio.get(i), prio.get(i + 1));
				if (mincost > cost) {
					mincost = cost;
					minloc = i;
				}
			}
			if (mincost <= threshold) {
				Segments newseg = prio.get(minloc);
				newseg.setEnd(prio.get(minloc + 1).getEnd());
				/*
				 * if(minloc+2 < prio.size()){ cost = mergeNext(newseg,
				 * prio.get(minloc+2)); newseg.setMegerCost(cost); }
				 * if(minloc-1>=0){ Segments preseg = prio.get(minloc-1); cost =
				 * mergeNext(preseg, newseg); preseg.setMegerCost(cost);
				 * prio.set(minloc-1, preseg); }
				 */
				prio.set(minloc, newseg);
				prio.remove(minloc + 1);
			} else {
				return 1;
			}
		}
		return 1;
	}

	public double mergeNext(Segments segmin, Segments next) {
		int begin = segmin.getBegin();
		int end = next.getEnd();
		double summercost = 0.0;
		for (int i = begin + 1; i < end; i++) {
			summercost += dist(begin, data[begin], end, data[end], i, data[i]);
		}
		return summercost;
	}

	public Segments findNext(int i) {
		Iterator<Segments> iter = prioList.iterator();
		while (iter.hasNext()) {
			Segments segnext = iter.next();
			if (segnext.getBegin() == i) {
				// iter.remove();
				return segnext;
			}
		}
		return null;
	}

	// 计算误差By List for SW
	public double calcErrorforSW(List seg) {
		double sum_Err = 0.0;
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.000000");
		int begin = 0;
		int lastend = dataLen - 1;
		// return Math.abs(Double.parseDouble(df.format((x0 - x1)* (y2 - y1) /
		// (x2 - x1))) + Double.parseDouble(df.format(y1 - y0)));
		for (int i = 0; i < seg.size(); i++) {
			begin = (Integer) seg.get(i);
			if (i + 1 < seg.size()) {
				int end = (Integer) seg.get(i + 1);
				for (int j = begin + 1; j < end; j++) {
					sum_Err += dist(begin, data[begin], end, data[end], j,
							data[j]);
					sum_Err = Double.parseDouble(df.format(sum_Err));
					// System.out.println("begin: "+begin+" end: "+
					// end+" sum_error: "+sum_Err);
				}
			}

		}
		for (int j = begin + 1; j < lastend; j++) {
			sum_Err += dist(begin, data[begin], lastend, data[lastend], j,
					data[j]);
			sum_Err = Double.parseDouble(df.format(sum_Err));
		}
		return sum_Err;
	}

	// 计算误差By List for BU
	public double calcErrorforBU(List<Segments> seg) {
		double sum_Err = 0.0;
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.000000");
		// return Math.abs(Double.parseDouble(df.format((x0 - x1)* (y2 - y1) /
		// (x2 - x1))) + Double.parseDouble(df.format(y1 - y0)));
		for (int i = 0; i < seg.size(); i++) {
			int begin = (Integer) seg.get(i).getBegin();
			int end = (Integer) seg.get(i).getEnd();
			for (int j = begin + 1; j < end; j++) {
				sum_Err += dist(begin, data[begin], end, data[end], j, data[j]);
				sum_Err = Double.parseDouble(df.format(sum_Err));
				// System.out.println("begin: "+begin+" end: "+
				// end+" sum_error: "+sum_Err);
			}
		}
		return sum_Err;
	}

	// 计算误差By int[]
	public double calcError(int[] seg) {
		double sum_Err = 0.0;
		for (int i = 0; i < seg.length; i++) {
			int begin = seg[i];
			if (i + 1 < seg.length) {
				int end = seg[i + 1];
				for (int j = begin + 1; j < end; j++) {
					sum_Err += dist(begin, data[begin], end, data[end], j,
							data[j]);
				}
			}

		}
		return sum_Err;
	}

	// 计算误差By range
	public int calcError(int begin, int end, double threshold) {
		double sum_Err = 0.0;
		if (end >= data.length) {
			return 0;
		}
		for (int i = begin + 1; i < end; i++) {
			sum_Err += dist(begin, data[begin], end, data[end], i, data[i]);
			if (sum_Err > threshold) {
				return 0;
			}
		}
		return 1;
	}

	// 返回计算误差By range
	public double calcSumError(int begin, int end, int loc) {
		double sum_Err = 0.0;
		if (end > loc) {
			return Double.MAX_VALUE;
		}
		for (int i = begin + 1; i < end; i++) {
			sum_Err += dist(begin, data[begin], end, data[end], i, data[i]);
		}
		return sum_Err;
	}

	// 计算点到直线的距离
	public double dist(int x1, double y1, int x2, double y2, int x0, double y0) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.000000");
		return Math.abs(Double.parseDouble(df.format((x0 - x1) * (y2 - y1)
				/ (x2 - x1) + y1 - y0)));
	}

	// 计算点到直线的距离
	public double dist(double x1, double y1, double x2, double y2, double x0,
			double y0) {
		return Math.abs((x0 - x1) * (y2 - y1) / (x2 - x1) + y1 - y0);
	}

	// 按阈值返回索引
	public List getIDPIndexByThreshold(double threshold) {
		List<Segments> BUList = segmentByBU(threshold);
		List<Integer> BUListLoc = new ArrayList<Integer>();
		System.out.println(BUList);
		if (BUList.size() != 0) {
			BUListLoc.add(BUList.get(0).getBegin());
		}
		for (int i = 0; i < BUList.size(); i++) {
			BUListLoc.add(BUList.get(0).getEnd());
		}
		System.out.println(BUList);
		/*
		 * int[] IDP = choosePointIDPByThreshold(threshold, mul); List<Integer>
		 * list = new ArrayList<Integer>(); int number = 0; for (int i = 0; i <
		 * IDP.length; i++) { if (IDP[i] == 1) { number++; list.add(i); } }
		 * int[] IDPindex = new int[number]; for (int i = 0; i < list.size();
		 * i++) { IDPindex[i] = list.get(i); } return IDPindex;
		 */
		return BUListLoc;
	}

	/*
	 * public static void main(String[] args){ //Double[]
	 * a={10.1,12.3,12.0,15.0,17.0,18.0,12.3,14.4,12.3,5.6,12.4,8.6}; //Double[]
	 * a={18.0,17.0,15.0,12.0,12.3,10.1}; //double[]
	 * test={18.0,17.0,15.0,12.0,12.3,10.1}; //Double[]
	 * a={10.1,12.3,12.0,15.0,17.0,18.0,12.3,14.4,12.3,5.6,12.4,8.6}; //Double[]
	 * a
	 * ={-11.6,-9.3,-7.8,-7.1,-5.8,-5.6,-5.6,-5.6,-5.7,-5.8,-5.8,-5.9,-5.8,-5.9,
	 * -
	 * 5.9,-5.6,-5.8,-5.9,-6.0,-6.1,-6.2,-6.3,-6.5,-6.5,-6.6,-6.7,-6.8,-6.9,-6.9
	 * ,-6.8,-7.0,-6.8,-6.9,-7.2,-7.1,-6.6,-7.3,-7.3,-7.0,-7.4,-7.3,-7.5};
	 * //Double[]
	 * a={7.5,7.3,7.4,7.0,7.3,7.3,6.6,7.1,7.2,6.9,6.8,7.0,6.8,6.9,6.9,
	 * 6.8,6.7,6.6
	 * ,6.5,6.5,6.3,6.2,6.1,6.0,5.9,5.8,5.6,5.9,5.9,5.8,5.9,5.8,5.8,5.7
	 * ,5.6,5.6,5.6
	 * ,5.8,7.1,7.8,9.3,11.6,14.8,17.1,16.6,17.0,17.1,17.2,17.1,17.0,
	 * 17.0,16.8,16.8
	 * ,16.7,16.6,16.5,16.5,16.3,16.2,16.1,16.0,15.9,15.7,15.5,15.4
	 * ,15.2,15.0,14.9
	 * ,14.7,14.6,14.5,14.4,14.3,14.3,14.2,14.3,14.5,15.3,17.1,18.0
	 * ,17.1,16.8,16.6
	 * ,16.5,16.0,16.0,15.9,15.8,15.6,15.1,14.3,14.6,14.7,14.3,14.3
	 * ,34.0,34.0,34.0
	 * ,33.8,13.7,13.5,13.4,13.2,13.0,12.9,12.7,12.5,12.3,12.2,12.0
	 * ,11.9,11.3,9.2,9.0}; Double[] a = { -7.5, -7.3, -7.4, -7.0, -7.3, -7.3,
	 * -6.6, -7.1, -7.2, -6.9, -6.8, -7.0, -6.8, -6.9, -6.9, -6.8, -6.7, -6.6,
	 * -6.5, -6.5, -6.3, -6.2, -6.1, -6.0, -5.9, -5.8, -5.6, -5.9, -5.9, -5.8,
	 * -5.9, -5.8, -5.8, -5.7, -5.6, -5.6, -5.6, -5.8, -7.1, -7.8, -9.3, -11.6,
	 * -14.8, -17.1, -16.6, -17.0, -17.1, -17.2, -17.1, -17.0, -17.0, -16.8,
	 * -16.8, -16.7, -16.6, -16.5, -16.5, -16.3, -16.2, -16.1, -16.0, -15.9,
	 * -15.7, -15.5, -15.4, -15.2, -15.0, -14.9, -14.7, -14.6, -14.5, -14.4,
	 * -14.3, -14.3, -14.2, -14.3, -14.5, -15.3, -17.1, -18.0, -17.1, -16.8,
	 * -16.6, -16.5, -16.0, -16.0, -15.9, -15.8, -15.6, -15.1, -14.3, -14.6,
	 * -14.7, -14.3, -14.3, -34.0, -34.0, -34.0, -33.8, -13.7, -13.5, -13.4,
	 * -13.2, -13.0, -12.9, -12.7, -12.5, -12.3, -12.2, -12.0, -11.9, -11.3,
	 * -11.5, -11.5, -11.0, -11.1, -11.0, -10.9, -10.8, -10.7, -10.7, -10.6,
	 * -10.5, -10.4, -10.3, -9.6, -33.9, -34.4, -34.0, -34.3, -9.0, -8.3, -8.6,
	 * -7.7, -8.3, -6.3, -3.3, -5.4, -4.6, -5.8, -5.6, -6.0, -6.1, -5.6, -5.4,
	 * -5.8, -5.6, -5.8, -5.9, -5.9, -5.7, -5.8, -5.5, -4.6, -5.3, -5.4, -5.4,
	 * -5.4, -5.4, -5.3, -5.3, -5.1, -5.0, -5.0, -4.9, -4.7, -4.6, -4.6, -4.6,
	 * -4.5, -4.3, -4.2, -4.1, -4.0, -3.9, -3.8, -3.6, -3.6, -3.5, -3.4, -3.3,
	 * -3.2, -5.0, -6.0, -9.8, -17.1, -22.2, -25.3, -26.0, -25.6, -25.3, -24.8,
	 * -24.3, -23.9, -23.5, -23.1, -22.7, -22.4, -22.1, -21.6, -21.3, -20.8,
	 * -20.3, -19.8, -19.4, -19.0, -18.5, -18.1, -17.8, -17.4, -17.0, -16.7,
	 * -16.4, -16.2, -16.0, -15.7, -15.5, -14.7, -14.5, -14.3, -14.1, -14.0,
	 * -13.9, -14.2, -14.3, -14.1, -13.8, -14.1, -13.9, -13.8, -13.6, -13.1,
	 * -13.1, -13.1, -12.8, -12.7, -12.7, -12.6, -12.5, -12.4, -32.3, -32.2,
	 * -32.1, -31.9, -31.8, -31.7, -31.6, -31.5, -31.4, -31.3, -31.1, -31.0,
	 * -30.9, -30.8, -30.6, -30.5, -30.4, -30.3, -30.1, -30.0, -29.9, -29.8,
	 * -9.6, -9.2, -9.4, -9.5, -9.6, -9.6, -9.6, -9.6, -9.0, -9.3, -9.3, -9.3,
	 * -9.4, -9.5, -9.5, -9.5, -9.5, -9.5, -9.5, -9.5, -9.5, -9.5, -9.3, -9.3,
	 * -9.2, -9.0 }; PLR_SW_SWAB p=new PLR_SW_SWAB(a); List segSWAB =
	 * p.segmentByBU(5.0); List segSW = p.getSegtemp(); int[]
	 * c=p.getIDPIndexByThreshold(5.0, 2); int[] b=p.getIDPIndexByNumber(5);
	 * int[] c=p.getIDPIndexByThreshold(3);
	 * System.out.println("The number of the segments by SW is "+
	 * p.getSeg_SW_num());
	 * System.out.println("The number of the segments by SWAB is "+
	 * p.getSeg_SWAB_num());
	 * System.out.println("The number of the segments by IDP is "+ c.length);
	 * System.out.print(segSW); System.out.println(); System.out.print(segSWAB);
	 * System.out.println();
	 * 
	 * System.out.print("["); for (int i = 0; i < c.length; i++) {
	 * System.out.print(c[i]+", "); } System.out.println("]");
	 * System.out.println("The Total error of the entire segments by SW is "+
	 * p.calcError(segSW) );
	 * System.out.println("The Total error of the entire segments by SWAB is "+
	 * p.calcError(segSWAB) );
	 * System.out.println("The Total error of the entire segments by IDP is "+
	 * p.calcError(c) ); }
	 */
}
