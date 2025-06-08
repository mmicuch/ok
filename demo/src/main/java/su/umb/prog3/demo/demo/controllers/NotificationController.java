package su.umb.prog3.demo.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import su.umb.prog3.demo.demo.persistence.Services.NotificationService;
import su.umb.prog3.demo.demo.persistence.dto.NotifikaciaDTO;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:4200")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<NotifikaciaDTO>> getUpcoming() {
        return ResponseEntity.ok(notificationService.getNadchadzajuceNotifikacie());
    }

    @GetMapping("/next-days/{days}")
    public ResponseEntity<List<NotifikaciaDTO>> getNextDays(@PathVariable int days) {
        return ResponseEntity.ok(notificationService.getNotifikacieVNasledujucichDnoch(days));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<NotifikaciaDTO>> getOverdue() {
        return ResponseEntity.ok(notificationService.getPrekroceneTerminy());
    }

    @GetMapping("/stats")
    public ResponseEntity<NotificationService.NotificationStatsDTO> getStats() {
        return ResponseEntity.ok(notificationService.getNotificationStats());
    }
}
