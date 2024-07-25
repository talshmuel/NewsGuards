package data.transfer.object;

public class LoginResponseDTO {
    private String message;
    private Integer userId;

    public LoginResponseDTO(String message, Integer userId) {
        this.message = message;
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

}
