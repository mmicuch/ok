package su.umb.prog3.demo.demo.persistence.dto;

import su.umb.prog3.demo.demo.persistence.entity.VakcinaDavka;

public class VakcinaDavkaDTO {
    private Long id;
    private Long vakcinaSchemaId;
    private int poradieDavky;
    private int dniOdPredchadzajucej;
    private String nazovDavky;
    private String popis;
    private boolean jePovinne;

    public VakcinaDavkaDTO() {
        // default constructor
    }

    public VakcinaDavkaDTO(VakcinaDavka entity) {
        this.id = entity.getId();
        this.vakcinaSchemaId = entity.getVakcinaSchema() != null ? entity.getVakcinaSchema().getId() : null;
        this.poradieDavky = entity.getPoradieDavky();
        this.dniOdPredchadzajucej = entity.getDniOdPredchadzajucej();
        this.nazovDavky = entity.getNazovDavky();
        this.popis = entity.getPopis();
        this.jePovinne = entity.isJePovinne();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVakcinaSchemaId() {
        return vakcinaSchemaId;
    }

    public void setVakcinaSchemaId(Long vakcinaSchemaId) {
        this.vakcinaSchemaId = vakcinaSchemaId;
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
}