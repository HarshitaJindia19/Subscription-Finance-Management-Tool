import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

/**
 * Unit tests for MonthlySubscription and YearlySubscription
 * @author Ramsha
 * @date March 18 2026
 * @tutorial T01
 */
public class SubscriptionTest {
    @Test
    void monthlySubscription_getMonthlyCost() {
        // monthly cost should equal cost directly
        MonthlySubscription sub = new MonthlySubscription("Netflix", 15.99, "Entertainment", LocalDate.of(2025, 3, 1), null, "ACTIVE");
        assertEquals(15.99, sub.getMonthlyCost(), "Monthly cost should equal cost directly");
    }
    @Test
    void monthlySubscription_getYearlyCost() {
        // yearly cost should be monthly * 12
        MonthlySubscription sub = new MonthlySubscription("Netflix", 15.99, "Entertainment", LocalDate.of(2025, 3, 1), null, "ACTIVE");
        assertEquals(15.99 * 12, sub.getYearlyCost(), "Yearly cost should be monthly * 12");
    }
    @Test
    void monthlySubscription_getMonthlyCost_zero() {
        //edge case(cost of 0)
        MonthlySubscription sub = new MonthlySubscription("FreeService", 0.0, "Entertainment", LocalDate.of(2025, 3, 1), null, "ACTIVE");
        assertEquals(0.0, sub.getMonthlyCost(), "Monthly cost of 0 should return 0");
    }
    @Test
    void monthlySubscription_getRenewal() {
        //renewal should always be Monthly for MonthlySubscription
        MonthlySubscription sub = new MonthlySubscription("Netflix", 15.99, "Entertainment", LocalDate.of(2025, 3, 1),null,"ACTIVE");
        assertEquals("Monthly", sub.getRenewal(), "Renewal should be Monthly");
    }
    @Test
    void monthlySubscription_isUpcoming_true() {
        //date within next 7 days should return true
        LocalDate upcoming = LocalDate.now().plusDays(3);
        MonthlySubscription sub = new MonthlySubscription("Spotify", 9.99, "Music", LocalDate.of(2025, upcoming.getMonthValue(), upcoming.getDayOfMonth()),null, "ACTIVE");
        assertTrue(sub.isUpcoming(), "Should be upcoming within 7 days");
    }

    @Test
    void monthlySubscription_isUpcoming_false() {
        //date more than 7 days away should return false
        LocalDate notUpcoming = LocalDate.now().plusDays(20);
        MonthlySubscription sub = new MonthlySubscription("Spotify", 9.99, "Music", LocalDate.of(2025, notUpcoming.getMonthValue(), notUpcoming.getDayOfMonth()),null, "ACTIVE");
        assertFalse(sub.isUpcoming(), "Should not be upcoming - more than 7 days away");
    }


}