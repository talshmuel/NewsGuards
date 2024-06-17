package logic.engine.reliability.management;

public enum Rate {

    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5);

    private final int value;

    Rate(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    public static Rate convertIntToRate(int intRate){
        switch (intRate){
            case 1:
                return ONE;
            case 2:
                return TWO;
            case 3:
                return THREE;
            case 4:
                return FOUR;
            case 5:
                return FIVE;
            default:
                return null;
            //todo throw exception
        }
    }

}
