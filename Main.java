import java.io.*;
import java.util.*;

public class Main
{
    // global variables; ticket prices
    public static final double ADULT$ = 10;
    public static final double CHILD$ = 5;
    public static final double SENIOR$ = 7.50;

    public static void main(String[] args) throws IOException
    {
        Scanner in = new Scanner(System.in);
        // key= username
        Map<String, Users> accounts = new HashMap<>();

        // open the user data file
        File userData = new File("userdb.dat");
        if (userData.exists() && userData.canRead())
        {
            Scanner dataIn = new Scanner(userData);
            // feed the data to the hashmap
            while (dataIn.hasNext())
            {
                // read line by line
                String line = dataIn.nextLine();
                // split the string by space
                String[] info = line.split(" ");
                String username = info[0], password = info[1];
                // link the username to the password
                accounts.put(username, new Users(password));
            }
        }

        // set up 3 auditoriums
        Auditorium[] auditoriums= new Auditorium[3];
        File a1 = new File("A1.txt");
        File a2 = new File("A2.txt");
        File a3 = new File("A3.txt");
        int[] theater = new int[2];
        int[] row = new int[3];
        int[] col = new int[3];

        // for auditorium 1
        if (a1.exists() && a1.canRead()) {
            // scanner object for file io
            Scanner in1 = new Scanner(a1);
            auditoriums[0]= new Auditorium(in1);
            theater = row_col(auditoriums[0]);
            row[0] = theater[0];
            col[0] = theater[1];
            // close file
            in1.close();
        }
        // for auditorium 2
        if (a2.exists() && a2.canRead()) {
            // scanner object for file io
            Scanner in2 = new Scanner(a2);
            auditoriums[1]= new Auditorium(in2);
            theater = row_col(auditoriums[1]);
            row[1] = theater[0];
            col[1] = theater[1];
            // close file
            in2.close();
        }
        // for auditorium 3
        if (a3.exists() && a3.canRead()) {
            // scanner object for file io
            Scanner in3 = new Scanner(a3);
            auditoriums[2]= new Auditorium(in3);
            theater = row_col(auditoriums[2]);
            row[2] = theater[0];
            col[2] = theater[1];
            // close file
            in3.close();
        }

        // variables for loop control
        int userInput = 0, adminInput = 0;
        boolean invalid = true;
        String loginName = "", loginPswd = "";

        // once accounts/ admin log out, back to start point
        do {
            // prompt for username and password till log in successfully
            do {
                // start point: prompt for login info
                System.out.println("----Log In----");
                System.out.print("Username: ");
                loginName = in.nextLine();
                System.out.print("Password: ");
                loginPswd = in.nextLine();

                // invaild password
                if (accounts.get(loginName).getPassword().compareTo(loginPswd) != 0)
                {
                    // 2 chances to enter re-password before return to the start point
                    for (int i = 0; i < 2; i++)
                    {
                        System.out.println("Invalid password. Try again!");
                        System.out.print("Password: ");
                        loginPswd = in.nextLine();

                        if (accounts.get(loginName).getPassword().compareTo(loginPswd) == 0)
                        {
                            invalid = false;
                            break;
                        }
                    }
                    continue;
                }
                // valid password
                invalid = false;
            } while (invalid);

            // admin login
            if (loginName.compareTo("admin") == 0)
            {
                do
                {
                    // display menu
                    adminMenu();
                    // prompt for menu choice
                    do
                    {
                        System.out.print("Enter Choice: ");
                        try
                        {
                            // read in user's choice
                            adminInput = in.nextInt();
                            // value entered is integer but not valid options
                            if (adminInput <= 0 || adminInput > 3)
                            {
                                System.out.println("Invalid value. Try again!");
                                invalid = true;
                                continue;
                            }
                            invalid = false;
                        }
                        // wrong data type is entered
                        catch (InputMismatchException e)
                        {
                            invalid = true;
                            System.out.println("Invalid value. Try again!");
                            // throw away incorrect input
                            in.nextLine();
                        }
                    } while (invalid);

                    switch (adminInput)
                    {
                        case 1: // 1. Print Report

                            double[] sum= null;
                            int open=0, reserved=0, adult=0, child=0, senior=0;
                            double total=0.0;

                            System.out.println();
                            // for each auditorium
                            for (int i=0; i<3; i++)
                            {
                                System.out.print("Auditorium "+ (i+1));
                                sum= adminReport(auditoriums[i], row[i], col[i]);
                                open+=sum[0];
                                reserved+= sum[1];
                                adult+= sum[2];
                                child+= sum[3];
                                senior+= sum[4];
                                total+= sum[5];
                            }

                            // overall summary
                            System.out.println("------Overall Summary------");
                            System.out.println("Open seats: "+ open);
                            System.out.println("Reserved seats : "+ reserved);
                            System.out.println("Adult: "+ adult);
                            System.out.println("Child: "+ child);
                            System.out.println("Senior: "+ senior);
                            System.out.printf("Total sales: $%.2f\n", total);

                            break;

                        case 2: // 2. Logout
                            // flush
                            in.nextLine();
                            break;

                        case 3: // 3. Exit
                            // create 3 files and write the results into the files
                            File final1 = new File("A1Final.txt");
                            File final2 = new File("A2Final.txt");
                            File final3 = new File("A3Final.txt");
                            PrintWriter f1= new PrintWriter(final1);
                            PrintWriter f2= new PrintWriter(final2);
                            PrintWriter f3= new PrintWriter(final3);
                            exit(auditoriums[0], f1, row[0], col[0]);
                            exit(auditoriums[1], f2, row[1], col[1]);
                            exit(auditoriums[2], f3, row[2], col[2]);
                            System.exit(0);
                            break;
                    }

                }while (adminInput!=2);
            }// enf if

            // normal users login
            else
            {
                int sub= -1;
                do
                {
                    // display menu
                    userMainMenu();
                    // prompt for menu choice
                    do {
                        System.out.print("Enter Choice: ");
                        try {
                            // read in user's choice
                            userInput = in.nextInt();
                            // value entered is integer but not valid options
                            if (userInput <= 0 || userInput > 5) {
                                System.out.println("Invalid value. Try again!");
                                invalid = true;
                                continue;
                            }

                            invalid = false;
                        }
                        // wrong data type is entered
                        catch (InputMismatchException e) {
                            invalid = true;
                            System.out.println("Invalid value. Try again!");
                            // throw away incorrect input
                            in.nextLine();
                        }
                    } while (invalid);

                    // access user's order linkedlist
                    LinkedList<orders> orderList= accounts.get(loginName).getOrderList();

                    switch(userInput)
                    {
                        case 1:// reserve
                            do
                            {
                                // display submenuR
                                submenuR();
                                System.out.print("Enter Choice: ");
                                try
                                {
                                    // read in user's choice
                                    sub = in.nextInt();
                                    // value entered is integer but not valid options
                                    if (sub <= 0 || sub > 3)
                                    {
                                        System.out.println("Invalid value. Try again!");
                                        invalid = true;
                                        continue;
                                    }

                                    invalid = false;
                                }
                                // wrong data type is entered
                                catch (InputMismatchException e)
                                {
                                    invalid = true;
                                    System.out.println("Invalid value. Try again!");
                                    // throw away incorrect input
                                    in.nextLine();
                                }
                            } while (invalid);

                            orders o= null;
                            displayTheater(auditoriums[sub-1], col[sub-1]);
                            o= reserve(in, 1, auditoriums[sub-1], sub, row[sub-1], col[sub-1]);
                            if (o!= null) accounts.get(loginName).addToList(o);
                            break;

                        case 2: // view
                            view(orderList);
                            break;

                        case 3: // update
                            displayOrders(orderList);
                            int orderNo=0;

                            // no existing order
                            if (orderList.size()==0)
                                System.out.println("No orders");
                            else
                            {
                                // prompt for order number
                                do {
                                    System.out.print("Enter Order No: ");
                                    try {
                                        // read in user's choice
                                        orderNo = in.nextInt();
                                        // value entered is integer but not valid options
                                        if (orderNo <= 0 || orderNo > accounts.get(loginName).getOrderList().size()) 
                                        {
                                            System.out.println("Invalid value. Try again!");
                                            invalid = true;
                                            continue;
                                        }

                                        invalid = false;
                                    }
                                    // wrong data type is entered
                                    catch (InputMismatchException e) {
                                        invalid = true;
                                        System.out.println("Invalid value. Try again!");
                                        // throw away incorrect input
                                        in.nextLine();
                                    }
                                } while (invalid);

                                do
                                {
                                    // display submenu
                                    submenuU();
                                    System.out.print("Enter Choice: ");
                                    try
                                    {
                                        // read in user's choice
                                        sub = in.nextInt();
                                        // value entered is integer but not valid options
                                        if (sub <= 0 || sub > 3)
                                        {
                                            System.out.println("Invalid value. Try again!");
                                            invalid = true;
                                            continue;
                                        }

                                        invalid = false;
                                    }
                                    // wrong data type is entered
                                    catch (InputMismatchException e)
                                    {
                                        invalid = true;
                                        System.out.println("Invalid value. Try again!");
                                        // throw away incorrect input
                                        in.nextLine();
                                    }
                                } while (invalid);

                                // for readability
                                orders obj = orderList.get(orderNo - 1);

                                // get auditorium #
                                int noA = obj.noAuditorium;
                                // the add-on order
                                orders add = null;

                                // 3 submenu options
                                switch (sub) {
                                    case 1:// add
                                        displayTheater(auditoriums[noA-1], col[noA-1]);
                                        add= reserve(in, 3, auditoriums[noA-1], 1, row[noA-1], col[noA-1]);
                                        addToOrder(obj, add);
                                        break;

                                    case 2:// delete
                                        in.nextLine();
                                        // prompt for the seat wanted to delete
                                        System.out.print("\nEnter the row number: ");
                                        String row_del = in.nextLine();
                                        System.out.print("Enter the seat character: ");
                                        String seat_del = in.nextLine();
                                        String remove = row_del + seat_del;
                                        deleteSeat(auditoriums[noA-1], obj, remove);
                                        break;

                                    case 3:// cancel
                                        cancelOrder(auditoriums[noA-1], orderList, orderNo-1);
                                        break;
                                }
                            }
                            break;

                        case 4:// receipt
                            displayReceipt(orderList);
                            break;

                        case 5:// log out
                            // flush
                            in.nextLine();
                            break;
                    }
                }while(userInput!=5);
            }// end else

        } while(userInput==5 || adminInput==2);

    }// main end

