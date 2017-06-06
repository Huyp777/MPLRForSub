package plr.idp;

import java.util.ArrayList;
import java.util.List;

import plr.segments.Segment;
import plr.segments.SegmentComparatorIDP;

public class PLR_SIP {
	private int[] point;
	private double[] eui;
	private List<Segment> lineList;
	private double data[];
	
	public PLR_SIP(double[] data2){
		int length=data2.length;
		point=new int[length];
		eui=new double[length];
		data=new double[length];
		for(int i=0;i<length;i++){
			point[i]=0;//默认未选取
			eui[i]=Double.MAX_VALUE;
		    data[i]=data2[i];
		}
		lineList=new ArrayList<Segment>();
	}

	/**
	 * 按阈值分段
	 * @param threshold    分段误差       
	 * @return
	 */
	public int[] choosePointSIPByThreshold(double threshold) {
		point[0] = 1;
		point[data.length - 1] = 1;
		updataInfo(0,data.length-1);
		lineList.sort(new SegmentComparatorIDP());
		Segment line=lineList.get(0);	
		/*do  {
			lineList.sort(new LineComparatorIDP());
			line = lineList.get(0);
			point[line.getPmax()] = 1;
			updataInfo(line.getBegin(), line.getPmax());
			updataInfo(line.getPmax(), line.getEnd());
			lineList.remove(0);
			lineList.sort(new LineComparatorIDP());
			line = lineList.get(0);
		}while(line.getWeight() >= threshold);*/
		while(line.getWeight() >= threshold){
			lineList.sort(new SegmentComparatorIDP());
			line = lineList.get(0);
			point[line.getPmax()] = 1;
			updataInfo(line.getBegin(), line.getPmax());
			updataInfo(line.getPmax(), line.getEnd());
			lineList.remove(0);
			lineList.sort(new SegmentComparatorIDP());
			line = lineList.get(0);
		}
		return point;
		
	}
	
	/**
	 * 按个数
	 * @param number 分段个数       
	 * @return
	 */
	public int[] choosePointSIPByNumber(int number) {
		
		point[0] = 1;
		point[data.length - 1] = 1;
		updataInfo(0,data.length-1);
		lineList.sort(new SegmentComparatorIDP());
		int pointNumber=2;
		while (pointNumber < number) {
			pointNumber++;
			Segment line = lineList.get(0);
			point[line.getPmax()] = 1;
			updataInfo(line.getBegin(), line.getPmax());
			updataInfo(line.getPmax(), line.getEnd());
			lineList.remove(0);
			lineList.sort(new SegmentComparatorIDP());
		}
		return point;	
	}
	//更新信息
	public void updataInfo(int begin, int end){
		int pmax=begin;
		double dist=0;
		double distmax=0;
		double sourdata = 0;
		eui[begin]=0;
		eui[end]=0;
		for (int i = begin+1; i < end; i++) {
			eui[i] = dist(begin + 1, data[begin], end + 1, data[end], i + 1, data[i]);
			dist+=eui[i];
			if(eui[i]>distmax){
				pmax=i;
				distmax=eui[i];
				sourdata = data[i];
			}
		}
		//weight计算PLR_IDP和PLR_SIP不同
		double weight=dist;
		Segment line=new Segment(begin,end,dist,distmax,sourdata,pmax,weight);
		lineList.add(line);
		
	}
    //计算点到直线的距离
	public double dist(double x1, double y1, double x2, double y2, double x0, double y0) {
		return Math.abs((x0 - x1) * (y2 - y1) / (x2 - x1) + y1 - y0);
	}
	
	// 按阈值返回索引
		public int[] getSIPIndexByThreshold(double threshold) {
			int[] IDP = choosePointSIPByThreshold(threshold);
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

		// 按个数返回索引
		public int[] getSIPIndexByNumber(int number) {
			int[] IDP = choosePointSIPByNumber(number);
			List<Integer> list = new ArrayList<Integer>();
			int loc = 0;
			for (int i = 0; i < IDP.length; i++) {
				if (IDP[i] == 1) {
					loc++;
					list.add(i);
				}
			}
			int[] IDPindex = new int[loc];
			for (int i = 0; i < list.size(); i++) {
				IDPindex[i] = list.get(i);
			}
			return IDPindex;
		}
		// 计算PLR-SIP拟合误差
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
						sum_Err += dist(begin, data[begin], end, data[end], j,
								data[j]);
						sum_Err = Double.parseDouble(df.format(sum_Err));
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
}
