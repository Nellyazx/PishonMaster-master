package alien.com.model;

public class OrderInfo
{
    private String ordercustname;
    private String orderitem;
    private String orderquantity;
    private String orderprice;
    private String customerlocation;
    private String customerphonenumber;
    private String orderId;


    public OrderInfo(String ordercustname,String orderitem,String orderquantity,String orderprice,String customerlocation,String customerphonenumber,String orderId)
    {
        this.ordercustname = ordercustname;
        this.orderitem = orderitem;
        this.orderquantity = orderquantity;
        this.orderprice= orderprice;
        this.customerlocation= customerlocation;
        this.customerphonenumber= customerphonenumber;
        this.orderId= orderId;
    }
    public String getOrdercustname()
    {
        return ordercustname;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderitem() {
        return orderitem;
    }

    public String getOrderquantity() {
        return orderquantity;
    }

    public String getOrderprice() {
        return orderprice;
    }

    public String getCustomerlocation() {
        return customerlocation;
    }

    public String getCustomerphonenumber() {
        return customerphonenumber;
    }
}
