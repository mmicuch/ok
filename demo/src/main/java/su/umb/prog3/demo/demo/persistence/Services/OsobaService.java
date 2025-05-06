
package su.umb.prog3.demo.demo.persistence.Services;

import org.springframework.stereotype.Service;
import su.umb.prog3.demo.demo.persistence.entity.OsobaEntity;
import su.umb.prog3.demo.demo.persistence.entity.OsobaVakcina;
import su.umb.prog3.demo.demo.persistence.entity.Vakcina;
import su.umb.prog3.demo.demo.persistence.repos.OsobaRepository;
import su.umb.prog3.demo.demo.persistence.repos.OsobaVakcinaRepository;
import su.umb.prog3.demo.demo.persistence.repos.VakcinaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Service
public class OsobaService {

    private final OsobaRepository osobaRepository;
    private final VakcinaRepository vakcinaRepository;
    private final OsobaVakcinaRepository osobaVakcinaRepository;

    public OsobaService(OsobaRepository osobaRepository, VakcinaRepository vakcinaRepository, OsobaVakcinaRepository osobaVakcinaRepository) {
        this.osobaRepository = osobaRepository;
        this.vakcinaRepository = vakcinaRepository;
        this.osobaVakcinaRepository = osobaVakcinaRepository;
    }

    // Create Person
    public OsobaEntity createOsoba(OsobaEntity osoba) {
        return osobaRepository.save(osoba);
    }

    // Remove Person
    public void removeOsoba(Long id) {
        osobaRepository.deleteById(id);
    }

    // Get all Persons
    public List<OsobaEntity> getAllOsoby() {
        List<OsobaEntity> osoby = new ArrayList<>();
        osobaRepository.findAll().forEach(osoby::add);
        return osoby;
    }

    // Add Vaccination Record (updated to use consistent method names)
    public OsobaVakcina addVakcinaToOsoba(Long osobaId, Long vakcinaId, LocalDate datumAplikacie, int poradieDavky) {
        OsobaEntity osoba = osobaRepository.findById(osobaId).orElseThrow(() -> new RuntimeException("Person not found"));
        Vakcina vakcina = vakcinaRepository.findById(vakcinaId).orElseThrow(() -> new RuntimeException("Vaccine not found"));
        
        OsobaVakcina osobaVakcina = new OsobaVakcina();
        osobaVakcina.setOsoba(osoba);
        osobaVakcina.setVakcina(vakcina);
        osobaVakcina.setDatumAplikacie(datumAplikacie);
        osobaVakcina.setPoradieDavky(poradieDavky);

        return osobaVakcinaRepository.save(osobaVakcina);
    }

    // Remove Vaccination Record
    public void removeVakcinaFromOsoba(Long id) {
        osobaVakcinaRepository.deleteById(id);
    }
}