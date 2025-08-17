import java.util.*;

// ---------- PARKING LOT ----------
abstract class ParkingLot {

    int id;
    boolean isEmpty;
    Vehicle vehicle;

    ParkingLot(int id) {
        this.id = id;
        isEmpty = true;
    }

    abstract public int price();

    void add(Vehicle v) {
        vehicle = v;
        isEmpty = false;
    }

    void remove() {
        vehicle = null;
        isEmpty = true;
    }

    boolean isAvailable() {
        return isEmpty;
    }
}

class TwoWheelerParking extends ParkingLot {
    TwoWheelerParking(int id) {
        super(id);
    }
    @Override
    public int price() {
        return 50;
    }
}

class FourWheelerParking extends ParkingLot {
    FourWheelerParking(int id) {
        super(id);
    }
    @Override
    public int price() {
        return 100;
    }
}

// ---------- VEHICLE ----------
enum Type {
    TWO, FOUR;
}

class Vehicle {
    int number;
    Type t;
    Vehicle(int number, Type t) {
        this.number = number;
        this.t = t;
    }
}

// ---------- STRATEGY ----------
interface ParkingStrategy {
    ParkingLot find(List<ParkingLot> spots, Vehicle v);
}

// Strategy: first available
class FirstAvailableStrategy implements ParkingStrategy {
    public ParkingLot find(List<ParkingLot> spots, Vehicle v) {
        for (ParkingLot lot : spots) {
            if (lot.isAvailable()) return lot;
        }
        return null;
    }
}

// Strategy: nearest to gate (lowest ID = closest)
class NearestToGateStrategy implements ParkingStrategy {
    public ParkingLot find(List<ParkingLot> spots, Vehicle v) {
        return spots.stream()
                .filter(ParkingLot::isAvailable)
                .min(Comparator.comparingInt(l -> l.id))
                .orElse(null);
    }
}

// ---------- PARKING MANAGER ----------
abstract class ParkingManager {
    List<ParkingLot> spots = new ArrayList<>();
    ParkingStrategy myStrategy;

    ParkingManager(ParkingStrategy strategy) {
        this.myStrategy = strategy;
    }

    abstract ParkingLot find(Vehicle v);

    abstract void addSpot(ParkingLot lot);

    abstract void removeSpot(ParkingLot lot);

    abstract boolean addVehicle(Vehicle v);

    abstract void removeVehicle(Vehicle v);
}

class TwoWheelerManager extends ParkingManager {
    TwoWheelerManager(ParkingStrategy strategy) {
        super(strategy);
    }

    @Override
    ParkingLot find(Vehicle v) {
        return myStrategy.find(spots, v);
    }

    @Override
    void addSpot(ParkingLot lot) {
        if (lot instanceof TwoWheelerParking) spots.add(lot);
    }

    @Override
    void removeSpot(ParkingLot lot) {
        spots.remove(lot);
    }

    @Override
    boolean addVehicle(Vehicle v) {
        ParkingLot spot = find(v);
        if (spot != null) {
            spot.add(v);
            return true;
        }
        return false;
    }

    @Override
    void removeVehicle(Vehicle v) {
        for (ParkingLot lot : spots) {
            if (!lot.isAvailable() && lot.vehicle.number == v.number) {
                lot.remove();
                return;
            }
        }
    }
}

class FourWheelerManager extends ParkingManager {
    FourWheelerManager(ParkingStrategy strategy) {
        super(strategy);
    }

    @Override
    ParkingLot find(Vehicle v) {
        return myStrategy.find(spots, v);
    }

    @Override
    void addSpot(ParkingLot lot) {
        if (lot instanceof FourWheelerParking) spots.add(lot);
    }

    @Override
    void removeSpot(ParkingLot lot) {
        spots.remove(lot);
    }

    @Override
    boolean addVehicle(Vehicle v) {
        ParkingLot spot = find(v);
        if (spot != null) {
            spot.add(v);
            return true;
        }
        return false;
    }

    @Override
    void removeVehicle(Vehicle v) {
        for (ParkingLot lot : spots) {
            if (!lot.isAvailable() && lot.vehicle.number == v.number) {
                lot.remove();
                return;
            }
        }
    }
}

// ---------- FACTORY ----------
class ParkingManagerFactory {
    static ParkingManager getManager(Type t, ParkingStrategy strategy) {
        if (t == Type.TWO) {
            return new TwoWheelerManager(strategy);
        } else {
            return new FourWheelerManager(strategy);
        }
    }
}

// ---------- ENTRY & EXIT GATES ----------
class EntryGate {
    ParkingManager manager;
    EntryGate(ParkingManager manager) {
        this.manager = manager;
    }

    boolean allowEntry(Vehicle v) {
        return manager.addVehicle(v);
    }
}

class ExitGate {
    ParkingManager manager;
    ExitGate(ParkingManager manager) {
        this.manager = manager;
    }

    void allowExit(Vehicle v) {
        manager.removeVehicle(v);
    }
}

// ---------- MAIN ----------
class main {
    public static void main(String args[]) {
        ParkingStrategy strategy = new NearestToGateStrategy();
        ParkingManager twoManager = ParkingManagerFactory.getManager(Type.TWO, strategy);

        // Add some spots
        twoManager.addSpot(new TwoWheelerParking(1));
        twoManager.addSpot(new TwoWheelerParking(2));

        EntryGate entry = new EntryGate(twoManager);
        ExitGate exit = new ExitGate(twoManager);

        Vehicle v1 = new Vehicle(101, Type.TWO);

        System.out.println("Vehicle entering: " + entry.allowEntry(v1));
        exit.allowExit(v1);
        System.out.println("Vehicle exited!");
    }
}
