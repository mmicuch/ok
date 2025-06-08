package su.umb.prog3.demo.demo.persistence.dto;

import java.util.List;

public class VakcinaSchemaDTO {
    private Long id;
    private Long vakcinaId;
    private String nazovSchemy;
    private String popis;
    private int celkovyPocetDavok;
    private boolean jeAktivna;
    private List<VakcinaDavkaDTO> davky;

    public VakcinaSchemaDTO() {
        this.jeAktivna = true;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getVakcinaId() { return vakcinaId; }
    public void setVakcinaId(Long vakcinaId) { this.vakcinaId = vakcinaId; }

    public String getNazovSchemy() { return nazovSchemy; }
    public void setNazovSchemy(String nazovSchemy) { this.nazovSchemy = nazovSchemy; }

    public String getPopis() { return popis; }
    public void setPopis(String popis) { this.popis = popis; }

    public int getCelkovyPocetDavok() { return celkovyPocetDavok; }
    public void setCelkovyPocetDavok(int celkovyPocetDavok) { this.celkovyPocetDavok = celkovyPocetDavok; }

    public boolean isJeAktivna() { return jeAktivna; }
    public void setJeAktivna(boolean jeAktivna) { this.jeAktivna = jeAktivna; }

    public List<VakcinaDavkaDTO> getDavky() { return davky; }
    public void setDavky(List<VakcinaDavkaDTO> davky) { this.davky = davky; }
}