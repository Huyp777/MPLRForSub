package plr.index;

public class IndexTreeNode {
	private int index;
	private double value;
	private int rank;
	private double error_L;
	private double error_R;
	private IndexTreeNode child_L;
	private IndexTreeNode child_R;
	public IndexTreeNode() {
		super();
	}
	public IndexTreeNode(int index, double value, int rank, double leftDist, double rightDist) {
		super();
		this.index = index;
		this.value = value;
		this.rank = rank;
		this.error_L = leftDist;
		this.error_R = rightDist;
		this.child_L = null;
		this.child_R = null;
	}
	
	public IndexTreeNode getLeftChild() {
		return child_L;
	}
	public void setLeftChild(IndexTreeNode leftChildInTree) {
		this.child_L = leftChildInTree;
	}
	public IndexTreeNode getRightChild() {
		return child_R;
	}
	public void setRightChild(IndexTreeNode rightChildInTree) {
		this.child_R = rightChildInTree;
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
	public void setRank(int sequenceNumber) {
		this.rank = sequenceNumber;
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
	
}
