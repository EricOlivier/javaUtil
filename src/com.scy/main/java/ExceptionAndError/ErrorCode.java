package ExceptionAndError;

/**
 * Created by SCY on 16/10/20.
 */

public enum ErrorCode {
    OK(0, "Success"),
    GeneralWarning(100, "Warning", false),
    WrongParameters(200, "参数错误", false),
    AuthenticationFailure(201, "Authentication failure", false),
    JsonFormatError(202, "Json格式错误"),
    InternalDataMissing(203, "内部逻辑中数据传递查找时丢失"),
    GeneralError(299, "系统内部错误"),

    // Deprecated error code
    //	GeneralDatabaseError(400, "Database error"),


    GenericError(500, "Generic error");


    private final int code;
    private final String description;
    private final boolean sendEmail;

    private ErrorCode(int code, String description) {
        this(code, description, true);
    }

    private ErrorCode(int code, String description, boolean sendEmail) {
        this.code = code;
        this.description = description;
        this.sendEmail = sendEmail;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    public boolean shouldSendEmail() {
        return this.sendEmail;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }

    public static ErrorCode getByCode(int code) {

        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.getCode() == code) {
                return errorCode;
            }
        }
        return null;
    }
}
