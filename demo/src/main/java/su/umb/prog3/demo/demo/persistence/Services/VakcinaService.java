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
        return vakcinaRepository.findAll();
    }

    // Get vaccine by ID
    public Optional<Vakcina> getVakcinaById(Long id) {
        return vakcinaRepository.findById(id);
    }

    // Create a new vaccine
    public Vakcina createVakcina(Vakcina vakcina) {
        return vakcinaRepository.save(vakcina);
    }

    // Update an existing vaccine
    public Optional<Vakcina> updateVakcina(Long id, Vakcina vakcina) {
        if (vakcinaRepository.existsById(id)) {
            vakcina.setIdEntity(id);
            return Optional.of(vakcinaRepository.save(vakcina));
        } else {
            return Optional.empty();
        }
    }

    // Delete vaccine by ID
    public boolean deleteVakcina(Long id) {
        if (vakcinaRepository.existsById(id)) {
            vakcinaRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}