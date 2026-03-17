import java.io.*;
import java.util.*;

class FilePersistenceService {
    public void saveInventory(RoomInventory inventory, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("Single=" + inventory.getAvailable("Single"));
            writer.println("Double=" + inventory.getAvailable("Double"));
            writer.println("Suite=" + inventory.getAvailable("Suite"));
            System.out.println("Inventory saved successfully.");
        } catch (IOException e) {
            System.out.println("Failed to save inventory: " + e.getMessage());
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
                if (parts.length == 2) {
                    String type = parts[0].trim();
                    int count = Integer.parseInt(parts[1].trim());
                    inventory.setAvailable(type, count);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Failed to load inventory. Starting fresh.");
        }
    }
}

class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single", 5);
        inventory.put("Double", 3);
        inventory.put("Suite", 2);
    }

    public boolean isAvailable(String type) {
        return inventory.getOrDefault(type, 0) > 0;
    }

    public void allocate(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }

    public int getAvailable(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void setAvailable(String type, int count) {
        inventory.put(type, count);
    }
}

public class BookMystay {
    public static void main(String[] args) {
        String filePath = "inventory.txt";
        RoomInventory inventory = new RoomInventory();
        FilePersistenceService persistenceService = new FilePersistenceService();

        persistenceService.loadInventory(inventory, filePath);

        System.out.println("\nCurrent Inventory:");
        System.out.println("Single: " + inventory.getAvailable("Single"));
        System.out.println("Double: " + inventory.getAvailable("Double"));
        System.out.println("Suite: " + inventory.getAvailable("Suite"));

        persistenceService.saveInventory(inventory, filePath);
    }
}