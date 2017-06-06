package plr.index;

import java.util.Comparator;




public class PriListComparErrorG implements Comparator<PriListNode> {
	public final int compare(PriListNode pFirst, PriListNode pSecond) {
		double a = pFirst.getCurFittingErr();
		double b = pSecond.getCurFittingErr();

		//descending order
		if (a > b)
			return -1;
		if (a < b)
			return 1;
		else
			return 0;
	}

}
