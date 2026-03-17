import java.util.*;

class RoomInventory {
    private Map<String, Integer> inventory;
    public RoomInventory() { inventory = new HashMap<>(); }
    public void addRoomType(String roomType, int count) { inventory.put(roomType, count); }
    public boolean isAvailable(String roomType) { return inventory.getOrDefault(roomType, 0) > 0; }
    public void incrementInventory(String roomType) { inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1); }
    public int getAvailability(String roomType) { return inventory.getOrDefault(roomType, 0); }
}

class CancellationService {
    private Stack<String> releasedRoomIds;
    private Map<String, String> reservationRoomTypeMap;
    public CancellationService() { releasedRoomIds = new Stack<>(); reservationRoomTypeMap = new HashMap<>(); }
    public void registerBooking(String reservationId, String roomType) { reservationRoomTypeMap.put(reservationId, roomType); }
    public void cancelBooking(String reservationId, RoomInventory inventory) {
        if (!reservationRoomTypeMap.containsKey(reservationId)) { System.out.println("Cancellation failed: Reservation ID does not exist."); return; }
        String roomType = reservationRoomTypeMap.get(reservationId);
        releasedRoomIds.push(reservationId);
        inventory.incrementInventory(roomType);
        reservationRoomTypeMap.remove(reservationId);
        System.out.println("Booking cancelled successfully. Inventory restored for room type: " + roomType);
    }
    public void showRollbackHistory() {
        System.out.println("\nRollback History (Most Recent First):");
        for (int i = releasedRoomIds.size() - 1; i >= 0; i--) { System.out.println("Released Reservation ID: " + releasedRoomIds.get(i)); }
    }
}

public class BookMystay {
    public static void main(String[] args) {
        System.out.println("Booking Cancellation");
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single", 6);
        inventory.addRoomType("Double", 3);
        inventory.addRoomType("Suite", 2);
        CancellationService cancellationService = new CancellationService();
        cancellationService.registerBooking("Single-1", "Single");
        cancellationService.registerBooking("Double-1", "Double");
        cancellationService.registerBooking("Suite-1", "Suite");
        cancellationService.cancelBooking("Single-1", inventory);
        cancellationService.showRollbackHistory();
        System.out.println("\nUpdated Single Room Availability: " + inventory.getAvailability("Single"));
    }
}