import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Unit tests for MonthlySubscription and YearlySubscription
 * @author Ramsha
 * @date March 18 2026
 * @tutorial T01
 */



//--------------------------MonthlySubscription Tests----------------------------------

public class SubscriptionTest {

    @Test
    void monthlySubscription_getMonthlyCost() {
        // monthly cost should equal cost directly
        MonthlySubscription sub = new MonthlySubscription("Netflix", 15.99, "Entertainment", LocalDate.of(2025, 3, 1), null, Status.ACTIVE);
        assertEquals(15.99, sub.getMonthlyCost(), "Monthly cost should equal cost directly");
    }
    @Test
    void monthlySubscription_getYearlyCost() {
        // yearly cost should be monthly * 12
        MonthlySubscription sub = new MonthlySubscription("Netflix", 15.99, "Entertainment", LocalDate.of(2025, 3, 1), null, Status.ACTIVE);
        assertEquals(15.99 * 12, sub.getYearlyCost(), "Yearly cost should be monthly * 12");
    }
    @Test
    void monthlySubscription_getMonthlyCost_zero() {
        //edge case(cost of 0)
        MonthlySubscription sub = new MonthlySubscription("FreeService", 0.0, "Entertainment", LocalDate.of(2025, 3, 1), null, Status.ACTIVE);
        assertEquals(0.0, sub.getMonthlyCost(), "Monthly cost of 0 should return 0");
    }
    @Test
    void monthlySubscription_getRenewal() {
        //renewal should always be Monthly for MonthlySubscription
        MonthlySubscription sub = new MonthlySubscription("Netflix", 15.99, "Entertainment", LocalDate.of(2025, 3, 1),null,Status.ACTIVE);
        assertEquals("Monthly", sub.getRenewal(), "Renewal should be Monthly");
    }
    @Test
    void monthlySubscription_isUpcoming_true() {
        //date within next 7 days should return true
        LocalDate upcoming = LocalDate.now().plusDays(3);
        MonthlySubscription sub = new MonthlySubscription("Spotify", 9.99, "Music", LocalDate.of(2025, upcoming.getMonthValue(), upcoming.getDayOfMonth()),null, Status.ACTIVE);
        assertTrue(sub.isUpcoming(), "Should be upcoming within 7 days");
    }

    @Test
    void monthlySubscription_isUpcoming_false() {
        //date more than 7 days away should return false
        LocalDate notUpcoming = LocalDate.now().plusDays(20);
        MonthlySubscription sub = new MonthlySubscription("Spotify", 9.99, "Music", LocalDate.of(2025, notUpcoming.getMonthValue(), notUpcoming.getDayOfMonth()),null, Status.ACTIVE);
        assertFalse(sub.isUpcoming(), "Should not be upcoming - more than 7 days away");
    }
    @Test
    void monthlySubscription_isUpcoming_today() {
        // renewal is today - should still be upcoming
        LocalDate today = LocalDate.now();
        MonthlySubscription sub = new MonthlySubscription("Spotify", 9.99, "Music", LocalDate.of(2025, today.getMonthValue(), today.getDayOfMonth()), null, Status.ACTIVE);
        assertTrue(sub.isUpcoming(), "Renewal today should count as upcoming");
    }
//    @Test
//    void monthlySubscription_toString() {
//        // toString should contain Monthly
//        MonthlySubscription sub = new MonthlySubscription("Netflix", 15.99, "Entertainment",LocalDate.of(2025, 3, 1), null, Status.ACTIVE);
//        assertTrue(sub.toString().contains("Monthly"), "toString should contain Monthly");
//    }


    //--------------------------YearlySubscription Tests----------------------
    @Test
    void yearlySubscription_getMonthlyCost() {
        //monthly cost should be yearly/12
        YearlySubscription sub = new YearlySubscription("Adobe", 120.00, "Work",LocalDate.of(2025, 3, 1), null, Status.ACTIVE);
        assertEquals(10.00, sub.getMonthlyCost(), "Monthly cost should be yearly / 12");
    }
    @Test
    void yearlySubscription_getMonthlyCost_notDivisible() {
        //edge case - cost not evenly divisible by 12
        YearlySubscription sub = new YearlySubscription("Adobe",100.00, "Work",LocalDate.of(2025, 3, 1), null, Status.ACTIVE);
        assertEquals(100.00 / 12, sub.getMonthlyCost(), "Monthly cost should be 100/12");
    }

    @Test
    void yearlySubscription_getYearlyCost() {
        //yearly cost should equal cost directly
        YearlySubscription sub = new YearlySubscription("Adobe",120.00, "Work",LocalDate.of(2025, 3, 1), null, Status.ACTIVE);
        assertEquals(120.00, sub.getYearlyCost(), "Yearly cost should equal cost directly");
    }
    @Test
    void yearlySubscription_getRenewal() {
        //renewal should always be yearly for YearlySubscription
        YearlySubscription sub = new YearlySubscription("Adobe",120.00,"Work",LocalDate.of(2025, 3, 1), null, Status.ACTIVE);
        assertEquals("Yearly", sub.getRenewal(), "Renewal should be Yearly");
    }
//    @Test
//    void yearlySubscription_toString() {
//        //toString should contain Yearly
//        YearlySubscription sub = new YearlySubscription("Adobe",120.00,"Work",LocalDate.of(2025, 3, 1), null, Status.ACTIVE);
//        assertTrue(sub.toString().contains("Yearly"), "toString should contain Yearly");
//    }