    // get the #row and #seat
    public static int[] row_col (Auditorium a)
    {
        int[] rolNcol = new int[2];

        // get number of rows
        int cntRow = 0;
        Node<Seat> ptrRow = a.getFirst();
        while (ptrRow != null)
        {
            // increment counter
            cntRow++;
            // move row pointer
            ptrRow = ptrRow.getDown();
        }
        rolNcol[0] = cntRow;

        // get number of seats
        int cntSeat = 0;
        Node<Seat> ptrSeat = a.getFirst();
        while (ptrSeat != null)
        {
            // increment counter
            cntSeat++;
            // move seat pointer
            ptrSeat = ptrSeat.getNext();
        }
        rolNcol[1] = cntSeat;

        return rolNcol;
    }

    // display admin menu
    public static void adminMenu ()
    {
        System.out.println("\nAdmin Menu: ");
        System.out.println("1. Print Report");
        System.out.println("2. Log Out");
        System.out.println("3. Exit");
    }

    // display user main menu
    public static void userMainMenu ()
    {
        System.out.println("\nUser Main Menu: ");
        System.out.println("1. Reserve Seats");
        System.out.println("2. View Orders");
        System.out.println("3. Update Order");
        System.out.println("4. Display Receipt");
        System.out.println("5. Log Out");
    }

