package su.umb.prog3.demo.demo.persistence.Services;

import org.springframework.stereotype.Service;
import su.umb.prog3.demo.demo.persistence.dto.VakcinaDavkaDTO;
import su.umb.prog3.demo.demo.persistence.entity.VakcinaDavka;
import su.umb.prog3.demo.demo.persistence.entity.VakcinaSchema;
import su.umb.prog3.demo.demo.persistence.repos.VakcinaDavkaRepository;
import su.umb.prog3.demo.demo.persistence.repos.VakcinaSchemaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VakcinaDavkaService {

    private final VakcinaDavkaRepository vakcinaDavkaRepository;
    private final VakcinaSchemaRepository vakcinaSchemaRepository;

    public VakcinaDavkaService(
        VakcinaDavkaRepository vakcinaDavkaRepository,
        VakcinaSchemaRepository vakcinaSchemaRepository
    ) {
        this.vakcinaDavkaRepository = vakcinaDavkaRepository;
        this.vakcinaSchemaRepository = vakcinaSchemaRepository;
    }

    // Získaj všetky dávky
    public List<VakcinaDavka> getAllVakcinaDavky() {
        return vakcinaDavkaRepository.findAll();
    }

    // Získaj všetky dávky ako DTO
    public List<VakcinaDavkaDTO> getAllVakcinaDavkyDTO() {
        return vakcinaDavkaRepository.findAll()
                .stream()
                .map(VakcinaDavkaDTO::new)
                .collect(Collectors.toList());
    }

    // Získaj dávku podľa ID
    public Optional<VakcinaDavka> getVakcinaDavkaById(Long id) {
        return vakcinaDavkaRepository.findById(id);
    }

    // Získaj dávku podľa ID ako DTO
    public Optional<VakcinaDavkaDTO> getVakcinaDavkaByIdDTO(Long id) {
        return vakcinaDavkaRepository.findById(id)
                .map(VakcinaDavkaDTO::new);
    }

    // Získaj dávky pre konkrétnu schému
    public List<VakcinaDavka> getDavkyForSchema(Long schemaId) {
        return vakcinaDavkaRepository.findByVakcinaSchemaIdOrderByPoradieDavky(schemaId);
    }

    // Získaj dávky pre konkrétnu schému ako DTO
    public List<VakcinaDavkaDTO> getDavkyForSchemaDTO(Long schemaId) {
        return vakcinaDavkaRepository.findByVakcinaSchemaIdOrderByPoradieDavky(schemaId)
                .stream()
                .map(VakcinaDavkaDTO::new)
                .collect(Collectors.toList());
    }

    // Získaj konkrétnu dávku podľa schémy a poradia
    public Optional<VakcinaDavka> getDavkaBySchemaAndPoradie(Long schemaId, int poradie) {
        VakcinaDavka davka = vakcinaDavkaRepository.findByVakcinaSchemaIdAndPoradieDavky(schemaId, poradie);
        return Optional.ofNullable(davka);
    }

    // Vytvor novú dávku
    public VakcinaDavka createVakcinaDavka(VakcinaDavka vakcinaDavka) {
        return vakcinaDavkaRepository.save(vakcinaDavka);
    }

    // Vytvor novú dávku z DTO
    public VakcinaDavka createVakcinaDavkaFromDTO(VakcinaDavkaDTO dto) {
        VakcinaSchema schema = vakcinaSchemaRepository.findById(dto.getVakcinaSchemaId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid vakcinaSchemaId: " + dto.getVakcinaSchemaId()));

        VakcinaDavka davka = new VakcinaDavka();
        davka.setVakcinaSchema(schema);
        davka.setPoradieDavky(dto.getPoradieDavky());
        davka.setDniOdPredchadzajucej(dto.getDniOdPredchadzajucej());
        davka.setNazovDavky(dto.getNazovDavky());
        davka.setPopis(dto.getPopis());
        davka.setJePovinne(dto.isJePovinne());

        return vakcinaDavkaRepository.save(davka);
    }

    // Aktualizuj existujúcu dávku
    public Optional<VakcinaDavka> updateVakcinaDavka(Long id, VakcinaDavka vakcinaDavka) {
        if (vakcinaDavkaRepository.existsById(id)) {
            vakcinaDavka.setId(id);
            return Optional.of(vakcinaDavkaRepository.save(vakcinaDavka));
        }
        return Optional.empty();
    }

    // Aktualizuj existujúcu dávku z DTO
    public Optional<VakcinaDavka> updateVakcinaDavkaFromDTO(Long id, VakcinaDavkaDTO dto) {
        Optional<VakcinaDavka> existingDavka = vakcinaDavkaRepository.findById(id);
        if (existingDavka.isPresent()) {
            VakcinaDavka davka = existingDavka.get();
            
            // Aktualizuj iba tie polia, ktoré sa môžu zmeniť
            if (dto.getVakcinaSchemaId() != null) {
                VakcinaSchema schema = vakcinaSchemaRepository.findById(dto.getVakcinaSchemaId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid vakcinaSchemaId: " + dto.getVakcinaSchemaId()));
                davka.setVakcinaSchema(schema);
            }
            
            davka.setPoradieDavky(dto.getPoradieDavky());
            davka.setDniOdPredchadzajucej(dto.getDniOdPredchadzajucej());
            davka.setNazovDavky(dto.getNazovDavky());
            davka.setPopis(dto.getPopis());
            davka.setJePovinne(dto.isJePovinne());

            return Optional.of(vakcinaDavkaRepository.save(davka));
        }
        return Optional.empty();
    }

    // Zmaž dávku
    public boolean deleteVakcinaDavka(Long id) {
        if (vakcinaDavkaRepository.existsById(id)) {
            vakcinaDavkaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Zmaž všetky dávky pre schému
    public void deleteAllDavkyForSchema(Long schemaId) {
        List<VakcinaDavka> davky = vakcinaDavkaRepository.findByVakcinaSchemaIdOrderByPoradieDavky(schemaId);
        vakcinaDavkaRepository.deleteAll(davky);
    }

    // Vytvor viac dávok naraz pre schému
    public List<VakcinaDavka> createMultipleDavkyForSchema(Long schemaId, List<VakcinaDavkaDTO> davkyDTO) {
        VakcinaSchema schema = vakcinaSchemaRepository.findById(schemaId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid vakcinaSchemaId: " + schemaId));

        List<VakcinaDavka> davky = davkyDTO.stream()
                .map(dto -> {
                    VakcinaDavka davka = new VakcinaDavka();
                    davka.setVakcinaSchema(schema);
                    davka.setPoradieDavky(dto.getPoradieDavky());
                    davka.setDniOdPredchadzajucej(dto.getDniOdPredchadzajucej());
                    davka.setNazovDavky(dto.getNazovDavky());
                    davka.setPopis(dto.getPopis());
                    davka.setJePovinne(dto.isJePovinne());
                    return davka;
                })
                .collect(Collectors.toList());

        return vakcinaDavkaRepository.saveAll(davky);
    }

    // Validácia poradia dávok pre schému
    public boolean validateDavkyOrder(Long schemaId) {
        List<VakcinaDavka> davky = getDavkyForSchema(schemaId);
        
        for (int i = 0; i < davky.size(); i++) {
            VakcinaDavka davka = davky.get(i);
            int expectedPoradie = i + 1;
            
            if (davka.getPoradieDavky() != expectedPoradie) {
                return false;
            }
            
            // Prvá dávka by mala mať 0 dní od predchádzajúcej
            if (i == 0 && davka.getDniOdPredchadzajucej() != 0) {
                return false;
            }
            
            // Ostatné dávky by mali mať pozitívny počet dní
            if (i > 0 && davka.getDniOdPredchadzajucej() <= 0) {
                return false;
            }
        }
        
        return true;
    }

    // Získaj nasledujúcu dávku v poradí
    public Optional<VakcinaDavka> getNextDavka(Long schemaId, int currentPoradie) {
        VakcinaDavka davka = vakcinaDavkaRepository.findByVakcinaSchemaIdAndPoradieDavky(schemaId, currentPoradie + 1);
        return Optional.ofNullable(davka);
    }

    // Získaj celkový počet dní od začiatku do konkrétnej dávky
    public int getTotalDaysToDate(Long schemaId, int poradieDavky) {
        List<VakcinaDavka> davky = vakcinaDavkaRepository.findByVakcinaSchemaIdOrderByPoradieDavky(schemaId);
        return davky.stream()
                .filter(d -> d.getPoradieDavky() <= poradieDavky)
                .mapToInt(VakcinaDavka::getDniOdPredchadzajucej)
                .sum();
    }

    public List<VakcinaDavka> getDavkyPreSchemu(Long schemaId) {
        return vakcinaDavkaRepository.findByVakcinaSchemaIdOrderByPoradieDavky(schemaId);
    }

    public Optional<VakcinaDavka> getDavka(Long schemaId, int poradie) {
        return Optional.ofNullable(vakcinaDavkaRepository.findByVakcinaSchemaIdAndPoradieDavky(schemaId, poradie));
    }

    public Optional<VakcinaDavka> getNasledujucaDavka(Long schemaId, int aktualnePoradie) {
        return Optional.ofNullable(vakcinaDavkaRepository.findNasledujucuDavku(schemaId, aktualnePoradie));
    }
}