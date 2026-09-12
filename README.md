# DISASTER RELIEF RESOURCE COORDINATOR
**B.Tech Major Project &bull; 3-Tier Web Architecture**

A comprehensive, full-stack disaster response and logistics coordination platform engineered for emergency management during floods, earthquakes, cyclones, and landslides.

---

## 1. Project Team & Responsibilities

| Team Member | Core Responsibility Area | Modules Implemented |
| :--- | :--- | :--- |
| **Dr. Rethish Kumar** | System Architecture & Backend Core | OOP Class Hierarchy, Concurrency & Thread Locks, Services, Integration Testing |
| **Dipanshu Verma** | Database & Resource Management | Relational Schema DDL, Dual-Mode JDBC (MySQL + H2), Resource/Request Services |
| **Hari Krishna** | Web Frontend & Transport Logistics | Responsive JSP UI, Volunteer & Fleet Management, Live Delivery Tracking |

---

## 2. Key Architecture & Assessment Mappings

### 3-Tier Web Architecture
- **Presentation Tier**: JSP/JSTL, Modern Emergency-Ops CSS theme, dynamic modals, live transit progression timeline.
- **Application Tier**: Pure Java OOP Business Services, Servlet Controllers, Multithreaded Workers, Custom Exception Handling, Object Serialization, File I/O.
- **Data Tier**: Resilient Dual-Mode Engine (MySQL 8.0 production standard + zero-config embedded H2 fallback), normalized tables, foreign keys, JDBC `PreparedStatement`.

### Core Technical Syllabus Concepts Demonstrated
1. **OOP Encapsulation**: Private model variables, validated getter/setter mutators, invariants.
2. **OOP Inheritance**: `abstract class User` extended by specialized actors: `Coordinator`, `ShelterUser`, `Organization`, and `Volunteer`.
3. **OOP Polymorphism**: Dynamic method dispatch for role dashboards, polymorphic user mapping in JDBC, priority score comparators.
4. **OOP Abstraction**: DAO and Service separation pattern.
5. **Java Collections**:
   - `PriorityQueue<ResourceRequest>`: Priority-ranked emergency queue processing life-saving supplies before lower-priority needs.
   - `HashMap<String, List<Resource>>`: Instant $O(1)$ categorization and lookup of resources by type.
   - `HashSet<String>`: Deduplication and fast verification of unique volunteer certifications and skills.
   - `Queue<Allocation>`: FIFO dispatch queue for staging pending delivery operations.
6. **Multithreading & Concurrency**:
   - `DeliveryTrackingWorker`: Background thread simulating delivery milestones (PREPARING $\rightarrow$ DISPATCHED $\rightarrow$ IN_TRANSIT $\rightarrow$ DELIVERED).
   - `AsyncFileLoggingWorker`: Producer-consumer pattern using `BlockingQueue` for non-blocking file I/O.
   - `PeriodicSnapshotWorker`: Scheduled executor creating periodic system snapshots.
   - `ReentrantLock` / `synchronized`: Concurrency guard preventing race conditions and negative inventory during simultaneous allocation requests.
7. **File Handling & Serialization**:
   - Logging to disk: `logs/system_activity.log`, `logs/allocations.log`, `logs/delivery_tracking.log`.
   - Structured Export: CSV allocation summary and TXT operational briefing.
   - Java Serialization: `SystemSnapshot` implements `Serializable`; exports `.ser` snapshot files and restores complete state without database dependence.
8. **Custom Exception Hierarchy**:
   - `DisasterReliefException` (base)
   - `InvalidRequestException`
   - `InsufficientResourceException`
   - `ResourceExpiredException`
   - `ShelterNotFoundException`
   - `DeliveryUnavailableException`
   - `DatabaseException`

---

## 3. Priority Queue Scoring Formula

To ensure equitable and life-saving supply distribution, resource requests are ordered in the `PriorityQueue` using:

$$\text{PriorityScore} = (\text{UrgencyWeight} \times 40) + (\text{PeopleAffected} \times 0.35) + (\text{WaitingHours} \times 2.0) + \text{DisasterSeverityBonus}$$

- **Urgency Weights**: CRITICAL = 10 (400 pts), HIGH = 7 (280 pts), MEDIUM = 4 (160 pts), LOW = 1 (40 pts).
- **Disaster Severity Bonus**: CRITICAL = +25, HIGH = +15, MEDIUM = +5, LOW = 0.

---

## 4. Quick Start & Execution

### Option A: 1-Click Embedded Server (Recommended for Viva/Evaluations)
1. Double-click `run.bat` or run in terminal:
   ```cmd
   run.bat
   ```
2. Open your browser and navigate to:
   ```
   http://localhost:8080
   ```

### Option B: Build Standard WAR Package (for Apache Tomcat 9)
```cmd
build.bat
```
The deployable WAR file is generated at `target/disaster-relief-coordinator.war`.

---

## 5. Demo Credentials (1-Click Fill on Login Screen)

| Role | Email | Password | Access Capabilities |
| :--- | :--- | :--- | :--- |
| **Coordinator (Admin)** | `admin@relief.org` | `admin123` | Full operations control, auto-allocation, disaster registry, snapshot management |
| **Shelter Manager** | `shelter1@relief.org` | `shelter123` | Shelter status updates, emergency resource requests, delivery tracking |
| **Relief Org (Red Cross)** | `redcross@relief.org` | `org123` | Supply registration, inventory restock, warehouse tracking |
| **Volunteer (Rahul)** | `volunteer1@relief.org` | `vol123` | Assigned task view, status updates, delivery transit milestones |

---

## 6. Viva Q&A Quick Reference

**Q: How does the system handle race conditions when two shelters request the same scarce resource?**  
**A:** The `AllocationService` enforces a `ReentrantLock` (`INVENTORY_LOCK`) around the deduction and allocation transactions. In addition, the SQL update statement utilizes atomic condition checking (`WHERE resource_id = ? AND quantity >= ?`), ensuring that two concurrent threads cannot over-allocate or reduce inventory below zero.

**Q: Why use Java Serialization (.ser) when MySQL is already present?**  
**A:** In emergency and disaster scenarios, network connectivity to centralized database servers can be lost. Serializing the entire state container (`SystemSnapshot`) into `.ser` binary files creates self-contained operational backups that can be transferred via offline USB media or restored on field laptops independently of database servers.

**Q: Where is polymorphism implemented in the system?**  
**A:** The `User` abstract class defines abstract methods (`getRoleTitle()`, `getDashboardRoute()`, `canAllocateResources()`). Specialized subclasses (`Coordinator`, `Volunteer`, `Organization`, `ShelterUser`) provide their own implementations. When a user logs in, `UserDAO` polymorphically instantiates the appropriate subclass, and navigation routes are resolved at runtime without hardcoded role checks.