    // display reserve seats submenu
    public static void submenuR ()
    {
        System.out.println("\nAuditorium Options: ");
        System.out.println("1. Auditorium 1");
        System.out.println("2. Auditorium 2");
        System.out.println("3. Auditorium 3");
    }

    // display update order submenu
    public static void submenuU()
    {
        System.out.println("\nUpdate Action Options: ");
        System.out.println("1. Add tickets to order");
        System.out.println("2. Delete tickets from order");
        System.out.println("3. Cancel Order");
    }

    // return admin report
    public static double[] adminReport (Auditorium a,int rowCnt, int seatCnt)
    {
        double[] summary = new double[6];
        // total seats
        int seatsTotal = rowCnt * seatCnt;
        int reserved = 0, adultTotal = 0, childTotal = 0, seniorTotal = 0;
        double salesTotal = 0.0;

        // use pointers to traverse
        Node<Seat> row = a.getFirst(), col = a.getFirst();

        // A/C/S tickets sold
        for (int r = 0; r < rowCnt; r++) {
            for (int s = 0; s < seatCnt; s++) {
                if (col.getPayload().getType() == 'A')
                    adultTotal++;
                if (col.getPayload().getType() == 'C')
                    childTotal++;
                if (col.getPayload().getType() == 'S')
                    seniorTotal++;

                // move to next seat/ node
                col = col.getNext();
            }
            // move to next row
            row = row.getDown();
            col = row;
        }

        // total tickets sold
        reserved = adultTotal + childTotal + seniorTotal;
        // open seats
        int open = seatsTotal - reserved;
        // total sales
        salesTotal = adultTotal * ADULT$ + childTotal * CHILD$ + seniorTotal * SENIOR$;

        System.out.print("\t"+ open+ "\t"+ reserved+ "\t"+ adultTotal+ "\t"+ childTotal+ "\t"+ seniorTotal);
        System.out.printf("\t$%.2f", salesTotal);
        System.out.println();

        summary[0] = open;
        summary[1] = reserved;
        summary[2] = adultTotal;
        summary[3] = childTotal;
        summary[4] = seniorTotal;
        summary[5] = salesTotal;

        return summary;
    }

