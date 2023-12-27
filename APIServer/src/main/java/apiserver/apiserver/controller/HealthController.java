package apiserver.apiserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("health")
public class HealthController {

    @GetMapping("/check")
    public ResponseEntity<String> health() {
        // Add custom logic to check if the application is ready
        // Return a 200 OK response when ready, or a different status code if not ready
        if (isApplicationReady()) {
            return ResponseEntity.ok("Application is healthy");
        } else {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Application is not ready");
        }
    }

    private boolean isApplicationReady() {
        // Implement your custom logic here to check if the application is ready
        // Return true when ready, false if not
        return true;
    }
}