package newsGuardServer;

public enum DatabaseConfig {
    POSTGRESQL("jdbc:postgresql://news-guard-proj.chayi2k0mcar.eu-north-1.rds.amazonaws.com/news-guard", "postgres", "12345678");

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