    // store each auditorium in a new file
    public static void exit (Auditorium a, PrintWriter out,int cntRow, int cntSeat)
    {
        // use pointers to traverse through the auditorium
        Node<Seat> ptrRow = a.getFirst(), ptrSeat = a.getFirst();

        // write/ store the auditorium to the file
        for (int r = 0; r < cntRow; r++)
        {
            for (int s = 0; s < cntSeat; s++)
            {
                out.print(ptrSeat.getPayload().getType());
                ptrSeat = ptrSeat.getNext();
            }
            // move pointers to the next row
            ptrRow = ptrRow.getDown();
            ptrSeat = ptrRow;
            out.println();
        }

        // close files
        out.close();
    }

    // display auditorium
    public static void displayTheater (Auditorium a,int cntSeat)
    {
        System.out.println();
        // display column header
        System.out.print("  ");
        for (int s = 0; s < cntSeat; s++)
            System.out.print((char) (65 + s));
        System.out.println();
        // disply the auditorium
        a.display();
    }

    // reserve seats
    public static orders reserve(Scanner in, int menuChoice, Auditorium a, int noA, int cntRow, int cntSeat)
    {
        boolean invalid= true;
        int row=0, adultNo=0, childNo=0, seniorNo = 0, totalQ=0;
        char seat=' ';
        orders obj= null;

        // prompt for row number
        do
        {
            System.out.print("Row number: ");
            try
            {
                row= in.nextInt();
                // check if value entered is in the range
                if (row<0 || row>cntRow)
                {
                    System.out.println("Invalid value. Try again!");
                    invalid= true;
                    continue;
                }
                invalid= false;
            }
            // wrong data type is entered
            catch (InputMismatchException e)
            {
                invalid= true;
                System.out.println("Invalid value. Try again!");
                // throw away incorrect input
                in.nextLine();
            }
        }while(invalid);

        // prompt for starting seat letter
        do
        {
            System.out.print("Starting seat letter: ");
            try
            {
                seat = in.next().charAt(0);
            }
            // wrong data type is entered
            catch (InputMismatchException e)
            {
                invalid= true;
                System.out.println("Invalid value. Try again!");
                // throw away incorrect input
                in.nextLine();
            }
            // check if value entered is in the range
            if (seat > (char) (cntSeat +64) || (int)seat<65)
            {
                System.out.println("Invalid value. Try again!");
                invalid= true;
                continue;
            }
            invalid= false;

        }while(invalid);

        // prompt for adult tickets
        do
        {
            System.out.print("Number of adult tickets: ");
            try
            {
                adultNo = in.nextInt();
                // check if value entered is positive
                if (adultNo<0)
                {
                    System.out.println("Invalid value. Try again!");
                    invalid= true;
                    continue;
                }
                invalid= false;
            }
            // wrong data type is entered
            catch (InputMismatchException e)
            {
                invalid= true;
                System.out.println("Invalid value. Try again!");
                // throw away incorrect input
                in.nextLine();
            }
        }while(invalid);

        // prompt for child tickets
        do
        {
            System.out.print("Number of child tickets: ");
            try
            {
                childNo = in.nextInt();
                // check if value entered is positive
                if (childNo<0)
                {
                    System.out.println("Invalid value. Try again!");
                    invalid= true;
                    continue;
                }
                invalid= false;
            }
            // wrong data type is entered
            catch (InputMismatchException e)
            {
                invalid= true;
                System.out.println("Invalid value. Try again!");
                // throw away incorrect input
                in.nextLine();
            }
        }while(invalid);

        // prompt for senior tickets
        do
        {
            System.out.print("Number of senior tickets: ");
            try
            {
                seniorNo = in.nextInt();
                // check if value entered is positive
                if (seniorNo<0)
                {
                    System.out.println("Invalid value. Try again!");
                    invalid= true;
                    continue;
                }
                invalid= false;
            }
            // wrong data type is entered
            catch (InputMismatchException e)
            {
                invalid= true;
                System.out.println("Invalid value. Try again!");
                // throw away incorrect input
                in.nextLine();
            }
        }while(invalid);

        // total tickets
        totalQ = adultNo + childNo + seniorNo;

        // seats wanted are available
        if (checkAvailability(a, row, seat, totalQ))
        {
            obj= new orders();
            reserveSeats(a, row, seat, adultNo, childNo, seniorNo);
            obj.noAuditorium= noA;
            obj.noAdult= adultNo;
            obj.noChild= childNo;
            obj.noSenior= seniorNo;
            obj.noTotal= totalQ;

            // add every seat to the order object linkedlist seats
            for (int i=0; i< totalQ; i++)
                obj.seats.add(String.valueOf(row)+ (char)((int)seat+i));
        }

        // seats wanted not available
        else if (menuChoice==1)
        {
            // check if there are seats available in the entire auditorium
            Node<Seat> BS = bestAvailable(a, cntRow, cntSeat, totalQ);
            // no seats available
            if (BS == null)
                System.out.println("Seats not available");

            // seats available
            else
            {
                System.out.println("The best available seats are:");
                // only buying 1 ticket
                if (totalQ == 1)
                {
                    System.out.print(BS.getPayload().getRow());
                    System.out.println(BS.getPayload().getSeat());
                }

                // multiple tickets
                else
                {
                    char endSeat = (char) ((int) BS.getPayload().getSeat() + totalQ - 1);
                    System.out.print(BS.getPayload().getRow());
                    System.out.print(BS.getPayload().getSeat());
                    System.out.print(" - " + BS.getPayload().getRow());
                    System.out.println(endSeat);
                }

                System.out.print("Enter Y(reserve) or N(refuse) the seats: ");
                char YorN = in.next().charAt(0);

                if (YorN == 'Y')
                {
                    obj= new orders();
                    reserveSeats(a, BS.getPayload().getRow(), BS.getPayload().getSeat(), adultNo, childNo, seniorNo);

                    obj.noAuditorium= noA;
                    obj.noAdult= adultNo;
                    obj.noChild= childNo;
                    obj.noSenior= seniorNo;
                    obj.noTotal= totalQ;

                    // add every seat to the order object linkedlist seats
                    for (int i=0; i< totalQ; i++)
                        obj.seats.add(String.valueOf(BS.getPayload().getRow())+ (char)((int)BS.getPayload().getSeat()+i));
                }
            }
        }
        return obj;
    }

