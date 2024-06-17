package logic.engine.report;

public enum Genre {
    TECHNOLOGY,
    HEALTH,
    LIFESTYLE,
    EDUCATION,
    ENTERTAINMENT,
    SPORTS;

    public static Genre convertStringToGenre(String strGenre) {
        switch (strGenre) {
            case "TECHNOLOGY":
                return TECHNOLOGY;
            case "HEALTH":
                return HEALTH;
            case "LIFESTYLE":
                return LIFESTYLE;
            case "EDUCATION":
                return EDUCATION;
            case "ENTERTAINMENT":
                return ENTERTAINMENT;
            case "SPORTS":
                return SPORTS;
            default:
                //todo throw exception
                return null;
        }
    }
}
