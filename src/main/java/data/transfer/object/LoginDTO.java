package data.transfer.object;

public final class LoginDTO {
    String email;
    String passwordToCheck;

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
