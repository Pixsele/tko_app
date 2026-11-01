package tko.utils;

public class KcalHelper {

    public static Integer percentOfTarget(Integer target,Integer kcal){
        Double targetPer = target/100.0;
        double kcalPer = kcal/targetPer;

        int result = (int) Math.round(kcalPer);

        if(result > 100){
            result = 100;
        }
        if(result < 0){
            result = 0;
        }
        return result;
    }

}
