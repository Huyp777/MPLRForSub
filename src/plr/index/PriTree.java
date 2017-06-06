package plr.index;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import plr.segments.Segment;

public class PriTree {
	private IndexTreeNode root;
	private int length;
	
	

	public PriTree() {
		this.root = null;
		this.length = 0;
	}

	public PriTree(IndexTreeNode root) {
		this.root = root;
		this.length = 1;
	}
	public IndexTreeNode getRoot() {
		return root;
	}

	public void setRoot(IndexTreeNode root) {
		this.root = root;
	}
	
	
	

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void initIndexTree(Segment init, double start, double last, int length) {
		root = new IndexTreeNode(0, start, 1, Double.MAX_VALUE, init.getDist());
		IndexTreeNode node = new IndexTreeNode(length, last, 2, init.getDist(),Double.MAX_VALUE);
		root.setRightChild(node);
		this.length++;
	}
	
	public void createIndexTree(Segment line, Segment first, Segment next,
			int rank) {
		int index = line.getPmax();
		double value = line.getSourData();
		double leftdist = first.getDist();
		double rightdist = next.getDist();
		IndexTreeNode newIndexNode = new IndexTreeNode(index, value, rank, leftdist, rightdist);
		insertChild(newIndexNode);
	}
	
	public void insertChild(IndexTreeNode child){
		IndexTreeNode nowNode = root;
		int index = nowNode.getIndex();
		int childIndex = child.getIndex();
		boolean inserted=true;
		do{
			if(childIndex < index){
				if(null==nowNode.getLeftChild()){
					nowNode.setLeftChild(child);
					length++;
					inserted=false;
				}else{
					nowNode = nowNode.getLeftChild();
					index = nowNode.getIndex();
				}
			}else{
				if(null == nowNode.getRightChild()){
					nowNode.setRightChild(child);
					length++;
					inserted = false;
				}else{
					nowNode = nowNode.getRightChild();
					index = nowNode.getIndex();
				}
			}
			
		}while(inserted);
	}
	
	
	// MPLR  for error_S
	
	public List<IndexTreeNode> errorS(int curRank) {
		List<IndexTreeNode> treeNodeList = new ArrayList<IndexTreeNode>(
				curRank - 1);
		traverseForErrorS(root, treeNodeList, curRank);
		treeNodeList.sort(new TreeListComparIndex());
		
		return treeNodeList;
	}
	
	public void traverseForErrorS(IndexTreeNode node,List<IndexTreeNode> list,int curRank) { 		
        if (node == null)  
            return;
        else{
        	if(node.getRank() <= curRank ){
        		list.add(node);
        	}
        	traverseForErrorS(node.getLeftChild(),list,curRank); 
        	traverseForErrorS(node.getRightChild(),list,curRank);  
        }
    }
	
	
	
	public List<IndexTreeNode> inOrderTraverseAll(){
		List<IndexTreeNode> list=new ArrayList<IndexTreeNode>();
		inOrderTraverseAll(root,list);
		return list;
		
	}
	
	
	public void inOrderTraverseAll(IndexTreeNode node,List<IndexTreeNode> list) { 		
        if (node == null)  
            return;  
        list.add(node) ;
        inOrderTraverseAll(node.getLeftChild(),list); 
        /*double[] point=new double[2];
        point[0]=node.getIndex();
        point[1]=node.getData();*/
  //      System.out.println(point[0]+"\t"+point[1]);
        inOrderTraverseAll(node.getRightChild(),list);  
    }  
	

	

	/*public List<PriListNode> searchRangeIDP(int strat, int end,
			PriListNode beginpoint, List<PriListNode> resultList) {
		PriListNode beginLeftChild = beginpoint.getLeftChild();
		PriListNode beginRightChild = beginpoint.getRightChild();
		int begin = beginpoint.getIndex();
		if (begin < strat) {
			if (beginRightChild != null) {
				searchRangeIDP(strat, end, beginRightChild, resultList);
			}
		} else if (begin > end) {
			if (beginLeftChild != null) {
				searchRangeIDP(strat, end, beginLeftChild, resultList);
			}
		} else {
			if ((begin != strat) && (begin != end)) {
				resultList.add(beginpoint);
			}
			if (beginLeftChild != null) {
				searchRangeIDP(strat, end, beginLeftChild, resultList);
			}
			if (beginRightChild != null) {
				searchRangeIDP(strat, end, beginRightChild, resultList);
			}
		}

		return resultList;

	}*/



	

}
