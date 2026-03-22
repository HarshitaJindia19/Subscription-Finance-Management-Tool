# CPSC 233 - Subscription Manager
### Team Members: Harshita, Ramsha, Mehrin
### Tutorial: T01
### Date: February 2026

---

## Project Description

Subscription Manager is a Java-based command-line application that helps users track and manage their subscription services (e.g. Netflix, Spotify, YouTube Premium). Users can add, update, and view subscriptions, set category budgets, and receive alerts when spending exceeds limits.

---

## Features

### Add SubscriptionManager
- Add a new subscription (name, cost, category, billing cycle, start date, end date, status)
- Add a monthly budget limit by category

### Update SubscriptionManager
- Change subscription status (ACTIVE, PAUSED, CANCELLED)
- Update any subscription field (name, cost, category, billing cycle, dates, status)

### General Output
- View all tracked subscriptions in a formatted table
- View all active subscriptions

### Special Output
- Calculate total monthly spend across all active subscriptions
- Calculate total annual spend across all active subscriptions
- List subscriptions renewing in the next 7 days (upcoming bills)
- List paused subscriptions
- Show percentage of budget used for a specific category
- List high-expense bills (subscriptions over a set amount)
- Budget alert (alert when total spending exceeds category budget limit)
- Search subscriptions by name or category

---

## SubscriptionManager Structures Used

| Structure | Purpose |
|---|---|
| `ArrayList<ArrayList<String>>` | Stores all subscriptions, each as an inner list of 7 fields |
| `HashMap<String, Double>` | Maps category names to their budget limits |
| `HashSet<String>` | Stores unique category names and subscription names |
| `String[]` | Stores fixed status options: ACTIVE, PAUSED, CANCELLED |

### Subscription SubscriptionManager Format (Inner ArrayList)
| Index | Field |
|---|---|
| 0 | Name |
| 1 | Cost |
| 2 | Category |
| 3 | Billing Cycle (Monthly/Yearly) |
| 4 | Start Date (MM/DD/YYYY) |
| 5 | End Date (MM/DD/YYYY or N/A) |
| 6 | Status (ACTIVE/PAUSED/CANCELLED) |

---

## Project Structure
```
src/
├── Main.java        # Entry point - calls Menu.runMenu()
├── Menu.java        # Front-end: all Scanner input and System.out output
├── SubscriptionManager.java        # Back-end: all data storage and logic
└── SubscriptionManagerTest.java    # JUnit 5 unit tests for SubscriptionManager.java methods
```

---

## How to Run

1. Clone the repository:
```
git clone https://csgit.ucalgary.ca/mehrin.chowdhury/cpsc233w26project1.git
```
2. Open the project in IntelliJ IDEA
3. Run `Main.java`
4. Use the menu to interact with the application

---

## How to Run Tests

Open `SubscriptionManagerTest.java` in IntelliJ and click the green run button next to the class or individual test methods. Requires JUnit 5.


---

## Git Workflow

- Each member worked in feature branches before merging into `main`
- Branches: `feature-main-menu`, `feature-update-logic`, `feature-special-outputs`, `feature-search`, `add_data_logic`, `data_storage`, `math_logic`, `SearchEngine`
- All merges performed via `git merge` into `main`