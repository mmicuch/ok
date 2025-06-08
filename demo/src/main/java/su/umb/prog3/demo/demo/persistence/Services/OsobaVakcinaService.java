package su.umb.prog3.demo.demo.persistence.Services;

import org.springframework.stereotype.Service;
import su.umb.prog3.demo.demo.persistence.dto.OsobaVakcinaDTO;
import su.umb.prog3.demo.demo.persistence.entity.OsobaEntity;
import su.umb.prog3.demo.demo.persistence.entity.Vakcina;
import su.umb.prog3.demo.demo.persistence.repos.OsobaRepository;
import su.umb.prog3.demo.demo.persistence.repos.VakcinaRepository;
import su.umb.prog3.demo.demo.persistence.repos.OsobaVakcinaRepository;
import su.umb.prog3.demo.demo.persistence.entity.OsobaVakcina;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OsobaVakcinaService {

    private final OsobaVakcinaRepository osobaVakcinaRepository;
    private final OsobaRepository osobaRepository;
    private final VakcinaRepository vakcinaRepository;

    public OsobaVakcinaService(
        OsobaVakcinaRepository osobaVakcinaRepository,
        OsobaRepository osobaRepository,
        VakcinaRepository vakcinaRepository
    ) {
        this.osobaVakcinaRepository = osobaVakcinaRepository;
        this.osobaRepository = osobaRepository;
        this.vakcinaRepository = vakcinaRepository;
    }

    public List<OsobaVakcina> getAllOsobaVakcina() {
        return osobaVakcinaRepository.findAll();
    }

    public Optional<OsobaVakcina> getOsobaVakcinaById(Long id) {
        return osobaVakcinaRepository.findById(id);
    }

    public OsobaVakcina createOsobaVakcinaFromDto(OsobaVakcinaDTO dto) {
        OsobaEntity osoba = osobaRepository.findById(dto.getOsobaId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid osobaId"));

        Vakcina vakcina = vakcinaRepository.findById(dto.getVakcinaId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid vakcinaId"));

        OsobaVakcina ov = new OsobaVakcina();
        ov.setOsoba(osoba);
        ov.setVakcina(vakcina);
        ov.setDatumAplikacie(dto.getDatumVakciny());  // Changed from getDatumAplikacie
        ov.setPoradieDavky(dto.getAktualnaDavka());   // Changed from getPoradieDavky

        return osobaVakcinaRepository.save(ov);
    }

    public OsobaVakcina createAdvancedVaccination(Long osobaId, Long vakcinaId, 
                                                 LocalDate datumPrvejDavky, String miestoAplikacie, 
                                                 String batchCislo, String poznamky) {
        OsobaEntity osoba = osobaRepository.findById(osobaId)
                .orElseThrow(() -> new RuntimeException("Osoba neexistuje"));
        
        Vakcina vakcina = vakcinaRepository.findById(vakcinaId)
                .orElseThrow(() -> new RuntimeException("Vakcína neexistuje"));

        OsobaVakcina osobaVakcina = new OsobaVakcina();
        osobaVakcina.setOsoba(osoba);
        osobaVakcina.setVakcina(vakcina);
        osobaVakcina.setDatumAplikacie(datumPrvejDavky);
        osobaVakcina.setPoradieDavky(1);
        osobaVakcina.setMiestoAplikacie(miestoAplikacie);
        osobaVakcina.setBatchCislo(batchCislo);
        osobaVakcina.setPoznamky(poznamky);

        // Vypočítaj dátum ďalšej dávky
        osobaVakcina.vypocitajDatumDalsejDavky();

        return osobaVakcinaRepository.save(osobaVakcina);
    }

    public OsobaVakcina addNasledujucaDavka(Long existujucaVakcinaciaId, LocalDate datumDavky, 
                                          String miestoAplikacie, String batchCislo, String poznamky) {
        OsobaVakcina poslednaVakcina = osobaVakcinaRepository.findById(existujucaVakcinaciaId)
                .orElseThrow(() -> new RuntimeException("Vakcinácia neexistuje"));

        // Nájdi najnovšiu dávku pre túto kombináciu osoba-vakcína
        Optional<OsobaVakcina> najnovsiaOpt = osobaVakcinaRepository
                .findTopByOsobaIdAndVakcinaIdOrderByPoradieDavkyDesc(
                        poslednaVakcina.getOsoba().getId(), 
                        poslednaVakcina.getVakcina().getId());

        if (najnovsiaOpt.isEmpty()) {
            throw new RuntimeException("Nenašla sa predchádzajúca dávka");
        }

        OsobaVakcina najnovsia = najnovsiaOpt.get();
        
        // Vytvor novú dávku
        OsobaVakcina novaDavka = new OsobaVakcina();
        novaDavka.setOsoba(najnovsia.getOsoba());
        novaDavka.setVakcina(najnovsia.getVakcina());
        novaDavka.setDatumAplikacie(datumDavky);
        novaDavka.setPoradieDavky(najnovsia.getPoradieDavky() + 1);
        novaDavka.setMiestoAplikacie(miestoAplikacie);
        novaDavka.setBatchCislo(batchCislo);
        novaDavka.setPoznamky(poznamky);

        // Vypočítaj dátum ďalšej dávky
        novaDavka.vypocitajDatumDalsejDavky();

        return osobaVakcinaRepository.save(novaDavka);
    }

    public Optional<OsobaVakcina> updateOsobaVakcina(Long id, OsobaVakcina osobaVakcina) {
        return osobaVakcinaRepository.findById(id)
                .map(existing -> {
                    existing.setDatumAplikacie(osobaVakcina.getDatumAplikacie());
                    existing.setPoradieDavky(osobaVakcina.getPoradieDavky());
                    existing.setMiestoAplikacie(osobaVakcina.getMiestoAplikacie());
                    existing.setBatchCislo(osobaVakcina.getBatchCislo());
                    existing.setPoznamky(osobaVakcina.getPoznamky());
                    
                    // Prepočítaj dátum ďalšej dávky
                    existing.vypocitajDatumDalsejDavky();
                    
                    return osobaVakcinaRepository.save(existing);
                });
    }

    public boolean deleteOsobaVakcina(Long id) {
        if (osobaVakcinaRepository.existsById(id)) {
            osobaVakcinaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<OsobaVakcina> getVakcinaciePreOsobu(Long osobaId) {
        return osobaVakcinaRepository.findByOsobaId(osobaId);
    }

    public List<OsobaVakcina> getVakcinaciePreVakcinu(Long vakcinaId) {
        return osobaVakcinaRepository.findByVakcinaId(vakcinaId);
    }

    public List<OsobaVakcina> getNedokonceneVakcinacie() {
        return osobaVakcinaRepository.findNedokonceneVakcinacie();
    }
}
