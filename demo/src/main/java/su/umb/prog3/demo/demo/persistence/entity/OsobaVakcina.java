package su.umb.prog3.demo.demo.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class OsobaVakcina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "osoba_id", nullable = false)
    private OsobaEntity osoba;

    @ManyToOne
    @JoinColumn(name = "vakcina_id", nullable = false)
    private Vakcina vakcina;

    private LocalDate datumAplikacie;
    private int poradieDavky;

    // Nové polia pre rozšírenú funkcionalitu
    @Column(name = "datum_planovej_dalej_davky")
    private LocalDate datumPlanovanejDalsejDavky;

    @Column(name = "je_dokoncena", nullable = false)
    private boolean jeDokoncena = false;

    @Column(name = "poznamky", columnDefinition = "TEXT")
    private String poznamky;

    @Column(name = "miesto_aplikacie")
    private String miestoAplikacie;

    @Column(name = "batch_cislo")
    private String batchCislo;

    // Standard getters/setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OsobaEntity getOsoba() {
        return osoba;
    }

    public void setOsoba(OsobaEntity osoba) {
        this.osoba = osoba;
    }

    public Vakcina getVakcina() {
        return vakcina;
    }

    public void setVakcina(Vakcina vakcina) {
        this.vakcina = vakcina;
    }

    public LocalDate getDatumAplikacie() {
        return datumAplikacie;
    }

    public void setDatumAplikacie(LocalDate datumAplikacie) {
        this.datumAplikacie = datumAplikacie;
    }

    public int getPoradieDavky() {
        return poradieDavky;
    }

    public void setPoradieDavky(int poradieDavky) {
        this.poradieDavky = poradieDavky;
    }

    public LocalDate getDatumPlanovanejDalsejDavky() {
        return datumPlanovanejDalsejDavky;
    }

    public void setDatumPlanovanejDalsejDavky(LocalDate datumPlanovanejDalsejDavky) {
        this.datumPlanovanejDalsejDavky = datumPlanovanejDalsejDavky;
    }

    public boolean isJeDokoncena() {
        return jeDokoncena;
    }

    public void setJeDokoncena(boolean jeDokoncena) {
        this.jeDokoncena = jeDokoncena;
    }

    public String getPoznamky() {
        return poznamky;
    }

    public void setPoznamky(String poznamky) {
        this.poznamky = poznamky;
    }

    public String getMiestoAplikacie() {
        return miestoAplikacie;
    }

    public void setMiestoAplikacie(String miestoAplikacie) {
        this.miestoAplikacie = miestoAplikacie;
    }

    public String getBatchCislo() {
        return batchCislo;
    }

    public void setBatchCislo(String batchCislo) {
        this.batchCislo = batchCislo;
    }

    // Pomocné metódy pre backward compatibility
    public boolean getJeDokoncena() {
        return jeDokoncena;
    }

    // For backward compatibility with existing code
    public Long getIdEntity() {
        return id;
    }

    public void setIdEntity(Long id) {
        this.id = id;
    }

    public OsobaEntity getOsobaEntity() {
        return osoba;
    }

    public void setOsobaEntity(OsobaEntity osoba) {
        this.osoba = osoba;
    }

    public Vakcina getVakcinaEntity() {
        return vakcina;
    }

    public void setVakcinaEntity(Vakcina vakcina) {
        this.vakcina = vakcina;
    }

    public LocalDate getDatumAplikacieEntity() {
        return datumAplikacie;
    }

    public void setDatumAplikacieEntity(LocalDate datumAplikacie) {
        this.datumAplikacie = datumAplikacie;
    }

    public int getPoradieDavkyEntity() {
        return poradieDavky;
    }

    public void setPoradieDavkyEntity(int poradieDavky) {
        this.poradieDavky = poradieDavky;
    }

    // Pomocné metódy pre prácu s viacnásobnými dávkami
    public void vypocitajDatumDalsejDavky() {
        if (vakcina != null && vakcina.getVakcinaSchema() != null) {
            VakcinaSchema schema = vakcina.getVakcinaSchema();
            
            // Nájdi nasledujúcu dávku v schéme
            VakcinaDavka nasledujucaDavka = schema.getDavky().stream()
                    .filter(d -> d.getPoradieDavky() == this.poradieDavky + 1)
                    .findFirst()
                    .orElse(null);
            
            if (nasledujucaDavka != null) {
                this.datumPlanovanejDalsejDavky = this.datumAplikacie.plusDays(nasledujucaDavka.getDniOdPredchadzajucej());
            } else {
                // Toto je posledná dávka
                this.jeDokoncena = true;
                this.datumPlanovanejDalsejDavky = null;
            }
        }
    }

    public boolean potrebujeDalsiuDavku() {
        if (jeDokoncena) {
            return false;
        }
        
        if (vakcina != null && vakcina.getVakcinaSchema() != null) {
            return poradieDavky < vakcina.getVakcinaSchema().getCelkovyPocetDavok();
        }
        
        return false; // pre vakcíny bez schémy
    }

    public int getPocetZostupujucichDni() {
        if (datumPlanovanejDalsejDavky != null) {
            return (int) java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), datumPlanovanejDalsejDavky);
        }
        return -1;
    }

    @Override
    public String toString() {
        return "OsobaVakcina{" +
                "id=" + id +
                ", osoba=" + (osoba != null ? osoba.getId() : null) +
                ", vakcina=" + (vakcina != null ? vakcina.getId() : null) +
                ", datumAplikacie=" + datumAplikacie +
                ", poradieDavky=" + poradieDavky +
                ", datumPlanovanejDalsejDavky=" + datumPlanovanejDalsejDavky +
                ", jeDokoncena=" + jeDokoncena +
                '}';
    }
}