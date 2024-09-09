package newsGuardServer;
import data.transfer.object.report.CommentDTO;
import data.transfer.object.report.NewReportDTO;
import logic.engine.Engine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "https://news-guard.vercel.app")

// ReportController.java
@RestController
@RequestMapping("/report")
public class ReportController {
    Engine engine;

    private final String uploadDir = "images/"; // GON: Directory to save uploaded images

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @Autowired
    public  ReportController(Engine engine)
    {
        this.engine = engine;
    }

    @PostMapping("/add-new-report")
    public ResponseEntity<String> createReport(@RequestBody NewReportDTO newReportDTO) {
        try {
            System.out.println(newReportDTO.getIsAnonymousReport());
            engine.addNewReportAndStartVerificationProcess(newReportDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Report created successfully");
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PostMapping("/upload-image")
    public Map<String, String> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        try {
            if (file.isEmpty()) {
                response.put("message", "File is empty");
                return response;
            }

            String fileName = file.getOriginalFilename();
            byte[] content = file.getBytes();
            String imageUrl = firebaseStorageService.uploadImage(fileName, content);

            response.put("imageUrl", imageUrl);
            return response;
        } catch (IOException e) {
            response.put("message", "Image upload failed");
            return response;
        }
    }

    @PostMapping("/add-comment")
    public ResponseEntity<String> comment(@RequestBody CommentDTO commentDTO) {
        try {
            engine.addCommentToReport(commentDTO);
            return ResponseEntity.ok("Comment successfully added");
        }catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/like-or-unlike")
    public ResponseEntity<String> likeOrUnlikeReport(
            @RequestParam("reportID") int reportID,
            @RequestParam("userID") int userID) {
        try {
            engine.addOrRemoveLikeToReport(reportID, userID);
            return ResponseEntity.ok("Like added / removed successfully");
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
}





