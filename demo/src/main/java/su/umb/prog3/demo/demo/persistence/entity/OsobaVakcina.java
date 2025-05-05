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

    // ✅ Custom getters/setters (you already had)
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

    // ✅ Standard getters/setters required for JSON mapping

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

    @Override
    public String toString() {
        return "OsobaVakcina{" +
                "id=" + id +
                ", osoba=" + (osoba != null ? osoba.getId() : null) +
                ", vakcina=" + (vakcina != null ? vakcina.getIdEntity() : null) +
                ", datumAplikacie=" + datumAplikacie +
                ", poradieDavky=" + poradieDavky +
                '}';
    }
}
