import java.io.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

class FilePersistenceService {


    public void saveInventory(RoomInventory inventory, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            for (String roomType : inventory.getAllRoomTypes()) {
                int count = inventory.getAvailableCount(roomType);
                writer.write(roomType + "=" + count);
                writer.newLine();
            }

            System.out.println("Inventory saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }


    public void loadInventory(RoomInventory inventory, String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("No valid inventory data found. Starting fresh.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");

                if (parts.length != 2) continue;

                String roomType = parts[0];
                int count = Integer.parseInt(parts[1]);

                inventory.setAvailableCount(roomType, count);
            }

            System.out.println("Inventory loaded successfully.");

        } catch (Exception e) {
            System.out.println("Error loading inventory. Starting fresh.");
        }
    }
}


public class BookMyStayApp {

    public static void main(String[] args) {

        String filePath = "inventory.txt";

        RoomInventory inventory = new RoomInventory();
        FilePersistenceService persistenceService = new FilePersistenceService();

        System.out.println("System Recovery");

        // Load previous state
        persistenceService.loadInventory(inventory, filePath);

        // If no data exists, initialize defaults
        if (inventory.isEmpty()) {
            inventory.addRoomType("Single", 5);
            inventory.addRoomType("Double", 3);
            inventory.addRoomType("Suite", 2);
        }

        // Display current inventory
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.getAllRoomTypes()) {
            System.out.println(type + ": " + inventory.getAvailableCount(type));
        }

        // Simulate saving before shutdown
        persistenceService.saveInventory(inventory, filePath);
    }
}


class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public void setAvailableCount(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailableCount(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public Set<String> getAllRoomTypes() {
        return inventory.keySet();
    }

    public boolean isEmpty() {
        return inventory.isEmpty();
    }
}