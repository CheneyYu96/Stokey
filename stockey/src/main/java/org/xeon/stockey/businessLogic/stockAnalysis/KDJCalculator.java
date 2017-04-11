package org.xeon.stockey.businessLogic.stockAnalysis;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.xeon.stockey.vo.DailyDataVO;
import org.xeon.stockey.vo.KdjVO;

public class KDJCalculator {
	public Collection<KdjVO> calculateKDJ(Collection<DailyDataVO> src){
		LinkedList<KdjVO> result = new LinkedList<KdjVO>();
		Iterator<DailyDataVO> it = src.iterator();
		result.add(getFirstKdj(it.next()));
		while(it.hasNext()){
			DailyDataVO vo = it.next();
			KdjVO lastKdj = result.peekLast();
			double K = (2.0/3)*lastKdj.kValue+(1.0/3)*getRSV(vo);
			double D = (2.0/3)*lastKdj.dValue+(1.0/3)*K;
			double J = 3.0*K - 2.0*D;
			KdjVO tmp = new KdjVO(K,D,J,vo.getDate());
			result.add(tmp);
		}
		return result;
	}
	private double getRSV(DailyDataVO src){
		double C = src.getClose();
		double H = src.getHigh();
		double L = src.getLow();
		double result = ((C-L)/(H-L))*100;
		if(!Double.isFinite(result))
			result = 50;
		return result;
	}
	private KdjVO getFirstKdj(DailyDataVO vo){
		double K = 2.0/3*50+1.0/3*getRSV(vo);
		double D = 2.0/3*50+1.0/3*K;
		double J = 3.0*K - 2.0*D;
		return new KdjVO(K,D,J,vo.getDate());
	}
}
