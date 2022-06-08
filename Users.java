import java.util.LinkedList;

class orders
{
    int noAuditorium= 0;
    int noAdult= 0;
    int noChild= 0;
    int noSenior= 0;
    int noTotal= 0;
    LinkedList<String> seats= new LinkedList<>();
}

public class Users
{
    private String password= "";
    private LinkedList<orders> orderList= new LinkedList<>();

    // constructors
    public Users(){}
    public Users(String passwd){password= passwd;}

    // setters
    public void setPassword(String password){this.password= password;}
    public void setOrderList(LinkedList<orders> orderList){this.orderList= orderList;}

    // getters
    public String getPassword(){return password;}
    public LinkedList<orders> getOrderList(){return orderList;}

    // add an order to the order list
    public void addToList(orders order){this.orderList.add(order);}
}
