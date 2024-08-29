package logic.engine.reliability.management;

public enum Verification {
    Approve,
    Deny,
    Avoid,
    Pending;

    public static Verification fromInt(int i) {
        switch (i) {
            case 0:
                return Approve;
            case 1:
                return Deny;
            case 2:
                return Avoid;
            case 3:
                return Pending;
            default:
                throw new IllegalArgumentException("Unexpected value: " + i);
        }
    }

    public int toInt() {
        switch (this) {
            case Approve:
                return 0;
            case Deny:
                return 1;
            case Avoid:
                return 2;
            case Pending:
                return 3;
            default:
                throw new IllegalArgumentException("Unexpected value");
        }
    }
}
