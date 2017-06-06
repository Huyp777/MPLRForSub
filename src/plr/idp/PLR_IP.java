package plr.idp;

import java.util.ArrayList;
import java.util.List;

import plr.segments.Segment;

public class PLR_IP {
	private int[] point;
	private double data[];
	private double proportion;
	
	public PLR_IP( double[] data) {
		super();
		int length = data.length;
		this.data = new double[length];
		point = new int[length];
		this.proportion = 0.00000000000000001;
		for (int i = 0; i < length; i++) {
			point[i] = 0;// 默认未选取
			this.data[i] = data[i];
		}
	}
	

	public double getProportion() {
		return proportion;
	}


	public void setProportion(double proportion) {
		this.proportion = proportion;
	}


	public int[] choosePointIP() {
		int[] point = new int[data.length];
		for (int i = 0; i < data.length; i++) {
			point[i] = 0;
		}
		double[] eui = new double[data.length];
		point[0] = 1;
		point[data.length - 1] = 1;

		for (int i = 1; i < data.length - 1; i++) {
			if (data[i] >= data[i + 1] && data[i] > data[i - 1]) {
				point[i] = 1;
			}
			if (data[i] <= data[i + 1] && data[i] < data[i - 1]) {
				point[i] = 1;
			}
		}

		int begin = 0;
		int mid = findNextPoint(point, begin);
		int end = findNextPoint(point, mid);
		while (mid < data.length - 1) {
			double a = data[begin];
			double b = data[mid];
			double c = data[end];
			if (data[mid] > data[begin]) {
				// 都是正数
				// if (data[mid] / data[begin] >= yuzhi && data[mid] / data[end]
				// >= yuzhi){
				// 都是负数
				if (data[end] / data[mid] >= proportion && data[begin] / data[mid] >= proportion) {
					begin = mid;
					mid = end;
					end = findNextPoint(point, mid);
				} else {
					point[mid] = 0;
					mid = end;
					end = findNextPoint(point, mid);
				}
			} else {
				// 都是正数
				// if (data[begin] / data[mid] >= yuzhi && data[begin] /
				// data[mid] >= yuzhi)
				// 都是负数
				if (data[mid] / data[begin] >= proportion && data[mid] / data[end] >= proportion) {
					begin = mid;
					mid = end;
					end = findNextPoint(point, mid);
				} else {
					point[mid] = 0;
					mid = end;
					end = findNextPoint(point, mid);
				}
			}

		}
		return point;

	}

	public double dist(double x1, double y1, double x2, double y2, double x0, double y0) {

		return Math.abs((x0 - x1) * (y2 - y1) / (x2 - x1) + y1 - y0);
	}

	public int findNextPoint(int[] point, int end) {
		for (int k = end + 1; k < point.length; k++) {
			if (1 == point[k]) {
				end = k;
				return end;
			}
		}
		return end;
	}

	public int[] choosePointIPWithNumber(int k) {
		double ratio = 0.00000000000000001;
		int number = 0;
		List<Integer> list;
		do {
			int[] IDP = choosePointIP();
			list = new ArrayList<Integer>();
			for (int i = 0; i < IDP.length; i++) {
				if (IDP[i] == 1) {
					number++;
					list.add(i);
				}
			}
			ratio*=2;
		} while (number < k);
		int[] IDPindex = new int[number];
		for (int i = 0; i < list.size(); i++) {
			IDPindex[i] = list.get(i);
		}
		return IDPindex;
	}

}
