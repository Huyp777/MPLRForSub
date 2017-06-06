package plr.segments;

public class Segment {
	private int p_b;
	private int p_e;
	private int p_max;
	private double dist;
	private double dist_max;
	private double value;
	private double s_weight;
	public Segment() {
		super();
		this.dist = 0;
		this.dist_max = 0;
		this.s_weight=0;
	}
	public Segment(int begin, int end, double dist, double distMax,double sourData, int pmax,double weight) {
		super();
		this.p_b = begin;
		this.p_e = end;
		this.dist = dist;
		this.dist_max = distMax;
		this.value = sourData;
		this.p_max = pmax;
		this.s_weight=weight;
		
		
	}
	
	public double getSourData() {
		return value;
	}
	public void setSourData(double sourData) {
		this.value = sourData;
	}
	public int getBegin() {
		return p_b;
	}
	public void setBegin(int begin) {
		this.p_b = begin;
	}
	public int getEnd() {
		return p_e;
	}
	public void setEnd(int end) {
		this.p_e = end;
	}
	public double getDist() {
		return dist;
	}
	public void setDist(double dist) {
		this.dist = dist;
	}
	public double getDistMax() {
		return dist_max;
	}
	public void setDistMax(double distMax) {
		this.dist_max = distMax;
	}
	public int getPmax() {
		return p_max;
	}
	public void setPmax(int pmax) {
		this.p_max = pmax;
	}
	public double getWeight() {
		return s_weight;
	}
	public void setWeight(double weight) {
		this.s_weight = weight;
	}
	
}
