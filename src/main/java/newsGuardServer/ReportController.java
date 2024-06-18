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

    public  ReportController(Engine engine)
    {
        this.engine = engine;
    }

    @GetMapping
    public List<Report> getAllReports() {
        // Retrieve and return all Reports
    }

    @GetMapping("/{id}")
    public Report getReportById(@PathVariable Long id) {
        // Retrieve and return report by ID
    }

    @PostMapping("/create")
    public ResponseEntity<String> createReport(@RequestParam("file") MultipartFile file) {
        try {
            NewReportDTO newReportDTO = objectMapper.readValue(file.getInputStream(), NewReportDTO.class);
            engine.addNewReportAndStartVerificationProcess(newReportDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Report created successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public Report updateReport(@PathVariable Long id, @RequestBody Report report) {
        // Update and return existing Report
        return;
    }
    @DeleteMapping("/{id}")
    public void deleteReport(@PathVariable Long Report) {
        // Delete Report by ID
    }
}






