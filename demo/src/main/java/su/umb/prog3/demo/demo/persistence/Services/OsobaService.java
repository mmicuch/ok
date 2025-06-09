package su.umb.prog3.demo.demo.persistence.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import su.umb.prog3.demo.demo.persistence.entity.OsobaEntity;
import su.umb.prog3.demo.demo.persistence.entity.OsobaVakcina;
import su.umb.prog3.demo.demo.persistence.entity.Vakcina;
import su.umb.prog3.demo.demo.persistence.repos.OsobaRepository;
import su.umb.prog3.demo.demo.persistence.repos.VakcinaRepository;
import su.umb.prog3.demo.demo.persistence.repos.OsobaVakcinaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

@Service
public class OsobaService {

    private final OsobaRepository osobaRepository;
    private final VakcinaRepository vakcinaRepository;
    private final OsobaVakcinaRepository osobaVakcinaRepository;

    public OsobaService(@Autowired(required = false) OsobaRepository osobaRepository, 
                       @Autowired(required = false) VakcinaRepository vakcinaRepository, 
                       @Autowired(required = false) OsobaVakcinaRepository osobaVakcinaRepository) {
        this.osobaRepository = osobaRepository;
        this.vakcinaRepository = vakcinaRepository;
        this.osobaVakcinaRepository = osobaVakcinaRepository;
        System.out.println("OsobaService initialized - repositories available: " + 
                          (osobaRepository != null && vakcinaRepository != null && osobaVakcinaRepository != null));
    }

    public List<OsobaEntity> getAllOsoby() {
        if (osobaRepository == null) {
            System.err.println("OsobaRepository not available, returning empty list");
            return Collections.emptyList();
        }
        return (List<OsobaEntity>) osobaRepository.findAll();
    }

    public Optional<OsobaEntity> getOsobaById(Long id) {
        if (osobaRepository == null) {
            System.err.println("OsobaRepository not available");
            return Optional.empty();
        }
        return osobaRepository.findById(id);
    }

    public OsobaEntity createOsoba(OsobaEntity osoba) {
        if (osobaRepository == null) {
            throw new RuntimeException("Database not available");
        }
        return osobaRepository.save(osoba);
    }

    public Optional<OsobaEntity> updateOsoba(Long id, OsobaEntity osoba) {
        return osobaRepository.findById(id)
                .map(existingOsoba -> {
                    existingOsoba.setMeno(osoba.getMeno());
                    existingOsoba.setPriezvisko(osoba.getPriezvisko());
                    existingOsoba.setRokNarodenia(osoba.getRokNarodenia());
                    existingOsoba.setPohlavie(osoba.getPohlavie());
                    return osobaRepository.save(existingOsoba);
                });
    }

    public void removeOsoba(Long id) {
        osobaRepository.deleteById(id);
    }

    public OsobaVakcina addVakcinaToOsoba(Long osobaId, Long vakcinaId, LocalDate datumAplikacie, int poradieDavky) {
        OsobaEntity osoba = osobaRepository.findById(osobaId)
                .orElseThrow(() -> new RuntimeException("Osoba neexistuje"));
        
        Vakcina vakcina = vakcinaRepository.findById(vakcinaId)
                .orElseThrow(() -> new RuntimeException("Vakcína neexistuje"));

        OsobaVakcina osobaVakcina = new OsobaVakcina();
        osobaVakcina.setOsoba(osoba);
        osobaVakcina.setVakcina(vakcina);
        osobaVakcina.setDatumAplikacie(datumAplikacie);
        osobaVakcina.setPoradieDavky(poradieDavky);

        // Vypočítaj dátum ďalšej dávky
        osobaVakcina.vypocitajDatumDalsejDavky();

        return osobaVakcinaRepository.save(osobaVakcina);
    }

    public void removeVakcinaFromOsoba(Long id) {
        osobaVakcinaRepository.deleteById(id);
    }
}