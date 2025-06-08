package su.umb.prog3.demo.demo.persistence.Services;

import org.springframework.stereotype.Service;
import su.umb.prog3.demo.demo.persistence.entity.Vakcina;
import su.umb.prog3.demo.demo.persistence.repos.VakcinaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class VakcinaService {

    private final VakcinaRepository vakcinaRepository;

    public VakcinaService(VakcinaRepository vakcinaRepository) {
        this.vakcinaRepository = vakcinaRepository;
    }

    // Get all vaccines
    public List<Vakcina> getAllVakciny() {
        try {
            System.out.println("=== VakcinaService: Getting all vaccines ===");
            List<Vakcina> result = vakcinaRepository.findAll();
            System.out.println("VakcinaService: Found " + result.size() + " vaccines");
            return result;
        } catch (Exception e) {
            System.err.println("VakcinaService: Error getting vaccines: " + e.getMessage());
            e.printStackTrace();
            // Vráťme prázdny zoznam namiesto 500
            return List.of();
        }
    }

    // Get vaccine by ID
    public Optional<Vakcina> getVakcinaById(Long id) {
        return vakcinaRepository.findById(id);
    }

    // Create a new vaccine
    public Vakcina createVakcina(Vakcina vakcina) {
        try {
            System.out.println("=== VakcinaService: Creating vaccine ===");
            System.out.println("Input vaccine: " + vakcina);

            // Nastav default hodnoty
            if (vakcina.getTyp() == null) {
                System.out.println("Setting default type to mRNA");
                vakcina.setTyp(su.umb.prog3.demo.demo.persistence.enums.TypVakciny.mRNA);
            }
            vakcina.setJeAktivna(true);

            Vakcina saved = vakcinaRepository.save(vakcina);
            System.out.println("VakcinaService: Saved vaccine: " + saved);
            return saved;
        } catch (Exception e) {
            System.err.println("VakcinaService: Error creating vaccine: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Get vaccines by activity status
    public List<Vakcina> getVakcinyByAktivnost(Boolean jeAktivna) {
        if (Boolean.TRUE.equals(jeAktivna)) {
            return vakcinaRepository.findByJeAktivna(true);
        } else if (Boolean.FALSE.equals(jeAktivna)) {
            return vakcinaRepository.findByJeAktivna(false);
        } else {
            return vakcinaRepository.findAll();
        }
    }

    // Update an existing vaccine
    public Optional<Vakcina> updateVakcina(Long id, Vakcina updatedVakcina) {
        return vakcinaRepository.findById(id)
                .map(existingVakcina -> {
                    if (updatedVakcina.getNazov() != null) {
                        existingVakcina.setNazov(updatedVakcina.getNazov());
                    }
                    if (updatedVakcina.getVyrobca() != null) {
                        existingVakcina.setVyrobca(updatedVakcina.getVyrobca());
                    }
                    if (updatedVakcina.getTyp() != null) {
                        existingVakcina.setTyp(updatedVakcina.getTyp());
                    }
                    // Vakcina nemá setPocetDavok - toto sa rieši cez VakcinaSchema
                    existingVakcina.setJeAktivna(updatedVakcina.isJeAktivna());
                    if (updatedVakcina.getPoznamky() != null) {
                        existingVakcina.setPoznamky(updatedVakcina.getPoznamky());
                    }
                    return vakcinaRepository.save(existingVakcina);
                });
    }

    // Delete vaccine by ID
    public boolean deleteVakcina(Long id) {
        if (vakcinaRepository.existsById(id)) {
            vakcinaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}