package newsGuardServer;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.transfer.object.LoginDTO;
import data.transfer.object.user.NewUserDTO;
import logic.engine.Engine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

// UserController.java
@RestController
@RequestMapping("/auth")

public class AuthController {////
    private final Engine engine;
     ObjectMapper objectMapper;

    @Autowired
    public AuthController(Engine engine) {
        this.engine = engine;
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
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam("file") MultipartFile file) {
        try {
            NewUserDTO newUserDTO = objectMapper.readValue(file.getInputStream(), NewUserDTO.class);
            engine.createNewUser(newUserDTO);
            return ResponseEntity.ok("register successful");
            }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}


