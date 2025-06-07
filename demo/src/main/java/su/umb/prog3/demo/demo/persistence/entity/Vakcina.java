package su.umb.prog3.demo.demo.persistence.entity;

import jakarta.persistence.*;
import su.umb.prog3.demo.demo.persistence.enums.TypVakciny;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*; 
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Vakcina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nazov;
    private String vyrobca;

    @Enumerated(EnumType.STRING)
    private TypVakciny typ;

    @OneToMany(mappedBy = "vakcina", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OsobaVakcina> osoby;

    // Nová relácia pre schému vakcíny
    @OneToOne(mappedBy = "vakcina", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private VakcinaSchema vakcinaSchema;

    // Nové polia pre dodatočné informácie
    @Column(name = "je_aktivna", nullable = false)
    private boolean jeAktivna = true;

    @Column(name = "poznamky", columnDefinition = "TEXT")
    private String poznamky;

    // ✅ Standard getters/setters for JSON serialization
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public String getVyrobca() {
        return vyrobca;
    }

    public void setVyrobca(String vyrobca) {
        this.vyrobca = vyrobca;
    }

    public TypVakciny getTyp() {
        return typ;
    }

    public void setTyp(TypVakciny typ) {
        this.typ = typ;
    }

    public List<OsobaVakcina> getOsoby() {
        return osoby;
    }

    public void setOsoby(List<OsobaVakcina> osoby) {
        this.osoby = osoby;
    }

    public VakcinaSchema getVakcinaSchema() {
        return vakcinaSchema;
    }

    public void setVakcinaSchema(VakcinaSchema vakcinaSchema) {
        this.vakcinaSchema = vakcinaSchema;
        if (vakcinaSchema != null) {
            vakcinaSchema.setVakcina(this);
        }
    }

    public boolean isJeAktivna() {
        return jeAktivna;
    }

    public void setJeAktivna(boolean jeAktivna) {
        this.jeAktivna = jeAktivna;
    }

    public String getPoznamky() {
        return poznamky;
    }

    public void setPoznamky(String poznamky) {
        this.poznamky = poznamky;
    }

    // Pomocné metódy pre backward compatibility
    public boolean getJeAktivna() {
        return jeAktivna;
    }

    // ✅ Existing methods with "Entity" suffix (keep these for internal use)
    public Long getIdEntity() {
        return id;
    }

    public void setIdEntity(Long id) {
        this.id = id;
    }

    public String getNazovEntity() {
        return nazov;
    }

    public void setNazovEntity(String nazov) {
        this.nazov = nazov;
    }

    public String getVyrobcaEntity() {
        return vyrobca;
    }

    public void setVyrobcaEntity(String vyrobca) {
        this.vyrobca = vyrobca;
    }

    public TypVakciny getTypEntity() {
        return typ;
    }

    public void setTypEntity(TypVakciny typ) {
        this.typ = typ;
    }

    public List<OsobaVakcina> getOsobyEntity() {
        return osoby;
    }

    public void setOsobyEntity(List<OsobaVakcina> osoby) {
        this.osoby = osoby;
    }

    // Pomocné metódy pre prácu so schémou
    public boolean maDefinovanuSchemu() {
        return vakcinaSchema != null;
    }

    public int getPocetDavok() {
        if (vakcinaSchema != null) {
            return vakcinaSchema.getCelkovyPocetDavok();
        }
        return 1; // default pre vakcíny bez schémy
    }

    @Override
    public String toString() {
        return "Vakcina{" +
                "id=" + id +
                ", nazov='" + nazov + '\'' +
                ", vyrobca='" + vyrobca + '\'' +
                ", typ=" + typ +
                ", jeAktivna=" + jeAktivna +
                '}';
    }
}