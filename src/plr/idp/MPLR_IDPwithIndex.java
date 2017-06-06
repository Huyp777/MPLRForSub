package plr.idp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import plr.index.IndexTreeNode;
import plr.index.PriListComparErrorG;
import plr.index.PriTree;
import plr.index.PriListNode;
import plr.index.PriorityList;
import plr.segments.Segment;
import plr.segments.SegmentComparatorIDP;

public class MPLR_IDPwithIndex {
	private int[] point;
	private double[] eui;
	private List<Segment> segListforIDP;
	private double data[];
	private int count;
	private PriorityList priIndexList;
	private PriTree pritree;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}


	public MPLR_IDPwithIndex(double[] oriData, int count) {
		int length = oriData.length;
		point = new int[length];
		eui = new double[length];
		data = new double[length];
		for (int i = 0; i < length; i++) {
			point[i] = -1;// 默认未选取
			eui[i] = Double.MAX_VALUE;
			data[i] = oriData[i];
		}
		segListforIDP = new ArrayList<Segment>();
		this.priIndexList = new PriorityList();
		this.pritree = new PriTree();
		this.count = count;

	}

	public void initPLR(int count) {
		for (int i = 0; i < point.length; i++) {
			point[i] = -1;
		}
		point[0] = 0;
		point[data.length - 1] = 1;
		segListforIDP.clear();
		this.count = count;
		Segment initline = updataInfo(0, data.length - 1);
		priIndexList.initPriList(initline, data[0], data[data.length-1], data.length-1);
		pritree.initIndexTree(initline, data[0], data[data.length - 1],data.length - 1);
	}

	public PriTree getPritree() {
		return pritree;
	}

	public void setPritree(PriTree pritree) {
		this.pritree = pritree;
	}
	
	

	public PriorityList getPriList() {
		return priIndexList;
	}

	public void setPriList(PriorityList priList) {
		this.priIndexList = priList;
	}
	

	
	// MPLR based on FER 
	
	public void mplrInFER(double fe_ratio) {
		PriListNode curNode = priIndexList.errorGbyFER(fe_ratio);
		if (curNode!=null) {
			int curRank = curNode.getRank();
			//traverse indexTree to get all indexTreeNode whose rank is no greater than curRank
			List<IndexTreeNode> treeNodeList = pritree.errorS(curRank);
			Iterator<IndexTreeNode> iter = treeNodeList.iterator();
			System.out.println("The current error_g is: "
					+ curNode.getCurFittingErr() + " according to FER("
					+ fe_ratio * 100 + "%)");
			System.out.println("All error_s according to the current error_g("
					+ curNode.getCurFittingErr() + ") are listed as follow: ");
			double errors = 0.0f;
			double sumerrors = 0.0f;
			IndexTreeNode nodeF = (IndexTreeNode) iter.next();
			while (iter.hasNext()) {
				IndexTreeNode nodeN = (IndexTreeNode) iter.next();
				if (nodeF != null && nodeN != null) {
					if (nodeF.getRank() < nodeN.getRank()) {
						errors = nodeN.getLeftDist();
					} else {
						errors = nodeF.getRightDist();
					}
					sumerrors += errors;
					System.out.println(" Begin point: " + (nodeF.getIndex()+1)
							+ " End point: " + (nodeN.getIndex()+1) + " error_s: "
							+ errors);
				}
				nodeF = nodeN;
			}
			System.out.println("the cumulative error_s: " + sumerrors
					+ " is the same as error_g: " + curNode.getCurFittingErr());
		}
		else{
			System.out.println("There is something wrong with parameter FER: "+fe_ratio);
		}
	}
	
	public void mplrInDCR(double num_ratio) {
		PriListNode curNode = priIndexList.errorGbyDCR(num_ratio);
		if (curNode != null) {
			int curRank = curNode.getRank();
			//traverse indexTree to get all indexTreeNode whose rank is no greater than curRank
			List<IndexTreeNode> treeNodeList = pritree.errorS(curRank);
			Iterator<IndexTreeNode> iter = treeNodeList.iterator();
			int curDCR = (int) (num_ratio*100);
			System.out.println("The current error_g is: "
					+ curNode.getCurFittingErr() + " according to DCR("
					+ curDCR + "%)");
			System.out.println("All error_s according to the current error_g("
					+ curNode.getCurFittingErr() + ") are listed as follow: ");
			double errors = 0.0f;
			double sumerrors = 0.0f;
			IndexTreeNode nodeF = (IndexTreeNode) iter.next();
			while (iter.hasNext()) {
				IndexTreeNode nodeN = (IndexTreeNode) iter.next();
				if (nodeF != null && nodeN != null) {
					if (nodeF.getRank() < nodeN.getRank()) {
						errors = nodeN.getLeftDist();
					} else {
						errors = nodeF.getRightDist();
					}
					sumerrors += errors;
					System.out.println(" Begin point: " + (nodeF.getIndex()+1)
							+ " End point: " + (nodeN.getIndex()+1) + " error_s: "
							+ errors);
				}
				nodeF = nodeN;
			}
			System.out.println("the cumulative error_s: " + sumerrors
					+ " is the same as error_g: " + curNode.getCurFittingErr());
		}
		else{
			System.out.println("There is something wrong with parameter DCR: "+num_ratio);
		}
	}
	
	
	

	/**
	 * 按阈值分段
	 * 
	 * @param threshold
	 *            分段误差
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int[] chooseIDPsByThreshold(double threshold, int count) {

		int segBegin = 0;
		int segEnd = data.length - 1;
		int selectIDP;
		Segment seg;
		Segment firstSeg;
		Segment nextSeg;
		initPLR(count); // 初始化
		int rank = 3;
		seg = segListforIDP.get(0);
		while (seg.getWeight() >= threshold) {
			point[seg.getPmax()] = rank - 1;

			segBegin = seg.getBegin();
			segEnd = seg.getEnd();
			selectIDP = seg.getPmax();
			firstSeg = updataInfo(segBegin, selectIDP);
			nextSeg = updataInfo(selectIDP, segEnd);
			priIndexList.createPriList(seg,firstSeg, nextSeg, rank);
			pritree.createIndexTree(seg, firstSeg, nextSeg, rank);
			rank++;
			segListforIDP.remove(0);

			segListforIDP.sort(new SegmentComparatorIDP());
			seg = segListforIDP.get(0);

		}
		return point;

	}

	/**
	 * 按个数
	 * 
	 * @param number
	 *            分段个数
	 * @return
	 */
	
	
	
	
	
	
	
	
	
	
	
	
	// 更新信息
	public Segment updataInfo(int begin, int end) {
		int pmax = begin;
		double dist = 0;
		double distmax = 0;
		double sourdata = 0;
		int mul = this.count;
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00000");
		for (int i = 0; i < data.length; i++) {
			eui[i] = 0;
		}
		for (int i = begin + 1; i < end; i++) {
			eui[i] = dist(begin + 1, data[begin], end + 1, data[end], i + 1,
					data[i]);
			dist += eui[i];
			if (eui[i] > distmax) {
				pmax = i;
				distmax = eui[i];
				sourdata = data[i];
			}
		}
		dist = Double.parseDouble(df.format(dist));
		distmax = Double.parseDouble(df.format(distmax));
		double weight = mul * distmax > dist ? mul * distmax : dist;
		Segment seg = new Segment(begin, end, dist, distmax, sourdata, pmax, weight);
		segListforIDP.add(seg);
		return seg;

	}

	// 计算点到直线的距离
	public double dist(double x1, double y1, double x2, double y2, double x0,
			double y0) {
		return Math.abs((x0 - x1) * (y2 - y1) / (x2 - x1) + y1 - y0);
	}

	// 按阈值返回索引
	public int[] getIDPIndexByThreshold(double threshold, int count) {
		int[] IDP = chooseIDPsByThreshold(threshold, count);
		// List<PriListNode> test = pritree.createIndexTree();
		List<Integer> list = new ArrayList<Integer>();
		int number = 0;
		for (int i = 0; i < IDP.length; i++) {
			if (IDP[i] != -1) {
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
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00000");
		int begin = 0;
		int last = data.length;
		for (int i = 0; i < seg.length; i++) {
			begin = seg[i];
			if (i + 1 < seg.length) {
				int end = seg[i + 1];
				for (int j = begin + 1; j < end; j++) {
					sum_Err += dist(begin + 1, data[begin], end + 1, data[end],
							j + 1, data[j]);
					sum_Err = Double.parseDouble(df.format(sum_Err));
				}
			}

		}
		for (int j = begin + 1; j < last; j++) {
			sum_Err += dist(begin + 1, data[begin], last + 1, data[last],
					j + 1, data[j]);
			sum_Err = Double.parseDouble(df.format(sum_Err));
		}
		return sum_Err;
	}
	
	
	

	// 计算MPLR拟合误差
	public double calcError() {
		double sum_Err = 0.0;
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00000");
		sum_Err = priIndexList.getIndexList().get(priIndexList.getIndexList().size() - 1)
				.getCurFittingErr();
		sum_Err = Double.parseDouble(df.format(sum_Err));
		return sum_Err;
	}

}