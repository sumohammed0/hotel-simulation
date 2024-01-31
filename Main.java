import java.util.*;
import java.util.concurrent.Semaphore;

import static java.lang.System.exit;

public class Main {
    // semaphores
    Semaphore guest_mutex = new Semaphore(1, true); // mutex for guest queue for front desk
    Semaphore bellhop_mutex = new Semaphore(1, true); // mutex for bellhop queue bc bellhop leaves

    Semaphore guest_ready = new Semaphore(0, true); // guest is in the queue for desk
    Semaphore desk_employee = new Semaphore(2, true); // desk employee available
    Semaphore bellhop = new Semaphore(2, true); // bellhop available
    Semaphore bags_received = new Semaphore(0, true); // let guest know that bellhop has the bags
    Semaphore assign_room = new Semaphore(0, true); // room is assigned by front desk employee
    Semaphore bellhop_needed = new Semaphore(0, true); // guest has too many bags and needs bellhop
    Semaphore at_room = new Semaphore(0, true); // guest is in room
    Semaphore at_roomb = new Semaphore(0, true); // the guest has reached the room (for bellhop)
    Semaphore bags_given = new Semaphore(0, true); // bags have been given to the bellhop by the guest
    Semaphore bags_delivered = new Semaphore(0, true); // bags have been given in room
    Semaphore guest_left = new Semaphore(0, true); // guest has left the desk
    Semaphore bags_received_g = new Semaphore(0, true); // let bellhop know bags have been recieved by guest at hotel room

    // queues
    Queue<Guest> frontdeskQueue = new LinkedList<>(); // line for front desk
    Queue<Guest> bellhopQueue = new LinkedList<>(); // line for bellhop

    // keep track of how many guest threads have ended
    public static int guestJoinedNum = 0;

    public static void main(String[] args) {
        Main main = new Main();
        // create front desk threads
        for (int f = 0; f < 2; f++) {
            FrontDesk frontdesk  = new FrontDesk(f, main);
        }
        // create bellhop threads
        for (int b = 0; b < 2; b++) {
            Bellhop bellhop = new Bellhop(b, main);
        }
        // create guest threads
        for (int g = 0; g < 25; g++) {
            Guest guest = new Guest(g, main);
        }

        // end when all the guests have finished
        while (main.guestJoinedNum != 25) {
            // do something while waiting
            System.out.print("");
        }

        System.out.println("Simulation ends");
        exit(0);
    }
}