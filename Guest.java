import java.util.*;

public class Guest implements Runnable {
    public int guestNum;
    public int bagNum;
    public int roomNum;
    public int employeeAssigned;
    public int bellhopAssigned;
    Thread guestThread;
    public Main main;

    public Guest (int gnum, Main m) {
        this.main= m;
        this.guestNum = gnum;
        Random random = new Random();
        bagNum = random.nextInt(6); // generate random number from 0-5

        System.out.println("Guest " + guestNum + " created");
        // start thread
        guestThread = new Thread(this);
        guestThread.start();
    }

    @Override
    public void run() {
        System.out.println("Guest " + guestNum + " enters the hotel with "
                + bagNum + " bags");

        try {
            main.guest_mutex.acquire();
            main.frontdeskQueue.add(this); // add guest to the line for front desk employee
            main.guest_mutex.release();

            main.desk_employee.acquire(); // wait for available desk employee
            main.guest_ready.release(); // guest is ready inside of queue

            main.assign_room.acquire(); // wait to be assigned a room
            System.out.println("Guest " + guestNum + " receives room key for room " + roomNum +
                    " from front desk employee "+ employeeAssigned);

            main.guest_left.release();

            if (bagNum > 2) {
                main.bellhop_mutex.acquire();
                main.bellhopQueue.add(this); // add guest to the line for bellhop
                main.bellhop_mutex.release();

                System.out.println("Guest " + guestNum + " requests help with bags");
                main.bellhop_needed.release(); // signal that a bellhop is needed


                main.bellhop.acquire(); // get available bellhop

                main.bags_given.release(); // give bags to bellhop

                main.bags_received.acquire(); // see if bags have been taken by the bellhop

                System.out.println("Guest " + guestNum + " enters room " + roomNum);
                main.at_roomb.release(); // guest is in their room

                main.bags_delivered.acquire(); // wait for bellhop to deliver the bags
                System.out.println("Guest " + guestNum + " receives bags from bellhop " + bellhopAssigned + " and gives tip");

                main.bags_received_g.release(); // let bellhop know guest has gotten their bags
            }
            else {
                main.at_room.release(); // guest is in their room
                System.out.println("Guest " + guestNum + " enters room " + roomNum);
            }

            System.out.println("Guest " + guestNum + " retires for the evening"); // guest is done

            Main.guestJoinedNum++;
            System.out.println("Guest " + guestNum + " joined");
            guestThread.join();


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
