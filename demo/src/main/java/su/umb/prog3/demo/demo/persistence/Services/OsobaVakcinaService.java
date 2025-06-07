package su.umb.prog3.demo.demo.persistence.Services;

import org.springframework.stereotype.Service;
import su.umb.prog3.demo.demo.persistence.dto.OsobaVakcinaDTO;
import su.umb.prog3.demo.demo.persistence.entity.OsobaEntity;
import su.umb.prog3.demo.demo.persistence.entity.Vakcina;
import su.umb.prog3.demo.demo.persistence.repos.OsobaRepository;
import su.umb.prog3.demo.demo.persistence.repos.VakcinaRepository;
import su.umb.prog3.demo.demo.persistence.repos.OsobaVakcinaRepository;
import su.umb.prog3.demo.demo.persistence.entity.OsobaVakcina;

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

    public OsobaVakcina createOsobaVakcina(OsobaVakcina osobaVakcina) {
        return osobaVakcinaRepository.save(osobaVakcina);
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

    public Optional<OsobaVakcina> updateOsobaVakcina(Long id, OsobaVakcina osobaVakcina) {
        if (osobaVakcinaRepository.existsById(id)) {
            osobaVakcina.setIdEntity(id);
            return Optional.of(osobaVakcinaRepository.save(osobaVakcina));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteOsobaVakcina(Long id) {
        if (osobaVakcinaRepository.existsById(id)) {
            osobaVakcinaRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
