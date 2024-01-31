public class Bellhop implements Runnable {
    public int bellhopNum;
    public Main main;
    public Thread bellhopThread;

    public Bellhop (int bNum, Main m) {
        main = m;
        this.bellhopNum = bNum;
        // start thread
        System.out.println("Bellhop " + bellhopNum + " created");
        bellhopThread = new Thread(this);
        bellhopThread.start();
    }
    @Override
    public void run() {
            try {
                while (true) {
                    main.bellhop_needed.acquire(); // wait until bellhop is needed
                    main.bellhop_mutex.acquireUninterruptibly();
                    Guest g = main.bellhopQueue.remove(); // remove guest from bellhop line
                    main.bellhop_mutex.release();

                    g.bellhopAssigned = bellhopNum; // assign bellhop to the guest
                    main.bags_given.acquire();

                    System.out.println("Bellhop " + bellhopNum + " receives bags from guest " + g.guestNum);
                    main.bags_received.release(); // signal that bags have been received

                    main.at_roomb.acquire(); // wait for guest to enter room

                    System.out.println("Bellhop " + bellhopNum + " delivers bags to guest " + g.guestNum);
                    main.bags_delivered.release(); // deliver the bag once guest is in their room

                    main.bags_received_g.acquire();
                    main.bellhop.release(); // release the bellhop
                }

            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
    }
}
