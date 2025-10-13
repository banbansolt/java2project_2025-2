package mvc_jdbc_test.entity;

public class Customer {
    private String customerid;
    private String customername;
    private int age;
    private String level;
    private String job;
    private int reward;//적립금

    public String getCustomerid() {
        return customerid;
    }
    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }
    public String getCustomername() {
        return customername;
    }
    public void setCustomername(String customername) {
        this.customername = customername;
    }
    public int getAge() {
        return age;
    }

}
