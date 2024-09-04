package data.transfer.object;

public class LoginResponseDTO {
    private String message;
    private String userFullName;
    private Integer userId;

    public LoginResponseDTO(String message, Integer userId, String userFullName) {
        this.message = message;
        this.userId = userId;
        this.userFullName = userFullName;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public String getUserFullName() {
        return userFullName;
    }
}
