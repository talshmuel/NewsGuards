package newsGuardServer;
import data.transfer.object.report.NewReportDTO;
import logic.engine.Engine;
import logic.engine.report.Report;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

// ReportController.java
@RestController
@RequestMapping("/reports")
public class ReportController {
    Engine engine;
    ObjectMapper objectMapper;

    public  ReportController(Engine theEngine)
    {
        this.engine = theEngine;
    }

    @GetMapping
    public List<Report> getAllReports() {
        // Retrieve and return all Reports
    }

    @GetMapping("/{id}")
    public Report getReportById(@PathVariable Long id) {
        // Retrieve and return report by ID
    }

    @PostMapping
    public ResponseEntity<String> createReport(@RequestParam("file") MultipartFile file) {
        try {
            NewReportDTO createReportDTO = objectMapper.readValue(file.getInputStream(), NewReportDTO.class);
            boolean createReportSuccessful = engine.addNewReportAndStartVerificationProcess(createReportDTO);

            if (createReportSuccessful) {
                return ResponseEntity.ok("Create new report successful");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid report");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error reading JSON file");
        }
    }

    @PutMapping("/{id}")
    public Report updateReport(@PathVariable Long id, @RequestBody Report report) {
        // Update and return existing Report
    }

    @DeleteMapping("/{id}")
    public void deleteReport(@PathVariable Long Report) {
        // Delete Report by ID
    }
}
