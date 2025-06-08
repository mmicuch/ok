package su.umb.prog3.demo.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import su.umb.prog3.demo.demo.persistence.entity.Vakcina;
import su.umb.prog3.demo.demo.persistence.Services.VakcinaService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vakcina") // Skontrolujte, že toto je správne
@CrossOrigin(origins = "http://localhost:4200")
public class VakcinaController {

    private final VakcinaService vakcinaService;

    public VakcinaController(VakcinaService vakcinaService) {
        this.vakcinaService = vakcinaService;
    }

    // Get all vaccines - PRIDAJTE CORS annotation
    @GetMapping("/all")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Vakcina>> getAllVakciny() {
        try {
            System.out.println("=== GETTING ALL VACCINES ===");
            List<Vakcina> vakciny = vakcinaService.getAllVakciny();
            if (vakciny == null) {
                System.out.println("No vaccines found, returning empty list");
                return ResponseEntity.ok(List.of());
            }
            System.out.println("Found " + vakciny.size() + " vaccines");
            return ResponseEntity.ok(vakciny);
        } catch (Exception e) {
            System.err.println("Error in getAllVakciny: " + e.getMessage());
            e.printStackTrace();
            // Vráťme prázdny zoznam namiesto 500
            return ResponseEntity.ok(List.of());
        }
    }

    // Get vaccine by ID
    @GetMapping("/{id}")
    public ResponseEntity<Vakcina> getVakcinaById(@PathVariable Long id) {
        return vakcinaService.getVakcinaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new vaccine
    @PostMapping("/add")
public ResponseEntity<Vakcina> createVakcina(@RequestBody Vakcina vakcina) {
    try {
        System.out.println("=== CREATING VACCINE ===");
        System.out.println("Received vaccine: " + vakcina);
        Vakcina saved = vakcinaService.createVakcina(vakcina);
        System.out.println("Saved vaccine: " + saved);
        return ResponseEntity.ok(saved);
    } catch (Exception e) {
        System.err.println("Error creating vaccine: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(500).build();
    }
}

    // Update an existing vaccine
    @PutMapping("/{id}")
    public ResponseEntity<Vakcina> updateVakcina(@PathVariable Long id, @RequestBody Vakcina vakcina) {
        try {
            vakcinaService.updateVakcina(id, vakcina);
            Optional<Vakcina> updatedVakcina = vakcinaService.getVakcinaById(id);
            return updatedVakcina.map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete vaccine by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVakcina(@PathVariable Long id) {
        if (vakcinaService.deleteVakcina(id)) {
            return ResponseEntity.ok("Vakcina bola vymazaná.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
