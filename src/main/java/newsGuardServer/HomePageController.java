package newsGuardServer;

import data.transfer.object.LoginDTO;
import data.transfer.object.LoginResponseDTO;
import data.transfer.object.report.ReportDTO;
import logic.engine.Engine;
import logic.engine.exception.InvalidPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@CrossOrigin(origins = "https://news-guard.vercel.app")
//@CrossOrigin(origins = "http://localhost:5173")

@RestController
@RequestMapping("/home-page")
public class HomePageController {
    private final Engine engine;

    @Autowired
    public HomePageController(Engine engine) {
        this.engine = engine;
    }

    @GetMapping("/get-last-twenty-reports")
    public ResponseEntity<ArrayList<ReportDTO>> getLastTwentyReportsToHomePage() {
        try {
            ArrayList<ReportDTO> response= engine.getLastTwentyReportsToHomePage();
            return ResponseEntity.ok(response);

        } catch (InvalidPasswordException | IllegalArgumentException e) {
            ArrayList<ReportDTO> response = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        } catch (Exception e) {
            ArrayList<ReportDTO> response = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
