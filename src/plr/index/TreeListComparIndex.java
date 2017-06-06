package plr.index;

import java.util.Comparator;




public class TreeListComparIndex implements Comparator<IndexTreeNode> {
	public final int compare(IndexTreeNode pFirst, IndexTreeNode pSecond) {
		int a = pFirst.getIndex();
		int b = pSecond.getIndex();

		//ascending order
		if (a > b)
			return 1;
		if (a < b)
			return -1;
		else
			return 0;
	}

}
