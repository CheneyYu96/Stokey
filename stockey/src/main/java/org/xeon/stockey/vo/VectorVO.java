package org.xeon.stockey.vo;

import org.thymeleaf.expression.Lists;
import org.thymeleaf.expression.Maps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yuminchen on 16/6/17.
 */
public class VectorVO {

    List<Double> values;

    public VectorVO(double ... doubleValues) {
        values = new ArrayList<>();
        for(int i = 0; i<doubleValues.length; i++) {
            values.add(doubleValues[i]);
        }
    }

    public VectorVO(List<Double> values){

        this.values = values.stream()
                .limit(5)
                .collect(Collectors.toList());

    }

    public double calculateDistance(VectorVO other,CalculateType type){

        if(type==CalculateType.COSINE){
            double thisValue = Math.sqrt(this.values
                    .stream()
                    .map(value->value*value)
                    .reduce(0.0, Double::sum));
            double otherValue = Math.sqrt(other.values
                    .stream()
                    .map(value->value*value)
                    .reduce(0.0,Double::sum));

            double sum = 0;
            Iterator<Double> iterator1 = this.values.iterator();
            Iterator<Double> iterator2 = other.values.iterator();
            while (iterator1.hasNext()&&iterator2.hasNext()){
                sum+=iterator1.next()*iterator2.next();
            }

            return sum/(thisValue*otherValue);
        }

        else if(type==CalculateType.EUCLID){
            double sum = 0;
            Iterator<Double> iterator1 = this.values.iterator();
            Iterator<Double> iterator2 = other.values.iterator();
            while (iterator1.hasNext()&&iterator2.hasNext()){
                sum+=Math.pow(iterator1.next()-iterator2.next(),2);
            }

            return Math.sqrt(sum);

        }
        return Double.MAX_VALUE;
    }

    public enum CalculateType{
        COSINE,
        EUCLID
    }

    @Override
    public String toString(){
        return values
                .stream()
                .map(value->value+"")
                .collect(Collectors.joining(", "));
    }

    public List<Double> getValues() {
        return values;
    }

}
