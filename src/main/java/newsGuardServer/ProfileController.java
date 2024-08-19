package newsGuardServer;

import data.transfer.object.user.UserDTO;
import logic.engine.Engine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/profile-page")
public class ProfileController {
    private final Engine engine;

    @Autowired
    public ProfileController(Engine engine) {
        this.engine = engine;
    }

    @GetMapping("/get-profile")
    public ResponseEntity<UserDTO> getUserProfile(@RequestParam int userID) {
        try {
            UserDTO response = engine.getUserProfile(userID);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
