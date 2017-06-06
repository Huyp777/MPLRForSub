package plr.segments;

import java.util.Comparator;




public class SegmentComparatorIDP implements Comparator<Segment> {
	public final int compare(Segment pFirst, Segment pSecond) {
		double a = ((Segment) pFirst).getWeight();
		double b = ((Segment) pSecond).getWeight();

		//½µÐòÅÅÁÐ
		if (a>b)
			return -1;
		if (a< b)
			return 1;
		else
			return 0;
	}

}
