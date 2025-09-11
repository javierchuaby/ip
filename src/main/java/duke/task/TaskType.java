package duke.task;

/**
 * Enum representing the different types of tasks in the Duke system.
 * Provides type safety and eliminates magic strings for task type codes.
 */
public enum TaskType {
    TODO("T", "Todo"),
    DEADLINE("D", "Deadline"),
    EVENT("E", "Event");

    private final String storageCode;
    private final String displayName;

    /**
     * Constructs a TaskType with storage code and display name.
     *
     * @param storageCode The single character code used in storage files
     * @param displayName The human-readable name for this task type
     */
    TaskType(String storageCode, String displayName) {
        this.storageCode = storageCode;
        this.displayName = displayName;
    }

    /**
     * Gets the storage code for this task type.
     *
     * @return The single character code used in storage files
     */
    public String getStorageCode() {
        return storageCode;
    }

    /**
     * Gets the display name for this task type.
     *
     * @return The human-readable name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Converts a storage code string to the corresponding TaskType.
     *
     * @param code The string code from storage file
     * @return The matching TaskType enum value
     * @throws IllegalArgumentException if the code is not recognized
     */
    public static TaskType fromStorageCode(String code) {
        for (TaskType type : values()) {
            if (type.storageCode.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown task type code: " + code);
    }
}
