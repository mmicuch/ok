package su.umb.prog3.demo.demo.persistence.Services;

import org.springframework.stereotype.Service;
import su.umb.prog3.demo.demo.persistence.entity.*;
import su.umb.prog3.demo.demo.persistence.repos.*;
import su.umb.prog3.demo.demo.persistence.dto.*;

import java.util.List;
import java.util.Optional;

@Service
public class VakcinaSchemaService {

    private final VakcinaSchemaRepository vakcinaSchemaRepository;
    private final VakcinaRepository vakcinaRepository;
    private final VakcinaDavkaService vakcinaDavkaService;

    public VakcinaSchemaService(
            VakcinaSchemaRepository vakcinaSchemaRepository,
            VakcinaRepository vakcinaRepository,
            VakcinaDavkaService vakcinaDavkaService
    ) {
        this.vakcinaSchemaRepository = vakcinaSchemaRepository;
        this.vakcinaRepository = vakcinaRepository;
        this.vakcinaDavkaService = vakcinaDavkaService;
    }

    public List<VakcinaSchema> getAllSchemas() {
        return vakcinaSchemaRepository.findAll();
    }

    public Optional<VakcinaSchema> getSchemaById(Long id) {
        return vakcinaSchemaRepository.findById(id);
    }

    public Optional<VakcinaSchema> getSchemaByVakcinaId(Long vakcinaId) {
        return vakcinaSchemaRepository.findByVakcinaId(vakcinaId);
    }

    public VakcinaSchema createSchemaFromDTO(Long vakcinaId, VakcinaSchemaDTO dto) {
        Vakcina vakcina = vakcinaRepository.findById(vakcinaId)
                .orElseThrow(() -> new IllegalArgumentException("Vakcína neexistuje"));

        VakcinaSchema schema = new VakcinaSchema();
        schema.setVakcina(vakcina);
        schema.setNazovSchemy(dto.getNazovSchemy());
        schema.setPopis(dto.getPopis());
        schema.setCelkovyPocetDavok(dto.getCelkovyPocetDavok());
        schema.setJeAktivna(dto.isJeAktivna());

        VakcinaSchema savedSchema = vakcinaSchemaRepository.save(schema);

        // Vytvor dávky pre schému
        if (dto.getDavky() != null && !dto.getDavky().isEmpty()) {
            vakcinaDavkaService.createMultipleDavkyForSchema(savedSchema.getId(), dto.getDavky());
        }

        return savedSchema;
    }

    public Optional<VakcinaSchema> updateSchemaFromDTO(Long id, VakcinaSchemaDTO dto) {
        return vakcinaSchemaRepository.findById(id)
                .map(existingSchema -> {
                    existingSchema.setNazovSchemy(dto.getNazovSchemy());
                    existingSchema.setPopis(dto.getPopis());
                    existingSchema.setCelkovyPocetDavok(dto.getCelkovyPocetDavok());
                    existingSchema.setJeAktivna(dto.isJeAktivna());

                    VakcinaSchema savedSchema = vakcinaSchemaRepository.save(existingSchema);

                    // Aktualizuj dávky
                    if (dto.getDavky() != null) {
                        vakcinaDavkaService.deleteAllDavkyForSchema(savedSchema.getId());
                        vakcinaDavkaService.createMultipleDavkyForSchema(savedSchema.getId(), dto.getDavky());
                    }

                    return savedSchema;
                });
    }

    public boolean deleteSchema(Long id) {
        if (vakcinaSchemaRepository.existsById(id)) {
            vakcinaDavkaService.deleteAllDavkyForSchema(id);
            vakcinaSchemaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public VakcinaSchema createDefaultSchema(Vakcina vakcina, int pocetDavok, int intervalDni) {
        VakcinaSchema schema = new VakcinaSchema();
        schema.setVakcina(vakcina);
        schema.setNazovSchemy("Default schéma pre " + vakcina.getNazov());
        schema.setCelkovyPocetDavok(pocetDavok);
        schema.setJeAktivna(true);

        VakcinaSchema savedSchema = vakcinaSchemaRepository.save(schema);

        // Vytvor default dávky
        for (int i = 1; i <= pocetDavok; i++) {
            VakcinaDavkaDTO davka = new VakcinaDavkaDTO();
            davka.setVakcinaSchemaId(savedSchema.getId());
            davka.setPoradieDavky(i);
            davka.setDniOdPredchadzajucej(i == 1 ? 0 : intervalDni);
            davka.setNazovDavky(i + ". dávka");
            davka.setJePovinne(true);

            vakcinaDavkaService.createVakcinaDavkaFromDTO(davka);
        }

        return savedSchema;
    }
}