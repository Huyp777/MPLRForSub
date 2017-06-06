package plr.index;

public class PriListNode {
	private int index;
	private double value;
	private int rank;
	private double error_L;
	private double error_R;
	private double curError_G;
	private PriListNode adjacentNode_L;
	private PriListNode adjacentNode_R;
	public PriListNode() {
		super();
	}
	public PriListNode(int index, double value, int rank, double error_L, double error_R,double curerror_G) {
		super();
		this.index = index;
		this.value = value;
		this.rank = rank;
		this.error_L = error_L;
		this.error_R = error_R;
		this.curError_G = curerror_G;
		this.adjacentNode_L = null;
		this.adjacentNode_R = null;
	}
	public PriListNode(int index, double data, int sequenceNumber,
			double leftDist, double rightWeight, double curFittingErr,
			PriListNode leftNodeInSelect, PriListNode rightNodeInSelect) {
		super();
		this.index = index;
		this.value = data;
		this.rank = sequenceNumber;
		this.error_L = leftDist;
		this.error_R = rightWeight;
		this.curError_G = curFittingErr;
		this.adjacentNode_L = leftNodeInSelect;
		this.adjacentNode_R = rightNodeInSelect;
	}
	
	public double getCurFittingErr() {
		return curError_G;
	}
	public void setCurFittingErr(double curFittingErr) {
		this.curError_G = curFittingErr;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public double getData() {
		return value;
	}
	public void setData(double data) {
		this.value = data;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public double getLeftDist() {
		return error_L;
	}
	public void setLeftDist(double leftWeight) {
		this.error_L = leftWeight;
	}
	public double getRightDist() {
		return error_R;
	}
	public void setRightDist(double rightWeight) {
		this.error_R = rightWeight;
	}
	public PriListNode getLeftNodeInSelect() {
		return adjacentNode_L;
	}
	public void setLeftNodeInSelect(PriListNode leftChild) {
		this.adjacentNode_L = leftChild;
	}
	public PriListNode getRightNodeInSelect() {
		return adjacentNode_R;
	}
	public void setRightNodeInSelect(PriListNode rightChild) {
		this.adjacentNode_R = rightChild;
	}
	
}
