public class Seat
{
    private int row= 0;
    private char seat;
    private char type;

    // constructors
    public Seat(){}
    public Seat(int r, char s, char t)
    {
        row= r;
        seat= s;
        type= t;
    }

    // setters
    public void setRow(int row) {this.row = row;}
    public void setSeat(char seat) {this.seat = seat;}
    public void setType(char type) {this.type = type;}

    // getters
    public int getRow() {return row;}
    public char getSeat() {return seat;}
    public char getType() {return type;}
}