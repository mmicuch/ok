package su.umb.prog3.demo.demo.persistence.dto;

import su.umb.prog3.demo.demo.persistence.entity.VakcinaSchema;
import java.util.List;
import java.util.stream.Collectors;

public class VakcinaSchemaDTO {
    private Long id;
    private Long vakcinaId;
    private String vakcinaNazov;
    private int celkovyPocetDavok;
    private String popis;
    private List<VakcinaDavkaDTO> davky;

    public VakcinaSchemaDTO() {
        // default constructor
    }

    public VakcinaSchemaDTO(VakcinaSchema entity) {
        this.id = entity.getId();
        this.vakcinaId = entity.getVakcina() != null ? entity.getVakcina().getId() : null;
        this.vakcinaNazov = entity.getVakcina() != null ? entity.getVakcina().getNazov() : null;
        this.celkovyPocetDavok = entity.getCelkovyPocetDavok();
        this.popis = entity.getPopis();
        
        // Map davky to DTOs
        if (entity.getDavky() != null) {
            this.davky = entity.getDavky().stream()
                    .map(VakcinaDavkaDTO::new)
                    .collect(Collectors.toList());
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVakcinaId() {
        return vakcinaId;
    }

    public void setVakcinaId(Long vakcinaId) {
        this.vakcinaId = vakcinaId;
    }

    public String getVakcinaNazov() {
        return vakcinaNazov;
    }

    public void setVakcinaNazov(String vakcinaNazov) {
        this.vakcinaNazov = vakcinaNazov;
    }

    public int getCelkovyPocetDavok() {
        return celkovyPocetDavok;
    }

    public void setCelkovyPocetDavok(int celkovyPocetDavok) {
        this.celkovyPocetDavok = celkovyPocetDavok;
    }

    public String getPopis() {
        return popis;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }

    public List<VakcinaDavkaDTO> getDavky() {
        return davky;
    }

    public void setDavky(List<VakcinaDavkaDTO> davky) {
        this.davky = davky;
    }
}