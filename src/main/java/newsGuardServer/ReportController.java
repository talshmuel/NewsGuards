package newsGuardServer;
import data.transfer.object.location.LocationDTO;
import data.transfer.object.report.CommentDTO;
import data.transfer.object.report.NewReportDTO;
import logic.engine.Engine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "http://localhost:5173")

// ReportController.java
@RestController
@RequestMapping("/report")
public class ReportController {
    Engine engine;

    @Autowired
    public  ReportController(Engine engine)
    {
        this.engine = engine;
    }

    @PostMapping("/add-new-report")
    public ResponseEntity<String> createReport(@RequestBody NewReportDTO newReportDTO) {
        try {
            engine.addNewReportAndStartVerificationProcess(newReportDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Report created successfully");
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return ResponseEntity.ok("Like added / removed successfully");
    }
}






