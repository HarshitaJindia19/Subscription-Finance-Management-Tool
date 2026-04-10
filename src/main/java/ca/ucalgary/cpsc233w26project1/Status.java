package ca.ucalgary.cpsc233w26project1;

/**
 * Status.java - enum for valid subscription status options
 * @author Harshita
 */
public enum Status {

    // 3 types of subscription status
    ACTIVE, PAUSED, CANCELLED;

    /**
     * Helper to look up matching status based on a String
     * @param status the string to match
     * @return the enum type of the status
     */
    public static Status getStatus(String status) {

        if (status.equalsIgnoreCase("ACTIVE")) {
            return ACTIVE;
        } else if (status.equalsIgnoreCase("PAUSED")) {
            return PAUSED;
        } else if (status.equalsIgnoreCase("CANCELLED")) {
            return CANCELLED;
        } else {
            throw new IllegalArgumentException("Status " + status
                    + " is not valid! Must be ACTIVE/PAUSED/CANCELLED");
        }
    }
}
