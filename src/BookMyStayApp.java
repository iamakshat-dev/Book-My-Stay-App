import java.util.HashMap;
import java.util.Map;

import java.util.LinkedList;
import java.util.Queue;

class ConcurrentBookingProcessor implements Runnable {

    // Shared booking request queue
    private BookingRequestQueue bookingQueue;

    // Shared room inventory
    private RoomInventory inventory;

    // Shared allocation service
    private RoomAllocationService allocationService;

    // Constructor
    public ConcurrentBookingProcessor(
            BookingRequestQueue bookingQueue,
            RoomInventory inventory,
            RoomAllocationService allocationService) {

        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
        this.allocationService = allocationService;
    }

    // Thread execution
    @Override
    public void run() {

        while (true) {
            Reservation reservation;

            // 🔒 Critical Section 1: Access booking queue safely
            synchronized (bookingQueue) {

                if (bookingQueue.isEmpty()) {
                    break; // Exit when no more requests
                }

                reservation = bookingQueue.getNextRequest();
            }

            // 🔒 Critical Section 2: Allocate room safely
            synchronized (inventory) {
                allocationService.allocateRoom(reservation, inventory);
            }
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        // Shared resources
        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService allocationService = new RoomAllocationService();

        // Add booking requests
        bookingQueue.addRequest(new Reservation("John", "Single"));
        bookingQueue.addRequest(new Reservation("Alice", "Double"));
        bookingQueue.addRequest(new Reservation("Bob", "Suite"));
        bookingQueue.addRequest(new Reservation("Emma", "Single"));

        // Create threads
        Thread t1 = new Thread(
                new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService)
        );

        Thread t2 = new Thread(
                new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService)
        );

        // Start threads
        t1.start();
        t2.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread execution interrupted.");
        }

        // Print remaining inventory
        System.out.println("\nRemaining Inventory:");
        inventory.printInventory();
    }
}

class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}


class RoomInventory {

    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("Single", 3);
        rooms.put("Double", 2);
        rooms.put("Suite", 1);
    }

    public boolean isAvailable(String type) {
        return rooms.getOrDefault(type, 0) > 0;
    }

    public void bookRoom(String type) {
        rooms.put(type, rooms.get(type) - 1);
    }

    public void printInventory() {
        for (Map.Entry<String, Integer> entry : rooms.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

class RoomAllocationService {

    public void allocateRoom(Reservation reservation, RoomInventory inventory) {

        String type = reservation.getRoomType();

        if (inventory.isAvailable(type)) {
            inventory.bookRoom(type);
            System.out.println("Booking confirmed for Guest: "
                    + reservation.getGuestName()
                    + ", Room Type: " + type);
        } else {
            System.out.println("No rooms available for Guest: "
                    + reservation.getGuestName()
                    + ", Room Type: " + type);
        }
    }
}


class BookingRequestQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}