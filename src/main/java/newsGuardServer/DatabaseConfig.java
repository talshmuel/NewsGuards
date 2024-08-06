package newsGuardServer;

public enum DatabaseConfig {
    POSTGRESQL("jdbc:postgresql://localhost:5432/postgres", "postgres", "4Xkp61jk!4Xkp61jk!");

    private final String url;
    private final String username;
    private final String password;

    DatabaseConfig(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
