package newsGuardServer;
import data.transfer.object.LoginDTO;
import data.transfer.object.LoginResponseDTO;
import logic.engine.Engine;
import logic.engine.exception.InvalidPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173") // Your frontend origin

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
            LoginResponseDTO response = engine.checkLoginDetails(userLoginDTO);
            return ResponseEntity.ok(response);

        } catch (InvalidPasswordException | IllegalArgumentException e) {
            LoginResponseDTO response = new LoginResponseDTO(e.getMessage(), null, null);
            System.out.print(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        } catch (Exception e) {
            System.out.print(e.getMessage());
            LoginResponseDTO response = new LoginResponseDTO(e.getMessage(), null, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


}