    // check if seats wanted are available
    public static boolean checkAvailability(Auditorium a, int rowEntered, char seatEntered, int total)
    {
        // get the position of the seat entered
        int seatIdx= (int)seatEntered-64;

        // use a pointer to move to the row entered
        Node<Seat> row= a.getFirst();
        for (int i=1; i<rowEntered; i++)
        {
            // move to next row
            row= row.getDown();
        }

        // use a pointer to move to the correct seat on the row
        Node<Seat> col= row;
        for (int j=1; j<seatIdx; j++)
        {
            // move the pointer to next seat/ node
            col= col.getNext();
        }

        // check if the sequential seats to the right of the first seat entered are empty
        for (int k=0; k<total; k++)
        {
            // '.'= empty
            if (col.getPayload().getType()!= '.')
                return false;

            // move to next seat/ node
            col= col.getNext();
        }
        return true;
    }

    // process the order; write to the auditorium and order LinkedList
    public static void reserveSeats(Auditorium a, int rowEntered, char seatEntered, int adultQ, int childQ, int seniorQ)
    {
        // get the position of the seat entered
        int seatIdx= (int)seatEntered-64;

        // use a pointer to move to the row entered
        Node<Seat> row= a.getFirst();
        for (int i=1; i<rowEntered; i++)
        {
            // move to next row
            row= row.getDown();
        }

        // use a pointer to move to the correct seat on the row
        Node<Seat> col= row;
        for (int j=1; j<seatIdx; j++)
        {
            // move the pointer to next seat/ node
            col= col.getNext();
        }

        // reserve seats for adults
        for (int w=0; w<adultQ; w++)
        {
            col.getPayload().setType('A');
            col= col.getNext();
        }

        // reserve seats for children
        for (int x=0; x<childQ; x++)
        {
            col.getPayload().setType('C');
            col= col.getNext();
        }

        // reserve seats for seniors
        for (int y=0; y<seniorQ; y++)
        {
            col.getPayload().setType('S');
            col= col.getNext();
        }

        System.out.println("Seats are reserved!");
    }

