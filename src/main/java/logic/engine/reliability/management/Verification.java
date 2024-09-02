package logic.engine.reliability.management;

public enum Verification {
    Approve,
    Deny,
    Avoid,
    Pending;

    public static Verification fromInt(int i) {
        switch (i){
            case 1:
                return Verification.Approve;
            case 2:
                return Verification.Deny;
            case 3:
                return Verification.Avoid;
            case 4:
                return Verification.Pending;
            default:
                throw new IllegalArgumentException("Guard verification should be number 1-3");
        }
    }





    public int toInt() {
        switch (this) {
            case Approve:
                return 1;
            case Deny:
                return 2;
            case Avoid:
                return 3;
            case Pending:
                return 4;
            default:
                throw new IllegalArgumentException("Unexpected value");
        }
    }
}
