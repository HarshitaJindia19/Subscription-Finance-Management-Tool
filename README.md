# CPSC 233 - Subscription Manager
### Team Members: Harshita Jindia, Ramsha Oad, Mehrin Chowdhury
### Tutorial: T01
### Course: CPSC 233 - Introduction to Computer Science for CS Majors II
### Date: April 2026

---

## Project Description

Subscription Manager is a JavaFX-based desktop application that helps users track 
and manage their subscription services (e.g. Netflix, Spotify, Adobe). Users can 
add, update, and view subscriptions, set category budgets, receive spending alerts, 
and save/load data to CSV files through a graphical interface.

---

## Authors

| Name | GitHub |
|---|---|
| Harshita Jindia | @harshitajindia |
| Ramsha Oad | @r.oad |
| Mehrin Chowdhury | @mehrin.chowdhury |

---

## Features

<img width="1494" height="920" alt="Subscription Tool Dashboard" src="https://github.com/user-attachments/assets/341579f9-8183-4f7b-a26f-43213a80d4b5" />

### Data Management
- Add a new subscription (name, cost, category, billing cycle, start date, end date, status)
- Update any subscription field with input validation
- Add a monthly budget limit by category

### General Output
- View all tracked subscriptions in a formatted table
- View active subscriptions
- View paused subscriptions

### Special Output
- Total monthly spend across all active subscriptions
- Total annual spend across all active subscriptions
- Upcoming bills (subscriptions renewing within the next 7 days)
- Budget % used by category
- High-expense bills (subscriptions over a set threshold)
- Budget alerts (when spending exceeds category budget limit)
- Search subscriptions by name or category

### File I/O
- Save subscription and budget data to a `.csv` file
- Load subscription and budget data from a `.csv` file
- Run with a command line argument to load a saved CSV on startup

---

## OO Design

| Class | Purpose |
|---|---|
| `Subscription` | Base class with all shared subscription fields and behaviour |
| `MonthlySubscription` | Extends Subscription — handles monthly billing logic |
| `YearlySubscription` | Extends Subscription — handles yearly billing logic |
| `SubscriptionManager` | Manages all subscriptions and budgets, implements Searchable |
| `Budget` | Stores category budget information |
| `Status` | Enum for subscription status (ACTIVE, PAUSED, CANCELLED) |
| `FileHandler` | Handles CSV save and load |

---

## How to Run (.jar file)

1. Download `SubscriptionManager.jar` from the repository
2. Run from command line:
3. To load a previously saved CSV file on startup:

---

## How to Run from Source

1. Clone the repository:
2. Open the project in IntelliJ IDEA
3. Add JavaFX SDK to project libraries
4. Run `Main.java`

---

## How to Run Tests

Open `SubscriptionManagerTest.java` or `SubscriptionTest.java` in IntelliJ and 
click the green run button. Requires JUnit 5.

---

## Git Workflow

- Each member worked in feature branches before merging into `main`
- Branches: `feature-main-menu`, `feature-update-logic`, `feature-subscription-classes`,
  `feature-special-outputs`, `feature-search`, `add_data_logic`, `data_storage`, 
  `math_logic`, `SearchEngine`
- All merges performed via `git merge` into `main`
