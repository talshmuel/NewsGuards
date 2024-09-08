package logic.engine.reliability.management;

public enum Verification {
    Approve,
    Deny,
    Dont_Know,
    Pending,
    No_Answer;

    public static Verification fromInt(int i) {
        switch (i){
            case 1:
                return Verification.Approve;
            case 2:
                return Verification.Deny;
            case 3:
                return Verification.Dont_Know;
            case 4:
                return Verification.Pending;
            case 5:
                return Verification.No_Answer;
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
            case Dont_Know:
                return 3;
            case Pending:
                return 4;
            case No_Answer:
                return 5;
            default:
                throw new IllegalArgumentException("Unexpected value");
        }
    }
}
