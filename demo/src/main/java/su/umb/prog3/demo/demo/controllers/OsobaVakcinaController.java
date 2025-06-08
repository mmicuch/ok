package su.umb.prog3.demo.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import su.umb.prog3.demo.demo.persistence.entity.OsobaVakcina;
import su.umb.prog3.demo.demo.persistence.Services.OsobaVakcinaService;
import su.umb.prog3.demo.demo.persistence.dto.OsobaVakcinaDTO;

import java.util.List;

@RestController
@RequestMapping("/api/osobavakcina")
@CrossOrigin(origins = "http://localhost:4200") // Pridané CORS
public class OsobaVakcinaController {

    private final OsobaVakcinaService osobaVakcinaService;

    public OsobaVakcinaController(OsobaVakcinaService osobaVakcinaService) {
        this.osobaVakcinaService = osobaVakcinaService;
    }

    // Get all osoba_vakcina records
    @GetMapping
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<OsobaVakcinaDTO>> getAllOsobaVakcina() {
        try {
            System.out.println("=== Getting all OsobaVakcina records ===");
            List<OsobaVakcina> entities = osobaVakcinaService.getAllOsobaVakcina();
            System.out.println("Found " + entities.size() + " entities");
            
            // Vráťme prázdny zoznam namiesto chyby ak nie sú žiadne entity
            if (entities.isEmpty()) {
                System.out.println("No vaccination records found, returning empty list");
                return ResponseEntity.ok(List.of());
            }
            
            List<OsobaVakcinaDTO> dtos = entities.stream()
                    .map(entity -> {
                        try {
                            return new OsobaVakcinaDTO(entity);
                        } catch (Exception e) {
                            System.err.println("Error converting entity to DTO: " + e.getMessage());
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(dto -> dto != null)
                    .toList();
            
            System.out.println("Returning " + dtos.size() + " DTOs");
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            System.err.println("Error in getAllOsobaVakcina: " + e.getMessage());
            e.printStackTrace();
            // Vráťme prázdny zoznam namiesto 500 chyby
            return ResponseEntity.ok(List.of());
        }
    }

    // Get osoba_vakcina by ID
    @GetMapping("/{id}")
    public ResponseEntity<OsobaVakcina> getOsobaVakcinaById(@PathVariable Long id) {
        return osobaVakcinaService.getOsobaVakcinaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new osoba_vakcina record
  @PostMapping("/add")
public ResponseEntity<OsobaVakcina> createOsobaVakcina(@RequestBody OsobaVakcinaDTO dto) {
    OsobaVakcina saved = osobaVakcinaService.createOsobaVakcinaFromDto(dto);
    System.out.println("Saved vaccination: " + saved); // Log the saved object
    return ResponseEntity.ok(saved); // Make sure to return the saved object
}


    // Update an existing osoba_vakcina record
    @PutMapping("/{id}")
    public ResponseEntity<OsobaVakcina> updateOsobaVakcina(@PathVariable Long id, @RequestBody OsobaVakcina osobaVakcina) {
        return osobaVakcinaService.updateOsobaVakcina(id, osobaVakcina)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete osoba_vakcina by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOsobaVakcina(@PathVariable Long id) {
        if (osobaVakcinaService.deleteOsobaVakcina(id)) {
            return ResponseEntity.ok("OsobaVakcina bola vymazaná.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Add this method to your OsobaVakcinaController class:

    @GetMapping("/search")
    public ResponseEntity<List<OsobaVakcinaDTO>> searchOsobaVakcina(@RequestParam String query) {
        // Convert to lowercase for case-insensitive search
        String lowerQuery = query.toLowerCase();
        
        List<OsobaVakcinaDTO> results = osobaVakcinaService.getAllOsobaVakcina().stream()
            .filter(ov -> 
                (ov.getOsoba().getMeno() + " " + ov.getOsoba().getPriezvisko()).toLowerCase().contains(lowerQuery) ||
                ov.getVakcina().getNazov().toLowerCase().contains(lowerQuery))
            .map(OsobaVakcinaDTO::new)
            .toList();
        
        return ResponseEntity.ok(results);
    }

    // Add advanced vaccination
    @PostMapping("/advanced")
    public ResponseEntity<OsobaVakcina> createAdvancedVaccination(
            @RequestParam Long osobaId,
            @RequestParam Long vakcinaId,
            @RequestParam String datumPrvejDavky,
            @RequestParam(required = false) String miestoAplikacie,
            @RequestParam(required = false) String batchCislo,
            @RequestParam(required = false) String poznamky) {
        
        java.time.LocalDate datum = java.time.LocalDate.parse(datumPrvejDavky);
        OsobaVakcina result = osobaVakcinaService.createAdvancedVaccination(
                osobaId, vakcinaId, datum, miestoAplikacie, batchCislo, poznamky);
        return ResponseEntity.ok(result);
    }

    // Add next dose
    @PostMapping("/dalsia-davka")
    public ResponseEntity<OsobaVakcina> addNasledujucaDavka(
            @RequestParam Long existujucaVakcinaciaId,
            @RequestParam String datumDavky,
            @RequestParam(required = false) String miestoAplikacie,
            @RequestParam(required = false) String batchCislo,
            @RequestParam(required = false) String poznamky) {
        
        java.time.LocalDate datum = java.time.LocalDate.parse(datumDavky);
        OsobaVakcina result = osobaVakcinaService.addNasledujucaDavka(
                existujucaVakcinaciaId, datum, miestoAplikacie, batchCislo, poznamky);
        return ResponseEntity.ok(result);
    }

    // Get vaccinations for person
    @GetMapping("/osoba/{osobaId}")
    public ResponseEntity<List<OsobaVakcina>> getVakcinaciePreOsobu(@PathVariable Long osobaId) {
        return ResponseEntity.ok(osobaVakcinaService.getVakcinaciePreOsobu(osobaId));
    }

    // Get incomplete vaccinations
    @GetMapping("/nedokoncene")
    public ResponseEntity<List<OsobaVakcina>> getNedokonceneVakcinacie() {
        return ResponseEntity.ok(osobaVakcinaService.getNedokonceneVakcinacie());
    }

}