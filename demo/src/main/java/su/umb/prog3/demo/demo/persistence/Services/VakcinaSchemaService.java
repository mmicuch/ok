package su.umb.prog3.demo.demo.persistence.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.umb.prog3.demo.demo.persistence.dto.VakcinaSchemaDTO;
import su.umb.prog3.demo.demo.persistence.dto.VakcinaDavkaDTO;
import su.umb.prog3.demo.demo.persistence.entity.Vakcina;
import su.umb.prog3.demo.demo.persistence.entity.VakcinaSchema;
import su.umb.prog3.demo.demo.persistence.entity.VakcinaDavka;
import su.umb.prog3.demo.demo.persistence.repos.VakcinaRepository;
import su.umb.prog3.demo.demo.persistence.repos.VakcinaSchemaRepository;
import su.umb.prog3.demo.demo.persistence.repos.VakcinaDavkaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VakcinaSchemaService {

    private final VakcinaSchemaRepository vakcinaSchemaRepository;
    private final VakcinaRepository vakcinaRepository;
    private final VakcinaDavkaRepository vakcinaDavkaRepository;

    public VakcinaSchemaService(
        VakcinaSchemaRepository vakcinaSchemaRepository,
        VakcinaRepository vakcinaRepository,
        VakcinaDavkaRepository vakcinaDavkaRepository
    ) {
        this.vakcinaSchemaRepository = vakcinaSchemaRepository;
        this.vakcinaRepository = vakcinaRepository;
        this.vakcinaDavkaRepository = vakcinaDavkaRepository;
    }

    // Získaj všetky schémy
    public List<VakcinaSchema> getAllVakcinaSchemata() {
        return vakcinaSchemaRepository.findAll();
    }

    // Získaj všetky schémy ako DTO
    public List<VakcinaSchemaDTO> getAllVakcinaSchemataDTO() {
        return vakcinaSchemaRepository.findAll()
                .stream()
                .map(VakcinaSchemaDTO::new)
                .collect(Collectors.toList());
    }

    // Získaj schému podľa ID
    public Optional<VakcinaSchema> getVakcinaSchemaById(Long id) {
        return vakcinaSchemaRepository.findById(id);
    }

    // Získaj schému podľa ID ako DTO
    public Optional<VakcinaSchemaDTO> getVakcinaSchemaByIdDTO(Long id) {
        return vakcinaSchemaRepository.findById(id)
                .map(VakcinaSchemaDTO::new);
    }

    // Získaj schému pre konkrétnu vakcínu
    public Optional<VakcinaSchema> getSchemaForVakcina(Long vakcinaId) {
        return vakcinaSchemaRepository.findByVakcinaId(vakcinaId);
    }

    // Získaj schému pre konkrétnu vakcínu ako DTO
    public Optional<VakcinaSchemaDTO> getSchemaForVakcinaDTO(Long vakcinaId) {
        return vakcinaSchemaRepository.findByVakcinaId(vakcinaId)
                .map(VakcinaSchemaDTO::new);
    }

    // Vytvor novú schému
    public VakcinaSchema createVakcinaSchema(VakcinaSchema vakcinaSchema) {
        return vakcinaSchemaRepository.save(vakcinaSchema);
    }

    // Vytvor novú schému z DTO
    public VakcinaSchema createVakcinaSchemaFromDTO(VakcinaSchemaDTO dto) {
        Vakcina vakcina = vakcinaRepository.findById(dto.getVakcinaId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid vakcinaId: " + dto.getVakcinaId()));

        VakcinaSchema schema = new VakcinaSchema();
        schema.setVakcina(vakcina);
        schema.setCelkovyPocetDavok(dto.getCelkovyPocetDavok());
        schema.setPopis(dto.getPopis());

        return vakcinaSchemaRepository.save(schema);
    }

    // Vytvor kompletnú schému s dávkami
    @Transactional
    public VakcinaSchema createCompleteSchemaWithDavky(Long vakcinaId, int pocetDavok, 
                                                      List<Integer> casoveRozostupy, 
                                                      List<String> nazovyDavok, 
                                                      String popis) {
        // Validácia vstupov
        if (casoveRozostupy.size() != pocetDavok - 1) {
            throw new IllegalArgumentException("Počet časových rozostupov musí byť o 1 menší ako počet dávok");
        }
        
        if (nazovyDavok != null && nazovyDavok.size() != pocetDavok) {
            throw new IllegalArgumentException("Počet názvov dávok sa musí zhodovať s počtom dávok");
        }

        Vakcina vakcina = vakcinaRepository.findById(vakcinaId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid vakcinaId: " + vakcinaId));

        // Vytvor schému
        VakcinaSchema schema = new VakcinaSchema();
        schema.setVakcina(vakcina);
        schema.setCelkovyPocetDavok(pocetDavok);
        schema.setPopis(popis);
        schema = vakcinaSchemaRepository.save(schema);

        // Vytvor dávky
        for (int i = 0; i < pocetDavok; i++) {
            VakcinaDavka davka = new VakcinaDavka();
            davka.setVakcinaSchema(schema);
            davka.setPoradieDavky(i + 1);
            
            // Prvá dávka má 0 dní od predchádzajúcej
            if (i == 0) {
                davka.setDniOdPredchadzajucej(0);
            } else {
                davka.setDniOdPredchadzajucej(casoveRozostupy.get(i - 1));
            }
            
            // Nastav názov dávky
            if (nazovyDavok != null && i < nazovyDavok.size()) {
                davka.setNazovDavky(nazovyDavok.get(i));
            } else {
                davka.setNazovDavky("Dávka " + (i + 1));
            }
            
            davka.setJePovinne(true);
            vakcinaDavkaRepository.save(davka);
        }

        // Načítaj schému s dávkami
        return vakcinaSchemaRepository.findById(schema.getId()).orElse(schema);
    }

    // Vytvor predvolenú schému pre vakcínu (1 dávka)
    public VakcinaSchema createDefaultSchema(Long vakcinaId) {
        Vakcina vakcina = vakcinaRepository.findById(vakcinaId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid vakcinaId: " + vakcinaId));

        VakcinaSchema schema = new VakcinaSchema();
        schema.setVakcina(vakcina);
        schema.setCelkovyPocetDavok(1);
        schema.setPopis("Predvolená schéma - 1 dávka");
        schema = vakcinaSchemaRepository.save(schema);

        // Vytvor jednu dávku
        VakcinaDavka davka = new VakcinaDavka();
        davka.setVakcinaSchema(schema);
        davka.setPoradieDavky(1);
        davka.setDniOdPredchadzajucej(0);
        davka.setNazovDavky("Jediná dávka");
        davka.setJePovinne(true);
        vakcinaDavkaRepository.save(davka);

        return schema;
    }

    // Aktualizuj schému
    public Optional<VakcinaSchema> updateVakcinaSchema(Long id, VakcinaSchema vakcinaSchema) {
        if (vakcinaSchemaRepository.existsById(id)) {
            vakcinaSchema.setId(id);
            return Optional.of(vakcinaSchemaRepository.save(vakcinaSchema));
        }
        return Optional.empty();
    }

    // Aktualizuj schému z DTO
    public Optional<VakcinaSchema> updateVakcinaSchemaFromDTO(Long id, VakcinaSchemaDTO dto) {
        Optional<VakcinaSchema> existingSchema = vakcinaSchemaRepository.findById(id);
        if (existingSchema.isPresent()) {
            VakcinaSchema schema = existingSchema.get();
            
            if (dto.getVakcinaId() != null) {
                Vakcina vakcina = vakcinaRepository.findById(dto.getVakcinaId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid vakcinaId: " + dto.getVakcinaId()));
                schema.setVakcina(vakcina);
            }
            
            schema.setCelkovyPocetDavok(dto.getCelkovyPocetDavok());
            schema.setPopis(dto.getPopis());

            return Optional.of(vakcinaSchemaRepository.save(schema));
        }
        return Optional.empty();
    }

    // Zmaž schému (aj s dávkami)
    @Transactional
    public boolean deleteVakcinaSchema(Long id) {
        if (vakcinaSchemaRepository.existsById(id)) {
            // Najprv zmaž všetky dávky
            List<VakcinaDavka> davky = vakcinaDavkaRepository.findByVakcinaSchemaIdOrderByPoradieDavky(id);
            vakcinaDavkaRepository.deleteAll(davky);
            
            // Potom zmaž schému
            vakcinaSchemaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Skontroluj, či má vakcína schému
    public boolean hasSchema(Long vakcinaId) {
        return vakcinaSchemaRepository.findByVakcinaId(vakcinaId).isPresent();
    }

    // Získaj všetky vakcíny bez schémy
    public List<Vakcina> getVakcinyWithoutSchema() {
        List<Long> vakcinaIdsWithSchema = vakcinaSchemaRepository.findAll()
                .stream()
                .map(schema -> schema.getVakcina().getId())
                .collect(Collectors.toList());
        
        return vakcinaRepository.findAll()
                .stream()
                .filter(vakcina -> !vakcinaIdsWithSchema.contains(vakcina.getId()))
                .collect(Collectors.toList());
    }

    // Validácia schémy
    public boolean validateSchema(Long schemaId) {
        Optional<VakcinaSchema> schemaOpt = vakcinaSchemaRepository.findById(schemaId);
        if (schemaOpt.isEmpty()) {
            return false;
        }
        
        VakcinaSchema schema = schemaOpt.get();
        List<VakcinaDavka> davky = vakcinaDavkaRepository.findByVakcinaSchemaIdOrderByPoradieDavky(schemaId);
        
        // Skontroluj, či počet dávok súhlasí
        if (davky.size() != schema.getCelkovyPocetDavok()) {
            return false;
        }
        
        // Skontroluj poradie dávok
        for (int i = 0; i < davky.size(); i++) {
            if (davky.get(i).getPoradieDavky() != i + 1) {
                return false;
            }
        }
        
        return true;
    }

    // Aktualizuj počet dávok v schéme na základe skutočných dávok
    @Transactional
    public VakcinaSchema synchronizePocetDavok(Long schemaId) {
        VakcinaSchema schema = vakcinaSchemaRepository.findById(schemaId)
                .orElseThrow(() -> new IllegalArgumentException("Schema not found: " + schemaId));
        
        List<VakcinaDavka> davky = vakcinaDavkaRepository.findByVakcinaSchemaIdOrderByPoradieDavky(schemaId);
        schema.setCelkovyPocetDavok(davky.size());
        
        return vakcinaSchemaRepository.save(schema);
    }

    // Klonuj schému pre inú vakcínu
    @Transactional
    public VakcinaSchema cloneSchemaForVakcina(Long sourceSchemaId, Long targetVakcinaId) {
        VakcinaSchema sourceSchema = vakcinaSchemaRepository.findById(sourceSchemaId)
                .orElseThrow(() -> new IllegalArgumentException("Source schema not found: " + sourceSchemaId));
        
        Vakcina targetVakcina = vakcinaRepository.findById(targetVakcinaId)
                .orElseThrow(() -> new IllegalArgumentException("Target vakcina not found: " + targetVakcinaId));
        
        // Vytvor novú schému
        VakcinaSchema newSchema = new VakcinaSchema();
        newSchema.setVakcina(targetVakcina);
        newSchema.setCelkovyPocetDavok(sourceSchema.getCelkovyPocetDavok());
        newSchema.setPopis("Klonovaná schéma z " + sourceSchema.getVakcina().getNazov());
        newSchema = vakcinaSchemaRepository.save(newSchema);
        
        // Klonuj dávky
        List<VakcinaDavka> sourceDavky = vakcinaDavkaRepository.findByVakcinaSchemaIdOrderByPoradieDavky(sourceSchemaId);
        for (VakcinaDavka sourceDavka : sourceDavky) {
            VakcinaDavka newDavka = new VakcinaDavka();
            newDavka.setVakcinaSchema(newSchema);
            newDavka.setPoradieDavky(sourceDavka.getPoradieDavky());
            newDavka.setDniOdPredchadzajucej(sourceDavka.getDniOdPredchadzajucej());
            newDavka.setNazovDavky(sourceDavka.getNazovDavky());
            newDavka.setPopis(sourceDavka.getPopis());
            newDavka.setJePovinne(sourceDavka.isJePovinne());
            vakcinaDavkaRepository.save(newDavka);
        }
        
        return newSchema;
    }
}