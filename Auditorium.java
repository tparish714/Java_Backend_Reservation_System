import java.util.Scanner;

public class Auditorium
{
    private Node<Seat> first;

    // constructors
    public Auditorium(){}
    public Auditorium(Scanner fileIN)
    {
        // count row
        int cntRow=0;
        Node<Seat> row= null;
        Node<Seat> cur = null;

        // read in line by line till end of the file
        while (fileIN.hasNextLine())
        {
            // increment row
            cntRow++;
            // read in one line at a time
            String line= fileIN.nextLine();

            // first row; first points to null
            if (cntRow== 1)
            {
                // have first point to a node
                first= new Node( new Seat(cntRow, (char)(65+0), line.charAt(0)));
                row= cur= first;

                // creat a node for every character read in
                for (int i=1; i<line.length(); i++)
                {
                    cur.setNext( new Node( new Seat(cntRow,  (char)(65+i), line.charAt(i))));
                    cur= cur.getNext();
                }
            }

            // rest of the auditoriun
            else
            {
                for (int i=0; i< line.length(); i++)
                {
                    Node<Seat> n= new Node( new Seat(cntRow, (char)(65+i), line.charAt(i)));
                    // first node on a new row
                    if (i==0)
                    {
                        row.setDown(n);
                        row= cur= n;
                    }

                    else
                    {
                        cur.setNext(n);
                        cur= cur.getNext();
                    }
                }
            }
        }
        // close the file
        fileIN.close();
    }

    // setter
    public void setFirst(Node<Seat> first) {this.first = first;}
    // getter
    public Node<Seat> getFirst() {return first;}

    // display auditorium
    public void display()
    {
        // use two pointers to go through the grid
        Node<Seat> row=first;
        Node<Seat> cur= first;
        int rowNo=0;

        // start from the first row till the last
        while (row!= null)
        {
            // display row number
            rowNo++;
            System.out.print(rowNo+ " ");
            // start from the first seat till the last
            while (cur!= null)
            {
                // print seat info
                // if type is '.' means empty
                // else display a '#' showing the seat is reserved
                System.out.print(cur.getPayload().getType()== '.'?'.':'#');
                // move cur
                cur= cur.getNext();
            }
            System.out.println();
            // move the row pointer down to next row
            row= row.getDown();
            // move the cur pointer doen to next row
            cur= row;
        }
    }
}
