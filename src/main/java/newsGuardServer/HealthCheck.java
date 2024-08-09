package newsGuardServer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/health-check")
public class HealthCheck {
    @GetMapping("/healthh")
    public ResponseEntity<String> healthh() {
        return ResponseEntity.ok("healthy");
    }

    @GetMapping("health")
    public String healthCheck(){
        return "Hello";
    }
}
