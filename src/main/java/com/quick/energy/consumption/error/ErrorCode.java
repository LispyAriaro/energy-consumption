package com.quick.energy.consumption.error;

/**
 * Contains the list of error codes defined by the service.
 * <p>
 * New error codes should be added at the bottom of the last error code otherwise existing semantics will be altered.
 *
 * @author efe ariaroo
 */
public enum ErrorCode {
    SERVER_ERROR,
    REMOTE_SERVER_ERROR;


    private static final int OFFSET = 5; // First error code begins with this value.
    private static final String PREFIX = "Err";
    private static final int WIDTH = 3;
    private static final String FORMAT = String.format("%s%%0%dd", PREFIX, WIDTH);

    public String getCode() {
        return String.format(FORMAT, OFFSET + ordinal());
    }

    public static ErrorCode fromCode(String code) {
        if (code.length() != PREFIX.length() + WIDTH || !code.startsWith(PREFIX))
            throw new RuntimeException("Invalid code " + code);
        try {
            int ordinal = Integer.parseInt(code.substring(PREFIX.length())) - OFFSET;
            for (ErrorCode localErrorCode : values())
                if (localErrorCode.ordinal() == ordinal)
                    return localErrorCode;
            throw new RuntimeException("Numeric code not in enum range " + code);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid numeric value in code " + code);
        }
    }
}