    // find the best alternative seats
    public static Node<Seat> bestAvailable(Auditorium a, int rowCnt, int seatCnt, int total)
    {
        // use two pointers to go through the grid
        Node<Seat> row = a.getFirst();
        Node<Seat> col = a.getFirst();
        // use two variables to compare options
        Node<Seat> availOption = null;
        Node<Seat> best = null;
        // set bound for seats
        int bound= seatCnt-total+ 1;
        // center of the auditorium
        double centerX= (seatCnt+1)/2.0;
        double centerY= (rowCnt+1)/2.0;

        double dist=0, rowDist=0, minDist= centerX;

        // go through the entire auditorium
        while (row!=null)
        {
            // bound checker
            int boundCheck=0;
            // check every seat on the row
            while (col != null)
            {
                // increment bound checker
                boundCheck++;

                // no enough seats for quantities wanted after this point
                if (boundCheck > bound)
                    break;

                else
                {
                    Node<Seat> start= col;
                    // assign the node to be the starting seat
                    availOption= col;
                    // check if there are sequential empty seats for the quantities wanted
                    for (int k = 0; k < total; k++)
                    {
                        // '.'= empty
                        if ('.' != start.getPayload().getType())
                        {
                            availOption = null;
                            break;
                        }
                        // move to next seat/ node
                        start = start.getNext();
                    }

                    // when seats are available, check the distance to the center
                    if (availOption != null)
                    {
                        // calculate the distance from the center of the seat selection to the center of the auditorium
                        double seatX= (double) availOption.getPayload().getSeat()- 64+ (total- 1)/ 2.0;
                        double seatY= (double) availOption.getPayload().getRow();
                        double x= Math.abs(centerX-seatX);
                        double y= Math.abs(centerY-seatY);
                        dist= Math.hypot(x, y);

                        // new min distance found
                        if (dist< minDist)
                        {
                            rowDist= Math.abs(centerY- availOption.getPayload().getRow());
                            minDist= dist;
                            best= availOption;
                        }

                        // distance is tied: select the row closest to the middle of the auditorium
                        else if (dist== minDist)
                        {
                            double rowDist2= Math.abs(centerY- availOption.getPayload().getRow());
                            if (rowDist> rowDist2)
                                best= availOption;
                        }
                    }
                    // move to next seat/ node
                    col= col.getNext();
                }
            }
            // move down to next row
            row= row.getDown();
            col= row;
        }
        return best;
    }

    public static void view(LinkedList<orders> list)
    {
        // no order
        if (list.size()==0)
        {
            System.out.println("No orders");
            return;
        }

        // print all the orders
        for (int i=0; i< list.size(); i++)
        {
            // for readability
            orders o= list.get(i);
            Collections.sort(o.seats);

            System.out.print("\nAuditorium "+ o.noAuditorium + ", ");

            // print all the seats
            for (int j=0; j<o.noTotal; j++)
            {
                // only one seat or the last seat
                if (o.noTotal==1 || j== o.noTotal-1)
                {
                    String seat= o.seats.get(0);
                    System.out.println(o.seats.get(j));
                }

                else
                    System.out.print(o.seats.get(j)+ ",");
            }
            System.out.println(o.noAdult+ " adult, "+ o.noChild+ " child, "+ o.noSenior+ " senior");
        }
    }

