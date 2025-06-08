package su.umb.prog3.demo.demo.persistence.dto;

import su.umb.prog3.demo.demo.persistence.entity.OsobaVakcina;
import java.time.LocalDate;

public class OsobaVakcinaDTO {
    private Long id;
    private Long osobaId;
    private Long vakcinaId;
    private String menoOsoby;
    private String priezviskoOsoby;
    private String nazovVakciny;
    private LocalDate datumVakciny;
    private int aktualnaDavka;
    private int potrebnaDavka;
    private String emailOsoby;

    public OsobaVakcinaDTO() {}

    public OsobaVakcinaDTO(OsobaVakcina osobaVakcina) {
        this.id = osobaVakcina.getId();
        this.osobaId = osobaVakcina.getOsoba() != null ? osobaVakcina.getOsoba().getId() : null;
        this.vakcinaId = osobaVakcina.getVakcina() != null ? osobaVakcina.getVakcina().getId() : null;
        this.menoOsoby = osobaVakcina.getOsoba() != null ? osobaVakcina.getOsoba().getMeno() : "";
        this.priezviskoOsoby = osobaVakcina.getOsoba() != null ? osobaVakcina.getOsoba().getPriezvisko() : "";
        this.nazovVakciny = osobaVakcina.getVakcina() != null ? osobaVakcina.getVakcina().getNazov() : "";
        this.datumVakciny = osobaVakcina.getDatumAplikacie();
        this.aktualnaDavka = osobaVakcina.getPoradieDavky();
        this.potrebnaDavka = osobaVakcina.getVakcina() != null ? osobaVakcina.getVakcina().getPocetDavok() : 1;
        this.emailOsoby = osobaVakcina.getOsoba() != null ? osobaVakcina.getOsoba().getEmail() : "";
    }

    // Gettery a settery
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getMenoOsoby() { return menoOsoby; }
    public void setMenoOsoby(String menoOsoby) { this.menoOsoby = menoOsoby; }
    
    public String getPriezviskoOsoby() { return priezviskoOsoby; }
    public void setPriezviskoOsoby(String priezviskoOsoby) { this.priezviskoOsoby = priezviskoOsoby; }
    
    public String getNazovVakciny() { return nazovVakciny; }
    public void setNazovVakciny(String nazovVakciny) { this.nazovVakciny = nazovVakciny; }
    
    public LocalDate getDatumVakciny() { return datumVakciny; }
    public void setDatumVakciny(LocalDate datumVakciny) { this.datumVakciny = datumVakciny; }
    
    public int getAktualnaDavka() { return aktualnaDavka; }
    public void setAktualnaDavka(int aktualnaDavka) { this.aktualnaDavka = aktualnaDavka; }
    
    public int getPotrebnaDavka() { return potrebnaDavka; }
    public void setPotrebnaDavka(int potrebnaDavka) { this.potrebnaDavka = potrebnaDavka; }
    
    public String getEmailOsoby() { return emailOsoby; }
    public void setEmailOsoby(String emailOsoby) { this.emailOsoby = emailOsoby; }

    // Pridajte chýbajúce gettery/settery
    public Long getOsobaId() {
        return this.osobaId;
    }
    
    public void setOsobaId(Long osobaId) {
        this.osobaId = osobaId;
    }
    
    public Long getVakcinaId() {
        return this.vakcinaId;
    }
    
    public void setVakcinaId(Long vakcinaId) {
        this.vakcinaId = vakcinaId;
    }
}