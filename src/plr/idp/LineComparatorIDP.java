package plr.idp;

import java.util.Comparator;




public class LineComparatorIDP implements Comparator<Line> {
	public final int compare(Line pFirst, Line pSecond) {
		double a = ((Line) pFirst).getWeight();
		double b = ((Line) pSecond).getWeight();

		//½µÐòÅÅÁÐ
		if (a>b)
			return -1;
		if (a< b)
			return 1;
		else
			return 0;
	}

}
