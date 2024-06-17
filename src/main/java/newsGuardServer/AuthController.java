package newsGuardServer;

import data.transfer.object.LoginDTO;
import logic.engine.Engine;
import com.server.application.dto.UserLoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

// UserController.java
@RestController
@RequestMapping("/auth")
public class AuthController {/////
    Engine engine;

    public AuthController(Engine engine) {
        this.engine = engine;
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam("file") MultipartFile file) {
        try {
            LoginDTO userLoginDTO = objectMapper.readValue(file.getInputStream(), LoginDTO.class);
            boolean loginSuccessful = engine.checkLoginDetails(userLoginDTO);

            if (loginSuccessful) {
                return ResponseEntity.ok("Login successful");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error reading JSON file");
        }
    }
}


