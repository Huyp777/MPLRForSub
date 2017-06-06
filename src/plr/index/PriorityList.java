package plr.index;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import plr.segments.Segment;

public class PriorityList {
	private PriListNode first;
	private PriListNode end;
	public List<PriListNode> priList;
	private List<PriListNode> indexBackupList;
	public List<Integer> priListOrder;

	public PriorityList() {
		this.first = null;
		this.end = null;
		this.priList = new ArrayList<PriListNode>();
		this.priListOrder = new ArrayList<Integer>();
	}


	public void initPriList(Segment init, double start, double last, int length) {
		priListOrder.clear();
		priList.clear();
		priListOrder.add(0);
		priListOrder.add(length);
		first = new PriListNode(0, start, 1, Double.MAX_VALUE,
				init.getDist(), Double.MAX_VALUE, null, null);
		end = new PriListNode(length, last, 2, init.getDist(),
				Double.MAX_VALUE, init.getDist(), first, null);
		first.setRightNodeInSelect(end);
		priList.add(first);
		priList.add(end);
	}

	public List<PriListNode> getIndexBackupList() {
		return indexBackupList;
	}

	public void setIndexBackupList(List<PriListNode> indexBackupList) {
		this.indexBackupList = new ArrayList<PriListNode>(indexBackupList);
	}

	public List<PriListNode> getIndexList() {
		return priList;
	}

	public void setIndexList(List<PriListNode> indexList) {
		this.priList = indexList;
	}

	public List<Integer> getIndexOrder() {
		return priListOrder;
	}

	public void setIndexOrder(List<Integer> indexOrder) {
		this.priListOrder = indexOrder;
	}
	
	// MPLR based on FER for error_G
	
		public PriListNode errorGbyFER(double fe_ratio) {
			List<PriListNode> reorder = new ArrayList<PriListNode>(priList.size());
			Iterator<PriListNode> iter = priList.iterator();
			while (iter.hasNext()) {
				 PriListNode node = iter.next();
				 reorder.add(node);
			}
			reorder.sort(new PriListComparErrorG());
			PriListNode maxNode = reorder.get(1);
			double curerr = maxNode.getCurFittingErr()*fe_ratio;
			PriListNode curNode = findNearestIndex(reorder,curerr);
			return curNode;
		}
		
	// MPLR based on DCR for error_G

	public PriListNode errorGbyDCR(double num_ratio) {
		List<PriListNode> reorder = new ArrayList<PriListNode>(priList.size());
		Iterator<PriListNode> iter = priList.iterator();
		while (iter.hasNext()) {
			PriListNode node = iter.next();
			reorder.add(node);
		}
		int curnum = (int) (reorder.size()*num_ratio);
		if (curnum <= reorder.size()) {
			PriListNode curNode = reorder.get(curnum - 1);
			return curNode;
		}
		else{
			return null;
		}
	}
		
		
		
	public PriListNode findNearestIndex(List<PriListNode> reorder, double curerr) {
		int beg = 0;
		int end = reorder.size() - 1;
		int mid = (beg + end) / 2;
		while (beg < end) {
			if (reorder.get(mid).getCurFittingErr() == curerr) {
				System.out.println("find the accurate error_g："
						+ reorder.get(mid).getCurFittingErr());
				return reorder.get(mid);
			} else if (reorder.get(mid).getCurFittingErr() > curerr) {
				beg = mid + 1;
			} else {
				end = mid;
			}
			mid = (beg + end) / 2;
		}
		if (reorder.get(mid).getCurFittingErr() < curerr) {
			System.out.println("find the nearest error_g："
					+ reorder.get(mid).getCurFittingErr()
					+ " compared with the specified error_g (Maxerror_g*FER): " + curerr);
			return reorder.get(mid);

		} else {
			return null;
		}

	}
	
	
	
	
	
	
	// create priority list
	public void createPriList(Segment source, Segment first, Segment next,int rank) {
		int segBegin;
		int segEnd;
		int selectIDP;
		double leftSumDist;
		double rightSumDist;
		double leftDist;
		double rightDist;
		double currSumDist;
		PriListNode newListNode;
		PriListNode leftNode;
		PriListNode rightNode;
		segBegin = source.getBegin();
		segEnd = source.getEnd();
		selectIDP = source.getPmax();

		leftDist = first.getDist();
		rightDist = next.getDist();
		leftNode = priList.get(priListOrder.indexOf(segBegin));
		rightNode = priList.get(priListOrder.indexOf(segEnd));
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00000");
		// 计算左侧的拟合误差
		leftSumDist = calcLeftFittingErr(leftNode);
		// 计算右侧的拟合误差
		rightSumDist = calcRightFittingErr(rightNode);
		// 计算加入本点的全局拟合误差
		currSumDist = leftSumDist + leftDist + rightSumDist + rightDist;
		currSumDist = Double.parseDouble(df.format(currSumDist));
		// 新增IDP
		newListNode = new PriListNode(selectIDP, source.getSourData(), rank, leftDist,
				rightDist, currSumDist);
		updateRelation(newListNode, leftNode, rightNode);
		priList.add(newListNode);
		priListOrder.add(newListNode.getIndex());

	}
	

	public Double calcLeftFittingErr(PriListNode node) {
		double sumFE = 0.0f;
		if (node.getIndex() == first.getIndex()) {
			return sumFE;
		} else {
			sumFE = node.getLeftDist();
			node = node.getLeftNodeInSelect();
			while ((node != null) && (node.getIndex() != first.getIndex())) {
				sumFE += node.getLeftDist();
				node = node.getLeftNodeInSelect();
			}
			return sumFE;
		}

	}

	public Double calcRightFittingErr(PriListNode node) {
		double sumFE = 0.0f;
		if (node.getIndex() == end.getIndex()) {
			return sumFE;
		} else {
			sumFE = node.getRightDist();
			node = node.getRightNodeInSelect();
			while ((node != null) && (node.getIndex() != end.getIndex())) {
				sumFE += node.getRightDist();
				node = node.getRightNodeInSelect();
			}
			return sumFE;
		}

	}

	public void updateRelation(PriListNode node, PriListNode left,
			PriListNode right) {

		left.setRightNodeInSelect(node);
		left.setRightDist(node.getLeftDist());
		right.setLeftNodeInSelect(node);
		right.setLeftDist(node.getRightDist());
		node.setLeftNodeInSelect(left);
		node.setRightNodeInSelect(right);

	}


	

}