    //--------------------------Extra Inheritance Tests----------------------

    @Test
    void monthlySubscription_isInstanceOfSubscription() {
        //monthlySubscription should be instance of Subscription
        MonthlySubscription sub = new MonthlySubscription("Netflix", 15.99, "Entertainment", LocalDate.of(2025, 3, 1), null, Status.ACTIVE);
        assertTrue(sub instanceof Subscription, "MonthlySubscription should be instance of Subscription");
    }

    @Test
    void yearlySubscription_isInstanceOfSubscription() {
        //yearlySubscription should be instance of Subscription
        YearlySubscription sub = new YearlySubscription("Adobe", 120.00, "Work", LocalDate.of(2025, 3, 1), null, Status.ACTIVE);
        assertTrue(sub instanceof Subscription, "YearlySubscription should be instance of Subscription");
    }

    //------------------------------------------- Status Enum Tests ---------------------------------------------------------------//

    @Test
    void status_getStatus_returnsCorrectEnum() {
        assertEquals(Status.ACTIVE, Status.getStatus("ACTIVE"), "getStatus(ACTIVE) should return Status.ACTIVE");
        assertEquals(Status.PAUSED, Status.getStatus("PAUSED"), "getStatus(PAUSED) should return Status.PAUSED");
        assertEquals(Status.CANCELLED, Status.getStatus("CANCELLED"), "getStatus(CANCELLED) should return Status.CANCELLED");
    }

    @Test
    void status_getStatus_caseInsensitive() {
        assertEquals(Status.ACTIVE, Status.getStatus("active"), "Should accept lowercase");
        assertEquals(Status.PAUSED, Status.getStatus("Paused"), "Should accept mixed case");
    }

    @Test
    void status_getStatus_invalidThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Status.getStatus("EXPIRED"), "Invalid status should throw IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> Status.getStatus(""), "Empty string should throw IllegalArgumentException");
    }

    @Test
    void status_toString_returnsCorrectString() {
        assertEquals("ACTIVE", Status.ACTIVE.toString(), "ACTIVE toString should return 'ACTIVE'");
        assertEquals("PAUSED", Status.PAUSED.toString(), "PAUSED toString should return 'PAUSED'");
        assertEquals("CANCELLED", Status.CANCELLED.toString(), "CANCELLED toString should return 'CANCELLED'");
    }

    //------------------------------------------------ Subscription equals() Test ----------------------------------------------------//

    @Test
    void subscription_equals_sameNameIsEqual() {
        Subscription s1 = new MonthlySubscription("Netflix", 15.99, "Entertainment", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        Subscription s2 = new MonthlySubscription("Netflix", 9.99, "Fitness", LocalDate.of(2025, 1, 1), null, Status.PAUSED);
        assertEquals(s1, s2, "Same name subscriptions should be equal regardless of other fields");
    }

    @Test
    void subscription_equals_differentNameNotEqual() {
        Subscription s1 = new MonthlySubscription("Netflix", 15.99, "Entertainment", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        Subscription s2 = new MonthlySubscription("Spotify", 15.99, "Entertainment", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        assertNotEquals(s1, s2, "Different name subscriptions should not be equal");
    }

    @Test
    void subscription_equals_caseInsensitive() {
        //Tests equals() is case insensitive
        Subscription s1 = new MonthlySubscription("netflix", 15.99, "Entertainment", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        Subscription s2 = new MonthlySubscription("NETFLIX", 15.99, "Entertainment", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        assertEquals(s1, s2, "equals() should be case insensitive");
    }

    // --------------------------------------------- Subscription compareTo() Test ----------------------------------------------------------------------------

    @Test
    void subscription_compareTo_activeSortBeforePaused() {
        //Tests active subscriptions go before paused
        Subscription active = new MonthlySubscription("Spotify", 9.99, "Entertainment", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        Subscription paused = new MonthlySubscription("Adobe", 50.00, "Software", LocalDate.of(2025, 1, 1), null, Status.PAUSED);
        assertTrue(active.compareTo(paused) < 0, "ACTIVE should sort before PAUSED");
    }

    @Test
    void subscription_compareTo_pausedSortBeforeCancelled() {
        //Tests paused subscriptions go before cancelled
        Subscription paused = new MonthlySubscription("Netflix", 15.99, "Entertainment", LocalDate.of(2025, 1, 1), null, Status.PAUSED);
        Subscription cancelled = new MonthlySubscription("Spotify", 15.99, "Entertainment", LocalDate.of(2025, 1, 1), null, Status.CANCELLED);
        assertTrue(paused.compareTo(cancelled) < 0, "PAUSED should sort before CANCELLED");
    }

    @Test
    void subscription_compareTo_sameStatusSortAlphabetically() {
        //Tests same status is stored in alphabetical order
        Subscription s1 = new MonthlySubscription("Adobe", 50.00, "Software", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        Subscription s2 = new MonthlySubscription("Netflix", 15.99, "Entertainment", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        assertTrue(s1.compareTo(s2) < 0, "Adobe should sort before Netflix alphabetically within same status");
    }




}