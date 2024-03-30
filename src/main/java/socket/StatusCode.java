package socket;

public enum StatusCode {
    EXIT(-1),
    OK(0),
    NOT_FOUND(4),
    INTERNAL_SERVER_ERROR(5);

    private int code;

    StatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static StatusCode fromCode(int code) {
        for (StatusCode status : StatusCode.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return StatusCode.INTERNAL_SERVER_ERROR;
    }


}
