package su.umb.prog3.demo.demo.persistence.entity;

import jakarta.persistence.*;

/**
 * Entita reprezentujúca jednu dávku v schéme vakcíny
 * Definuje poradie dávky a časový odstup od predchádzajúcej dávky
 */
@Entity
@Table(name = "vakcina_davka")
public class VakcinaDavka {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vakcina_schema_id", nullable = false)
    private VakcinaSchema vakcinaSchema;

    @Column(name = "poradie_davky", nullable = false)
    private int poradieDavky;

    @Column(name = "dni_od_predchadzajucej", nullable = false)
    private int dniOdPredchadzajucej;

    @Column(name = "nazov_davky")
    private String nazovDavky;

    @Column(name = "popis")
    private String popis;

    @Column(name = "je_povinne", nullable = false)
    private boolean jePovinne = true;

    // Konštruktory
    public VakcinaDavka() {}

    public VakcinaDavka(VakcinaSchema vakcinaSchema, int poradieDavky, int dniOdPredchadzajucej, String nazovDavky) {
        this.vakcinaSchema = vakcinaSchema;
        this.poradieDavky = poradieDavky;
        this.dniOdPredchadzajucej = dniOdPredchadzajucej;
        this.nazovDavky = nazovDavky;
    }

    // Getters a Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VakcinaSchema getVakcinaSchema() {
        return vakcinaSchema;
    }

    public void setVakcinaSchema(VakcinaSchema vakcinaSchema) {
        this.vakcinaSchema = vakcinaSchema;
    }

    public int getPoradieDavky() {
        return poradieDavky;
    }

    public void setPoradieDavky(int poradieDavky) {
        this.poradieDavky = poradieDavky;
    }

    public int getDniOdPredchadzajucej() {
        return dniOdPredchadzajucej;
    }

    public void setDniOdPredchadzajucej(int dniOdPredchadzajucej) {
        this.dniOdPredchadzajucej = dniOdPredchadzajucej;
    }

    public String getNazovDavky() {
        return nazovDavky;
    }

    public void setNazovDavky(String nazovDavky) {
        this.nazovDavky = nazovDavky;
    }

    public String getPopis() {
        return popis;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }

    public boolean isJePovinne() {
        return jePovinne;
    }

    public void setJePovinne(boolean jePovinne) {
        this.jePovinne = jePovinne;
    }

    // Pomocné metódy pre backward compatibility
    public boolean getJePovinne() {
        return jePovinne;
    }

    @Override
    public String toString() {
        return "VakcinaDavka{" +
                "id=" + id +
                ", poradieDavky=" + poradieDavky +
                ", dniOdPredchadzajucej=" + dniOdPredchadzajucej +
                ", nazovDavky='" + nazovDavky + '\'' +
                ", jePovinne=" + jePovinne +
                '}';
    }
}