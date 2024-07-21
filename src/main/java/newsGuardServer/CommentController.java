package newsGuardServer;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.transfer.object.report.CommentDTO;
import logic.engine.Engine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

// UserController.java
@RestController
@RequestMapping("/comment")

public class CommentController {////
    private final Engine engine;
    ObjectMapper objectMapper;

    @Autowired
    public CommentController(Engine engine) {
        this.engine = engine;
        ObjectMapper objectMapper = new ObjectMapper();
    }

    @PostMapping("report/comment")
    public ResponseEntity<String> comment(@RequestParam("file") MultipartFile file) {
        try {
            CommentDTO commentDTO = objectMapper.readValue(file.getInputStream(), CommentDTO.class);
            engine.addCommentToReport(commentDTO);
            return ResponseEntity.ok("register successful");
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}


