public class FrontDesk implements Runnable {
    public int employeeNum;
    // static bc otherwise it won't update properly for each thread
    public static int roomNum = 0; // start at 0 and then increment for each guest
    public Main main;
    public Thread deskThread;

    public FrontDesk (int eNum, Main m) {
        this.employeeNum = eNum;
        this.main= m;
        System.out.println("Front desk employee " + employeeNum + " created");
        deskThread = new Thread(this);
        deskThread.start();
    }

    @Override
    public void run() {
            try {
                while (true) {
                    main.guest_ready.acquire(); // wait for a ready guest

                    main.guest_mutex.acquireUninterruptibly();
                    Guest g = main.frontdeskQueue.remove();
                    this.roomNum++;
                    g.roomNum = this.roomNum;
                    main.guest_mutex.release();

                    g.employeeAssigned = this.employeeNum;

                    System.out.println("Front desk employee " + employeeNum + " registers guest " + g.guestNum +
                            " and assigns room " + g.roomNum);
                    main.assign_room.release();

                    main.guest_left.acquire(); // wait for the guest to leave before releasing the employee
                    main.desk_employee.release();

                }
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

    }
}
