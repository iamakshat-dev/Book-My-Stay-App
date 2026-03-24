// Main Class
import java.util.HashMap;
import java.util.Map;

// ================= MAIN CLASS =================
public class BookMyStayApp {

    public static void main(String[] args) {

        // Create room objects (domain model)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Initialize centralized inventory
        RoomInventory inventory = new RoomInventory();

        System.out.println("Hotel Room Inventory (Centralized)\n");

        // Display room details + availability from inventory
        display(single, inventory);
        display(doubleRoom, inventory);
        display(suite, inventory);

        // Example update
        System.out.println("\n--- Updating Availability ---");
        inventory.updateAvailability("SingleRoom", 4);

        // Display after update
        System.out.println("\nAfter Update:\n");
        display(single, inventory);
    }

    private static void display(Room room, RoomInventory inventory) {
        room.displayRoomDetails();
        int available = inventory.getRoomAvailability()
                .get(room.getClass().getSimpleName());
        System.out.println("Available: " + available);
        System.out.println();
    }
}

// ================= INVENTORY CLASS =================
class RoomInventory {

    // Key = Room type name, Value = available count
    private Map<String, Integer> roomAvailability;

    // Constructor
    public RoomInventory() {
        roomAvailability = new HashMap<>();
        initializeInventory();
    }

    // Initialize default availability
    private void initializeInventory() {
        roomAvailability.put("SingleRoom", 5);
        roomAvailability.put("DoubleRoom", 3);
        roomAvailability.put("SuiteRoom", 2);
    }

    // Get current availability map
    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }

    // Update availability for a room type
    public void updateAvailability(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }
}

// ================= ABSTRACT CLASS =================
abstract class Room {
    protected int numberOfBeds;
    protected int squareFeet;
    protected double pricePerNight;

    public Room(int numberOfBeds, int squareFeet, double pricePerNight) {
        this.numberOfBeds = numberOfBeds;
        this.squareFeet = squareFeet;
        this.pricePerNight = pricePerNight;
    }

    public abstract void displayRoomDetails();
}

// ================= ROOM TYPES =================
class SingleRoom extends Room {

    public SingleRoom() {
        super(1, 250, 1500.0);
    }

    @Override
    public void displayRoomDetails() {
        System.out.println("Single Room:");
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + squareFeet + " sqft");
        System.out.println("Price per night: " + pricePerNight);
    }
}

class DoubleRoom extends Room {

    public DoubleRoom() {
        super(2, 400, 2500.0);
    }

    @Override
    public void displayRoomDetails() {
        System.out.println("Double Room:");
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + squareFeet + " sqft");
        System.out.println("Price per night: " + pricePerNight);
    }
}

class SuiteRoom extends Room {

    public SuiteRoom() {
        super(3, 750, 5000.0);
    }

    @Override
    public void displayRoomDetails() {
        System.out.println("Suite Room:");
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + squareFeet + " sqft");
        System.out.println("Price per night: " + pricePerNight);
    }
}
