package newsGuardServer;

import data.transfer.object.user.NewUserDTO;
import logic.engine.Engine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")

@RestController
@RequestMapping("/register")
public class RegisterController {
    private final Engine engine;

    @Autowired
    public RegisterController(Engine engine) {
        this.engine = engine;
    }
    @PostMapping()
    public ResponseEntity<String> register(@RequestBody NewUserDTO newUserDTO) {
        try {
            engine.createNewUser(newUserDTO);
            return ResponseEntity.ok("register successful");
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
