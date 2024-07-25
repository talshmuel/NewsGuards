package newsGuardServer;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.transfer.object.LoginDTO;
import data.transfer.object.LoginResponseDTO;
import data.transfer.object.user.NewUserDTO;
import logic.engine.Engine;
import logic.engine.exception.InvalidPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final Engine engine;

    @Autowired
    public LoginController(Engine engine) {
        this.engine = engine;
    }

    @PostMapping()
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody LoginDTO userLoginDTO) {
        try {
            Integer userID = engine.checkLoginDetails(userLoginDTO);

            LoginResponseDTO response = new LoginResponseDTO("Login successful", userID);
            return ResponseEntity.ok(response);

        } catch (InvalidPasswordException | IllegalArgumentException e) {
            LoginResponseDTO response = new LoginResponseDTO(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        } catch (Exception e) {
            LoginResponseDTO response = new LoginResponseDTO(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


}


