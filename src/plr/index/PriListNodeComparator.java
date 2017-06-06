package plr.index;

import java.util.Comparator;




public class PriListNodeComparator implements Comparator<PriListNode> {
	public final int compare(PriListNode pFirst, PriListNode pSecond) {
		int a = ((PriListNode) pFirst).getIndex();
		int b = ((PriListNode) pSecond).getIndex();

		//ÉýÐòÅÅÁÐ
		if (a>b)
			return 1;
		if (a< b)
			return -1;
		else
			return 0;
	}

}
