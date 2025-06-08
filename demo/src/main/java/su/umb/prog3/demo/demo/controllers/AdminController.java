package su.umb.prog3.demo.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import su.umb.prog3.demo.demo.persistence.Services.*;
import su.umb.prog3.demo.demo.persistence.entity.*;
import su.umb.prog3.demo.demo.persistence.dto.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    private final OsobaService osobaService;
    private final VakcinaService vakcinaService;
    private final OsobaVakcinaService osobaVakcinaService;
    private final VakcinaSchemaService vakcinaSchemaService;
    private final NotificationService notificationService;
    private final AdminService adminService;

    public AdminController(
            OsobaService osobaService,
            VakcinaService vakcinaService,
            OsobaVakcinaService osobaVakcinaService,
            VakcinaSchemaService vakcinaSchemaService,
            NotificationService notificationService,
            AdminService adminService
    ) {
        this.osobaService = osobaService;
        this.vakcinaService = vakcinaService;
        this.osobaVakcinaService = osobaVakcinaService;
        this.vakcinaSchemaService = vakcinaSchemaService;
        this.notificationService = notificationService;
        this.adminService = adminService;
    }

    // === SPRÁVA OSÔB ===
    @PostMapping("/osoby")
    public ResponseEntity<OsobaEntity> createOsoba(@RequestBody OsobaEntity osoba) {
        try {
            OsobaEntity savedOsoba = adminService.createOsoba(osoba);
            return ResponseEntity.ok(savedOsoba);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/osoby/{id}")
    public ResponseEntity<OsobaEntity> updateOsoba(@PathVariable Long id, @RequestBody OsobaEntity osoba) {
        return osobaService.updateOsoba(id, osoba)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/osoby/{id}")
    public ResponseEntity<Void> deleteOsoba(@PathVariable Long id) {
        osobaService.removeOsoba(id);
        return ResponseEntity.noContent().build();
    }

    // === SPRÁVA VAKCÍN ===
    @PostMapping("/vakciny")
    public ResponseEntity<Vakcina> createVakcina(@RequestBody Vakcina vakcina) {
        return ResponseEntity.ok(vakcinaService.createVakcina(vakcina));
    }

    @PutMapping("/vakciny/{id}")
    public ResponseEntity<Vakcina> updateVakcina(@PathVariable Long id, @RequestBody Vakcina vakcina) {
        return vakcinaService.updateVakcina(id, vakcina)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/vakciny/{id}")
    public ResponseEntity<Void> deleteVakcina(@PathVariable Long id) {
        vakcinaService.deleteVakcina(id);
        return ResponseEntity.noContent().build();
    }

    // === KOMPLEXNÁ VAKCINÁCIA ===
    @PostMapping("/vakcinacie/komplexna")
    public ResponseEntity<OsobaVakcina> createKomplexnaVakcina(@RequestBody VakcinaciaRequestDTO request) {
        if (!request.isValidForNovuVakcinaciu()) {
            return ResponseEntity.badRequest().build();
        }

        OsobaVakcina result = osobaVakcinaService.createAdvancedVaccination(
                request.getOsobaId(),
                request.getVakcinaId(),
                request.getDatumPrvejDavky(),
                request.getMiestoAplikacie(),
                request.getBatchCislo(),
                request.getPoznamky()
        );

        return ResponseEntity.ok(result);
    }

    @PostMapping("/vakcinacie/dalsia-davka")
    public ResponseEntity<OsobaVakcina> addDalsiaDavka(@RequestBody VakcinaciaRequestDTO request) {
        if (!request.isValidForDalsiaDavka()) {
            return ResponseEntity.badRequest().build();
        }

        OsobaVakcina result = osobaVakcinaService.addNasledujucaDavka(
                request.getExistujucaVakcinaciaId(),
                request.getDatumPrvejDavky(),
                request.getMiestoAplikacie(),
                request.getBatchCislo(),
                request.getPoznamky()
        );

        return ResponseEntity.ok(result);
    }

    // === SPRÁVA SCHÉM VAKCÍN ===
    @PostMapping("/vakciny/{vakcinaId}/schema")
    public ResponseEntity<VakcinaSchema> createVakcinaSchema(
            @PathVariable Long vakcinaId,
            @RequestBody VakcinaSchemaDTO schemaDTO) {

        VakcinaSchema result = vakcinaSchemaService.createSchemaFromDTO(vakcinaId, schemaDTO);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/schema/{id}")
    public ResponseEntity<VakcinaSchema> updateVakcinaSchema(
            @PathVariable Long id,
            @RequestBody VakcinaSchemaDTO schemaDTO) {

        return vakcinaSchemaService.updateSchemaFromDTO(id, schemaDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === ŠTATISTIKY PRE ADMINA ===
    @GetMapping("/statistiky")
    public ResponseEntity<Map<String, Object>> getAdminStatistiky() {
        Map<String, Object> stats = Map.of(
                "celkovyPocetOsob", osobaService.getAllOsoby().size(),
                "celkovyPocetVakcin", vakcinaService.getAllVakciny().size(),
                "celkovyPocetVakcinacii", osobaVakcinaService.getAllOsobaVakcina().size(),
                "nedokonceneVakcinacie", osobaVakcinaService.getNedokonceneVakcinacie().size(),
                "notifikacieStats", notificationService.getNotificationStats()
        );
        return ResponseEntity.ok(stats);
    }

    // === NÚDZOVÉ FUNKCIE ===
    @PostMapping("/notifikacie/manual-check")
    public ResponseEntity<String> manualNotificationCheck() {
        notificationService.checkDailyNotifications();
        return ResponseEntity.ok("Manuálna kontrola notifikácií dokončená");
    }

    @GetMapping("/vakcinacie/problematicke")
    public ResponseEntity<List<OsobaVakcina>> getProblematickeVakcinacie() {
        // Vakcinácie s prekročenými termínmi alebo inými problémami
        return ResponseEntity.ok(osobaVakcinaService.getNedokonceneVakcinacie());
    }
}
