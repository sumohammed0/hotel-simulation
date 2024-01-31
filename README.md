# hotel-simulation
This project simulates a hotel through the use of threads and semaphores to model guest and employee behavior. There are two employees and two bellhops, each of which is a thread. The order of operation is the guest will visit the front desk for a number. If the guest has more than three bags, they will drop their bags at the bellhop and go to their assigned room. The bellhop will bring the bags. There are 25 guests and one thread is created per guest at the beginning of this simulation.

Instructions 
Compile: Javac *.java
Run: Java Main
