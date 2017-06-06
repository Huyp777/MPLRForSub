package plr.segments;

public class SegmentForBU {
	private int begin;
	private int end;
	private double mergeCost; // the merge cost for two adjacent segments
	private int isFirst;
	private int indexloc;
	public SegmentForBU() {
		super();
		this.mergeCost = Double.MAX_VALUE;
		this.isFirst = 0;
		this.indexloc = 0;
	}
	public SegmentForBU(int begin, int end, double mergecost) {
		super();
		this.begin = begin;
		this.end = end;
		this.indexloc = 0;
		this.mergeCost = mergecost;
	}
	public SegmentForBU(int begin, int end) {
		super();
		this.begin = begin;
		this.end = end;
		this.mergeCost = 0;
		this.isFirst = 1;
		this.indexloc = 0;
	}
	public SegmentForBU(int begin, int end,int spit) {
		super();
		this.begin = begin;
		this.end = end;
		this.mergeCost = 0;
		this.isFirst = spit;
		this.indexloc = 0;
	}
	public int getIndexloc() {
		return indexloc;
	}
	public void setIndexloc(int indexloc) {
		this.indexloc = indexloc;
	}
	public int getBegin() {
		return begin;
	}
	public void setBegin(int begin) {
		this.begin = begin;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public double getMegerCost() {
		return mergeCost;
	}
	public void setMegerCost(double mergecost) {
		this.mergeCost = mergecost;
	}
	
	public int getIsFirst() {
		return isFirst;
	}
	public void setIsFirst(int first) {
		this.isFirst = first;
	}
	
	
}
