package w0827;

import java.util.Calendar;
import java.util.Random;

public class chapter09_LAB1 {
    public static void main(String[] args) {
        String[] wiseSay ={
                "용기는 두려움을 잃어버린 것이 아니라 그것을 극복하는 것이다.",
                "오늘 할 수 있는 일을 내일로 미루지 마라." ,
                "삶이 그대를 속일지라도, 슬퍼하거나 노여워하지 말라!" ,
                "성공의 비결은 목표의 지속성에 있다." ,
                "꿈을 계속 간직하고 있으면 반드시 실현할 때가 온다.",
                "시작이 반이다." ,
                "행복은 내면에서 발견된다.",
                "어제를 버리지 않으면 내일은 오지 않는다." ,
                "네 자신을 알라."
        };
        Random rand = new Random();

        int todayIndex = rand.nextInt(wiseSay.length);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        System.out.printf("%d년 %d월 %d일 오늘의 명언 : %s \n ", year, month, day, wiseSay[todayIndex]);
    }
}
