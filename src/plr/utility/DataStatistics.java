package plr.utility;

import java.util.ArrayList;
import java.util.List;

public class DataStatistics {
	/**
	 * ����������
	 * @param data ����
	 * @param IDP  ��Ҫ��
	 * @return {ȫ�����ֶ����������������}
	 */
	public double[] staticFittingError(double[] data, List<double[]> IDP) {
		double[] fittingerror = { 0.0, 0.0, 0.0 };
		if (null == IDP ||IDP.size() < 2) {
			return fittingerror;
		}
		double[] fittingerrorSegments={0,0};
		for (int i = 0; i < IDP.size() - 1; i++) {
			fittingerrorSegments=staticFittingErrorForSegments(data,(int)IDP.get(i)[0],(int)IDP.get(i+1)[0]);
			fittingerror[0]+=fittingerrorSegments[0];
			if(fittingerrorSegments[0]>fittingerror[1]){
				fittingerror[1]=fittingerrorSegments[0];
			}
			if(fittingerrorSegments[1]>fittingerror[2]){
				fittingerror[2]=fittingerrorSegments[1];
			}
			
		}

		return fittingerror;
	}

	public double[] staticFittingError(double[] data, int[] IDPArray) {
		double[] fittingerror = { 0.0, 0.0, 0.0 };
		if(null==IDPArray||IDPArray.length<2){
			return fittingerror;
		}
		List<double[]> IDPList=new ArrayList<double[]>();
		for(int i=0;i<IDPArray.length;i++){
			double[] point={IDPArray[i],data[IDPArray[i]]};
			IDPList.add(point);
		}
		
		

		return staticFittingError(data,IDPList);
	}
	
	/**
	 * �߶�������
	 * @param data ����
	 * @param IDP1  �������
	 * @param IDP2  �յ�����
	 * @return {�߶���������������}
	 */
	public double[] staticFittingErrorForSegments(double[] data, int IDP1, int IDP2) {

		double[] fittingerrorSegments = { 0.0, 0.0 };
		if (IDP1 >= IDP2) {
			return fittingerrorSegments;
		}
		double dist=0.0;
		for(int i=IDP1;i<=IDP2;i++){
			 dist=dist(IDP1,data[IDP1],IDP2,data[IDP2],i,data[i]);
			 fittingerrorSegments[0]+=dist;
			 if(dist>fittingerrorSegments[1]){
				 fittingerrorSegments[1]=dist;
			 }
		}

		return fittingerrorSegments;
	}

	// ����㵽ֱ�ߵľ���
	public static double dist(double x1, double y1, double x2, double y2, double x0, double y0) {
		return Math.abs((x0 - x1) * (y2 - y1) / (x2 - x1) + y1 - y0);
	}

}
