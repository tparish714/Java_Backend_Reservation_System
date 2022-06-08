public class Node <G>
{
    private Node<G> next;
    private Node<G> down;
    private G payload;

    // constructors
    public Node(){}
    public Node(G p){payload= p;}

    // setters
    public void setNext(Node<G> next) {this.next = next;}
    public void setDown(Node<G> down) {this.down = down;}
    public void setPayload(G payload) {this.payload = payload;}

    // getters
    public Node<G> getNext() {return next;}
    public Node<G> getDown() {return down;}
    public G getPayload() {return payload;}
}

/*
public class Node
{
    private Node next, down;
    private Seat payload;

    // constructors
    public Node(){}
    public Node(Seat s){payload= s;}

    // setters
    public void setNext(Node Next) {next = Next;}
    public void setDown(Node Down) {down = Down;}
    public void setPayload(Seat Payload) {payload = Payload;}

    // getters
    public Node getNext() {return next;}
    public Node getDown() {return down;}
    public Seat getPayload() {return payload;}
}
 */
