package su.umb.prog3.demo.demo.persistence.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Entita reprezentujúca schému vakcíny - definuje koľko dávok má mať vakcína
 * a aké sú časové rozostupy medzi nimi
 */
@Entity
@Table(name = "vakcina_schema")
public class VakcinaSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "vakcina_id", nullable = false)
    private Vakcina vakcina;

    @Column(name = "celkovy_pocet_davok", nullable = false)
    private int celkovyPocetDavok;

    @Column(name = "popis")
    private String popis;

    @Column(name = "nazov_schemy")
    private String nazovSchemy;

    @Column(name = "je_aktívna")
    private boolean jeAktivna;

    @OneToMany(mappedBy = "vakcinaSchema", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VakcinaDavka> davky = new ArrayList<>();

    // Konštruktory
    public VakcinaSchema() {}

    public VakcinaSchema(Vakcina vakcina, int celkovyPocetDavok, String popis) {
        this.vakcina = vakcina;
        this.celkovyPocetDavok = celkovyPocetDavok;
        this.popis = popis;
    }

    // Getters a Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vakcina getVakcina() {
        return vakcina;
    }

    public void setVakcina(Vakcina vakcina) {
        this.vakcina = vakcina;
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

    public String getNazovSchemy() {
        return nazovSchemy;
    }

    public void setNazovSchemy(String nazovSchemy) {
        this.nazovSchemy = nazovSchemy;
    }

    public boolean isJeAktivna() {
        return jeAktivna;
    }

    public void setJeAktivna(boolean jeAktivna) {
        this.jeAktivna = jeAktivna;
    }

    public List<VakcinaDavka> getDavky() {
        return davky;
    }

    public void setDavky(List<VakcinaDavka> davky) {
        this.davky = davky;
    }

    // Pomocné metódy
    public void addDavka(VakcinaDavka davka) {
        davky.add(davka);
        davka.setVakcinaSchema(this);
    }

    public void removeDavka(VakcinaDavka davka) {
        davky.remove(davka);
        davka.setVakcinaSchema(null);
    }

    @Override
    public String toString() {
        return "VakcinaSchema{" +
                "id=" + id +
                ", vakcina=" + (vakcina != null ? vakcina.getNazov() : null) +
                ", celkovyPocetDavok=" + celkovyPocetDavok +
                ", popis='" + popis + '\'' +
                '}';
    }
}