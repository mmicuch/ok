package su.umb.prog3.demo.demo.persistence.dto;

import su.umb.prog3.demo.demo.persistence.entity.OsobaVakcina;

import java.time.LocalDate;

public class OsobaVakcinaDTO {
    private Long osobaId;
    private Long vakcinaId;
    private LocalDate datumAplikacie;
    private int poradieDavky;

    public OsobaVakcinaDTO() {
        // default constructor
    }

    public OsobaVakcinaDTO(OsobaVakcina entity) {
        this.osobaId = entity.getOsoba().getId();
        this.vakcinaId = entity.getVakcina().getId();
        this.datumAplikacie = entity.getDatumAplikacie();
        this.poradieDavky = entity.getPoradieDavky();
    }

    public Long getOsobaId() {
        return osobaId;
    }

    public void setOsobaId(Long osobaId) {
        this.osobaId = osobaId;
    }

    public Long getVakcinaId() {
        return vakcinaId;
    }

    public void setVakcinaId(Long vakcinaId) {
        this.vakcinaId = vakcinaId;
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
}
