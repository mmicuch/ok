package su.umb.prog3.demo.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import su.umb.prog3.demo.demo.persistence.entity.OsobaVakcina;
import su.umb.prog3.demo.demo.persistence.Services.OsobaVakcinaService;
import su.umb.prog3.demo.demo.persistence.dto.OsobaVakcinaDTO;

import java.util.List;

@RestController
@RequestMapping("/api/osobavakcina")
public class OsobaVakcinaController {

    private final OsobaVakcinaService osobaVakcinaService;

    public OsobaVakcinaController(OsobaVakcinaService osobaVakcinaService) {
        this.osobaVakcinaService = osobaVakcinaService;
    }

    // Get all osoba_vakcina records
    @GetMapping
    public ResponseEntity<List<OsobaVakcinaDTO>> getAllOsobaVakcina() {
    List<OsobaVakcinaDTO> dtos = osobaVakcinaService.getAllOsobaVakcina().stream()
            .map(OsobaVakcinaDTO::new)
            .toList();

    return ResponseEntity.ok(dtos);
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



}