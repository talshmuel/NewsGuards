package newsGuardServer;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.transfer.object.LoginDTO;
import logic.engine.Engine;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

// UserController.java
@RestController
@RequestMapping("/auth")
public class AuthController {////
    Engine engine;
    ObjectMapper objectMapper;

    public AuthController(Engine theEngine) {
        this.engine = theEngine;
        ObjectMapper objectMapper = new ObjectMapper();
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


