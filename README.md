This project is a Parking Lot Management System implemented in Java using Object-Oriented Design and key Design Patterns. It models a real-world parking lot with support for both two-wheelers and four-wheelers, dynamic pricing, entry and exit gates, and flexible strategies for allocating spots.

At its core, the system uses an abstract ParkingLot class, extended by TwoWheelerParking and FourWheelerParking, ensuring common behavior (add/remove vehicles) while allowing type-specific pricing. Vehicles are categorized via an enum, making the system easily extendable to more types in the future.

The Strategy Pattern is applied through the ParkingStrategy interface, allowing different spot allocation algorithms, such as FirstAvailableStrategy or NearestToGateStrategy. This design enables plug-and-play flexibility where new strategies (e.g., VIP priority, price-based selection) can be added without modifying existing code.

Parking is orchestrated by an abstract ParkingManager, with concrete managers (TwoWheelerManager, FourWheelerManager) handling their respective spots. To simplify manager creation, the Factory Pattern is used via ParkingManagerFactory, ensuring the right manager is provided based on vehicle type.

Interaction happens through Entry and Exit Gates, which delegate responsibilities to the correct manager. In the current implementation, gates can serve both vehicle types by internally mapping to the appropriate manager.

The design emphasizes abstraction, composition, and extensibility: strategies can be swapped at runtime, new vehicle categories can be introduced, and business rules (like billing) can be layered without breaking existing functionality.

This modular approach makes the system robust, testable, and ready for future extensions such as multi-floor parking, ticketing, or time-based billing.
