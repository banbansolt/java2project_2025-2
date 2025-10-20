package mvc_jdbc_test.view;

import mvc_jdbc_test.entity.Order;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrdersView {
    public static void printHead(){
        System.out.println("================== 주문 목록 화면 ==================");
        System.out.println("고객명\t 고객아이디\t 제품명\t 수량\t 주문일자\t 배송지");
        System.out.println();
    }

    public static void printOrders(Order order){
        Date shippingDate = order.getShippingDate();
//        String orderDate = (shippingDate.getYear()+1900)+"-"+(shippingDate.getMonth()+1)+"-"+shippingDate.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String orderDate = dateFormat.format(shippingDate);
        System.out.printf("%s\t%s\t\t%s\t\t%d\t\t%s\t%s\n", order.getCustomername(), order.getCustomerid(), order.getProductname(), order.getQuantity(), orderDate, order.getShippingAddress());
    }
}