    public static void displayOrders(LinkedList<orders> list)
    {
        System.out.println();
        // print all the orders
        for (int i = 0; i < list.size(); i++)
        {
            System.out.print("Order #" + (i + 1) + ": ");

            // for readability
            orders o = list.get(i);
            Collections.sort(o.seats);

            System.out.print("Auditorium " + o.noAuditorium + ", ");

            // print all the seats
            for (int j = 0; j < o.noTotal; j++)
            {
                // only one seat or the last seat
                if (o.noTotal == 1 || j == o.noTotal - 1)
                {
                    String seat = o.seats.get(0);
                    System.out.print(o.seats.get(j));
                }
                else
                    System.out.print(o.seats.get(j) + ",");
            }

            System.out.println(", " + o.noAdult + " adult, " + o.noChild + " child, " + o.noSenior + " senior");
        }
    }

    // add a seat to an order
    public static void addToOrder(orders ori, orders addOn)
    {
        // update quantities
        ori.noAdult+= addOn.noAdult;
        ori.noChild+= addOn.noChild;
        ori.noSenior+= addOn.noSenior;
        ori.noTotal+= addOn.noTotal;

        // add all new seats the the original order
        for (int i=0; i<addOn.seats.size(); i++)
            ori.seats.add(addOn.seats.get(i));
    }

    // delete a seat from an order
    public static void deleteSeat(Auditorium a, orders ori, String s)
    {
        // use 2 pointers to traverse through the auditorium
        Node<Seat> row= null;
        Node<Seat> cur = null;

        // find the index of the seat in the seats LinkedList
        int idx= ori.seats.indexOf(s);
        int r=  Character.getNumericValue(s.charAt(0));
        int c= (int)s.charAt(1)-64;

        // seat entered is found in the order
        if (idx!=-1)
        {
            row= a.getFirst();

            // move to the row number
            for (int i=1; i< r; i++)
                row= row.getDown();

            // move to thae row number
            cur= row;
            for (int j=1; j< c; j++)
                cur= cur.getNext();

            // reserved for adult
            if (cur.getPayload().getType()=='A')
            {
                // #adult- 1
                ori.noAdult -= 1;
            }

            // reserved for child
            else if (cur.getPayload().getType()=='C')
            {
                // #child -1
                ori.noChild-=1;
            }

            // reserved for senior
            else if (cur.getPayload().getType()=='S')
            {
                // #senior -1
                ori.noSenior-=1;
            }

            // write a '.' to the auditorium
            cur.getPayload().setType('.');
            // total# -1
            ori.noTotal-=1;

            ori.seats.remove(s);
        }
        else
            System.out.println("Can't find the seat entered in this order.");
    }

    // remove an order
    public static void cancelOrder(Auditorium a, LinkedList<orders> list, int idx)
    {
        int count= list.get(idx).noTotal;
        for (int i= count-1; i>=0; i--)
        {
            String seat= list.get(idx).seats.get(i);
            deleteSeat(a,list.get(idx), seat);
        }
        list.remove(idx);
    }

    //display user's receipt
    public static void displayReceipt(LinkedList<orders> list)
    {
        // no order
        if (list.size()==0)
        {
            System.out.println("Customer Total: $0.00");
            return;
        }

        System.out.println("\n-------Receipts-------");

        double sum=0;
        // print all the orders
        for (int i=0; i< list.size(); i++)
        {
            // for readability
            orders o= list.get(i);
            Collections.sort(o.seats);

            System.out.print("Auditorium "+ o.noAuditorium + ", ");

            // print all the seats
            for (int j=0; j<o.noTotal; j++)
            {
                // only one seat or the last seat
                if (o.noTotal==1 || j== o.noTotal-1)
                {
                    String seat= o.seats.get(0);
                    System.out.println(o.seats.get(j));
                }

                else
                    System.out.print(o.seats.get(j)+ ",");
            }
            System.out.println(o.noAdult+ " adult, "+ o.noChild+ " child, "+ o.noSenior+ " senior");

            // order total
            double order$= o.noAdult*ADULT$+ o.noChild* CHILD$+ o.noSenior* SENIOR$;
            // customer total
            sum+=order$;
            System.out.printf("Order Total: $%.2f\n", order$);
        }
        System.out.printf("\n-> Customer Total: $%.2f\n", sum);
    }

}// end MAIN