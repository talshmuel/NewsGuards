package data.transfer.object;

public final class LoginDTO {
    private final String email;
    private final String passwordToCheck;

    public LoginDTO(String email, String password) {
        this.email = email;
        this.passwordToCheck = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordToCheck() {
        return passwordToCheck;
    }
}
