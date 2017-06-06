package plr.idp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import plr.segments.SegmentForBU;

public class PLR_SW_BU {
	private int[] point;
	private int seg_SW_num;
	private int seg_BU_num;
	private int seg_Back_num;
	private int dataLen;
	private double[] eui;
	private List<Integer> segSWList;
	/*private List<Integer> segBackList;
	private List<Integer> segStepList;
	private List<Segments> segmentsList;*/
	private double data[];
	private List<Integer> segtemp;
	//private List<Line> lineList;
	private List<SegmentForBU> prioList;

	// private SegmentsComparator segCompara;

	public PLR_SW_BU(double[] oriData) {
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
			data[i] = oriData[i];
		}
		/*segmentsList = new ArrayList<Segments>();
		segBackList = new ArrayList<Integer>();
		segStepList = new ArrayList<Integer>();
		lineList = new ArrayList<Line>();*/
		segSWList = new ArrayList<Integer>();
		segtemp = new ArrayList<Integer>();
		// segCompara = new SegmentsComparator();
		prioList = new ArrayList<SegmentForBU>();
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
	 * Segmentation by slide window
	 * 
	 * @param threshold
	 *        fitting error for segments    
	 * @return
	 */
	
	public List<Integer> segmentBySW(double threshold) {
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
	 *  Segmentation by Bottom Up
	 * 
	 * @param threshold
	 *            fitting error for segments  
	 * @return
	 */
	
	public List<SegmentForBU> segmentByBU(double threshold) {
		initPrioList(0, dataLen - 1);
		int sig = 0;
		if (prioList.size() > 0) {
			do {
				sig = mergeSegments(prioList, threshold);
			} while (sig != 1);
		}
		seg_BU_num = prioList.size() + 1;
		return prioList;
	}

	public void initPrioList(int loc, int end) {
		if (loc < end) {
			SegmentForBU segment;
			for (int k = loc; k < end; k++) {
				segment = new SegmentForBU(k, k + 1, 0);
				prioList.add(segment);
			}
		}
	}
	/**
	 *  Merge the two adjacent segments with the minimum cost
	 * 
	 * @param threshold
	 *            fitting error for segments  
	 * @return
	 */
	public int mergeSegments(List<SegmentForBU> prio, double threshold) {
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
				SegmentForBU newseg = prio.get(minloc);
				newseg.setEnd(prio.get(minloc + 1).getEnd());
				prio.set(minloc, newseg);
				prio.remove(minloc + 1);
			} else {
				return 1;
			}
		}
		return 1;
	}

	public double mergeNext(SegmentForBU segmin, SegmentForBU next) {
		int begin = segmin.getBegin();
		int end = next.getEnd();
		double summercost = 0.0;
		for (int i = begin + 1; i < end; i++) {
			summercost += dist(begin, data[begin], end, data[end], i, data[i]);
		}
		return summercost;
	}

	public SegmentForBU findNext(int i) {
		Iterator<SegmentForBU> iter = prioList.iterator();
		while (iter.hasNext()) {
			SegmentForBU segnext = iter.next();
			if (segnext.getBegin() == i) {
				return segnext;
			}
		}
		return null;
	}

	// Calculate the fitting error for SW 
	public double calcErrorforSW(List<Integer> seg) {
		double sum_Err = 0.0;
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.000000");
		int begin = 0;
		int lastend = dataLen - 1;
		for (int i = 0; i < seg.size(); i++) {
			begin = seg.get(i);
			if (i + 1 < seg.size()) {
				int end = (Integer) seg.get(i + 1);
				for (int j = begin + 1; j < end; j++) {
					sum_Err += dist(begin+1, data[begin], end+1, data[end], j+1,
							data[j]);
					sum_Err = Double.parseDouble(df.format(sum_Err));
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

	// Calculate the fitting error for BU
	public double calcErrorforBU(List<SegmentForBU> seg) {
		double sum_Err = 0.0;
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.000000");
		for (int i = 0; i < seg.size(); i++) {
			int begin = (Integer) seg.get(i).getBegin();
			int end = (Integer) seg.get(i).getEnd();
			for (int j = begin + 1; j < end; j++) {
				sum_Err += dist(begin+1, data[begin], end+1, data[end], j+1, data[j]);
				sum_Err = Double.parseDouble(df.format(sum_Err));
			}
		}
		return sum_Err;
	}
	
	// Calculate the fitting error of range
	public int calcError(int begin, int end, double threshold) {
		double sum_Err = 0.0;
		if (end >= data.length) {
			return 0;
		}
		for (int i = begin + 1; i < end; i++) {
			sum_Err += dist(begin+1, data[begin], end+1, data[end], i+1, data[i]);
			if (sum_Err > threshold) {
				return 0;
			}
		}
		return 1;
	}
	// Calculate the fitting error of array
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

	// calculate vertical distance 
	public double dist(int x1, double y1, int x2, double y2, int x0, double y0) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.000000");
		return Math.abs(Double.parseDouble(df.format((x0 - x1) * (y2 - y1)
				/ (x2 - x1) + y1 - y0)));
	}

/*
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
		return BUListLoc;
	}*/

}
