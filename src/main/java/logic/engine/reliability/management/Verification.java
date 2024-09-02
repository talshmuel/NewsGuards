package logic.engine.reliability.management;

public enum Verification {
    Approve,
    Deny,
    Avoid,
    Pending;

    public static Verification fromInt(int i) {
        switch (i){
            case 0:
                return Verification.Approve;
            case 1:
                return Verification.Deny;
            case 2:
                return Verification.Avoid;
            case 3:
                return Verification.Pending;
            default:
                throw new IllegalArgumentException("Guard verification should be number 1-3");
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
