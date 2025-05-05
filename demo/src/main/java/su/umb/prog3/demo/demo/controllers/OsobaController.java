package su.umb.prog3.demo.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import su.umb.prog3.demo.demo.persistence.entity.OsobaEntity;
import su.umb.prog3.demo.demo.persistence.entity.OsobaVakcina;
import su.umb.prog3.demo.demo.persistence.Services.OsobaService;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/osoby")
public class OsobaController {

    private final OsobaService osobaService;

    public OsobaController(OsobaService osobaService) {
        this.osobaService = osobaService;
    }

    @PostMapping("/add")
    public ResponseEntity<OsobaEntity> addOsoba(@RequestBody OsobaEntity osoba) {
        return ResponseEntity.ok(osobaService.createOsoba(osoba));
    }

    @GetMapping("/all")
    public ResponseEntity<List<OsobaEntity>> getAllOsoby() {
        return ResponseEntity.ok(osobaService.getAllOsoby());
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeOsoba(@PathVariable Long id) {
        osobaService.removeOsoba(id);
        return ResponseEntity.ok("Person removed successfully");
    }

    @PostMapping("/vakcina/add")
    public ResponseEntity<OsobaVakcina> addVakcinaToOsoba(@RequestParam Long osobaId, @RequestParam Long vakcinaId, @RequestParam LocalDate datumAplikacie, @RequestParam int poradieDavky) {
        return ResponseEntity.ok(osobaService.addVakcinaToOsoba(osobaId, vakcinaId, datumAplikacie, poradieDavky));
    }

    @DeleteMapping("/vakcina/remove/{id}")
    public ResponseEntity<String> removeVakcinaFromOsoba(@PathVariable Long id) {
        osobaService.removeVakcinaFromOsoba(id);
        return ResponseEntity.ok("Vaccination record removed successfully");
    }
}
