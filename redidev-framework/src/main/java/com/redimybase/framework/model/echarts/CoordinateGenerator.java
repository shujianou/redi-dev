package com.redimybase.framework.model.echarts;

import java.util.Arrays;
import java.util.Random;

/**
 * 坐标生成器
 * Created by Irany 2018/8/16 16:08
 */
public class CoordinateGenerator {

    public static void main(String[] args) {

    }

    /**
     * *GaussianRNG 是一个产生正态分布N（0,1）的随机数的类。
     * 其next方法返一个double类型的符合正态分布的随机数
     */
    public static class GaussianRNG{
        static int iset;
        static double gset;
        static Random r1, r2;

        public GaussianRNG(){
            r1 = new Random();
            r2 = new Random();
            iset = 0;
        }

        public static double next(){
            double fac, rsq, v1, v2;
            if (iset == 0) {
                do {
                    v1 = 2.0 * r1.nextDouble() - 1.0;
                    v2 = 2.0 * r2.nextDouble() - 1.0;
                    rsq = v1*v1 + v2*v2;
                } while (rsq >= 1.0 || rsq == 0.0);
                fac = Math.sqrt(-2.0*Math.log(rsq)/rsq);
                gset = v1*fac;
                iset = 1;
                return v2*fac;
            } else {
                iset = 0;
                return gset;
            }
        }
        public static void main(String[] args){
            //下面产生1000个正态分布的随机数:
            double[] doubles=new double[1000];
            for(int i=0;i<1000;i++){
                doubles[i]=next();
            }
            Arrays.sort(doubles);
            //排序之后输出:
            System.out.println(Arrays.toString(doubles));

        }

    }
}
