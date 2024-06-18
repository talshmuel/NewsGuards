package newsGuardServer;

import logic.engine.Engine;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports/{reportId}/likes")
public class LikeController {
    Engine engine;

    public LikeController(Engine engine) {
        this.engine = engine;
    }

    @GetMapping("/likePost")
    public ResponseEntity<String> likePost(
            @RequestParam("reportID") int reportID,
            @RequestParam("userID") int userID) {

    }

    @DeleteMapping("/unlikePost")
    public void unlikePost( @RequestParam("reportID") int reportID,
                            @RequestParam("userID") int userID) {
        // Unlike a specific report
    }
}
