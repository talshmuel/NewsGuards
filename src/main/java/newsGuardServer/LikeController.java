package newsGuardServer;

import logic.engine.Engine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports/{reportId}/likes")
public class LikeController {
    Engine engine;

    @Autowired
    public LikeController(Engine engine) {
        this.engine = engine;
    }

    @GetMapping("/likeOrUnlikeReport")
    public ResponseEntity<String> likeOrUnlikeReport(
        @RequestParam("reportID") int reportID,
        @RequestParam("userID") int userID) {
        engine.addOrRemoveLikeToReport(reportID, userID);
        return ResponseEntity.ok("successful");
    }
}
