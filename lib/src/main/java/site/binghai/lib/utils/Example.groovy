package site.binghai.lib.utils

class Example {

    Map invoke(Map context){
        if(context.batchName == '本科' || context.batchName == '专科'){
            result.sw = context.score;
        }else{
            def lk = Integer.valueOf(context.art_score);
            def gk = Integer.valueOf(context.score);
            if(context.batchName.contains("美术")){
                result.sw = lk*750/300*0.7+gk*0.3;
            }else{
                result.sw = lk*750/300*0.3+gk*0.7;
            }
        }
    }
}
