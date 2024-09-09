package newsGuardServer;

import data.transfer.object.report.ReportDTO;
import logic.engine.Engine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.NoSuchElementException;


@CrossOrigin(origins = "https://news-guard.vercel.app")
//@CrossOrigin(origins = "http://localhost:5173")

@RestController
@RequestMapping("/verification")
public class VerificationController {
    private final Engine engine;

    @Autowired
    public VerificationController(Engine engine) {
        this.engine = engine;
    }

    @GetMapping("/get-reports-that-guard-need-to-verify")
    public ResponseEntity<ArrayList<ReportDTO>> getReportsThatGuardNeedToVerify(@RequestParam int guardID) {
        try {
            ArrayList<ReportDTO> response = engine.getReportsThatGuardNeedToVerify(guardID);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ArrayList<ReportDTO> response = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        } catch (Exception e) {
            ArrayList<ReportDTO> response = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @PutMapping("/update-guard-verification")
    public ResponseEntity<String> updateGuardVerification(
            @RequestParam("reportID") int reportID,
            @RequestParam("guardID") int guardID,
            @RequestParam("verification") int verification) {//1 - APPROVE, 2 - DENY, 3 - AVOID
        try {
            engine.updateGuardVerification(reportID, guardID, verification);
        }catch (NoSuchElementException | IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok("Guard verification received successfully");
    }
}

