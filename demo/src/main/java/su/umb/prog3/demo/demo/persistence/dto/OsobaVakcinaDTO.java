package su.umb.prog3.demo.demo.persistence.dto;

import java.time.LocalDate;

public class OsobaVakcinaDTO {
    private Long id;
    private Long osobaId;
    private Long vakcinaId;
    private LocalDate datumVakciny;
    private String nazovVakciny;
    private String menoOsoby;
    private String priezviskoOsoby;
    
    // Nové polia pre viacnásobné dávky
    private Integer pocetDavok;
    private Integer aktualnaDavka;
    private Integer intervalMedziDavkami; // v dňoch
    private LocalDate datumDalsejDavky;
    private Boolean jeKompletna; // či je vakcinácia dokončená
    
    // Konštruktory
    public OsobaVakcinaDTO() {}
    
    public OsobaVakcinaDTO(Long id, Long osobaId, Long vakcinaId, LocalDate datumVakciny, 
                          String nazovVakciny, String menoOsoby, String priezviskoOsoby) {
        this.id = id;
        this.osobaId = osobaId;
        this.vakcinaId = vakcinaId;
        this.datumVakciny = datumVakciny;
        this.nazovVakciny = nazovVakciny;
        this.menoOsoby = menoOsoby;
        this.priezviskoOsoby = priezviskoOsoby;
        this.aktualnaDavka = 1;
        this.jeKompletna = false;
    }
    
    // Rozšírený konštruktor pre viacnásobné dávky
    public OsobaVakcinaDTO(Long id, Long osobaId, Long vakcinaId, LocalDate datumVakciny, 
                          String nazovVakciny, String menoOsoby, String priezviskoOsoby,
                          Integer pocetDavok, Integer aktualnaDavka, Integer intervalMedziDavkami) {
        this(id, osobaId, vakcinaId, datumVakciny, nazovVakciny, menoOsoby, priezviskoOsoby);
        this.pocetDavok = pocetDavok;
        this.aktualnaDavka = aktualnaDavka;
        this.intervalMedziDavkami = intervalMedziDavkami;
        this.jeKompletna = aktualnaDavka >= pocetDavok;
        
        // Vypočítaj dátum ďalšej dávky ak nie je kompletná
        if (!this.jeKompletna && intervalMedziDavkami != null) {
            this.datumDalsejDavky = datumVakciny.plusDays(intervalMedziDavkami);
        }
    }
    
    // Gettery a settery
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getOsobaId() { return osobaId; }
    public void setOsobaId(Long osobaId) { this.osobaId = osobaId; }
    
    public Long getVakcinaId() { return vakcinaId; }
    public void setVakcinaId(Long vakcinaId) { this.vakcinaId = vakcinaId; }
    
    public LocalDate getDatumVakciny() { return datumVakciny; }
    public void setDatumVakciny(LocalDate datumVakciny) { this.datumVakciny = datumVakciny; }
    
    public String getNazovVakciny() { return nazovVakciny; }
    public void setNazovVakciny(String nazovVakciny) { this.nazovVakciny = nazovVakciny; }
    
    public String getMenoOsoby() { return menoOsoby; }
    public void setMenoOsoby(String menoOsoby) { this.menoOsoby = menoOsoby; }
    
    public String getPriezviskoOsoby() { return priezviskoOsoby; }
    public void setPriezviskoOsoby(String priezviskoOsoby) { this.priezviskoOsoby = priezviskoOsoby; }
    
    // Nové gettery a settery
    public Integer getPocetDavok() { return pocetDavok; }
    public void setPocetDavok(Integer pocetDavok) { 
        this.pocetDavok = pocetDavok;
        updateKompletnost();
    }
    
    public Integer getAktualnaDavka() { return aktualnaDavka; }
    public void setAktualnaDavka(Integer aktualnaDavka) { 
        this.aktualnaDavka = aktualnaDavka;
        updateKompletnost();
    }
    
    public Integer getIntervalMedziDavkami() { return intervalMedziDavkami; }
    public void setIntervalMedziDavkami(Integer intervalMedziDavkami) { 
        this.intervalMedziDavkami = intervalMedziDavkami;
        updateDatumDalsejDavky();
    }
    
    public LocalDate getDatumDalsejDavky() { return datumDalsejDavky; }
    public void setDatumDalsejDavky(LocalDate datumDalsejDavky) { this.datumDalsejDavky = datumDalsejDavky; }
    
    public Boolean getJeKompletna() { return jeKompletna; }
    public void setJeKompletna(Boolean jeKompletna) { this.jeKompletna = jeKompletna; }
    
    // Pomocné metódy
    private void updateKompletnost() {
        if (pocetDavok != null && aktualnaDavka != null) {
            this.jeKompletna = aktualnaDavka >= pocetDavok;
        }
    }
    
    private void updateDatumDalsejDavky() {
        if (!Boolean.TRUE.equals(jeKompletna) && datumVakciny != null && intervalMedziDavkami != null) {
            this.datumDalsejDavky = datumVakciny.plusDays(intervalMedziDavkami);
        }
    }
    
    // Metóda na získanie plného mena
    public String getPlneMeno() {
        return menoOsoby + " " + priezviskoOsoby;
    }
    
    // Metóda na získanie progressu vakcinácie ako string
    public String getProgressVakciny() {
        if (pocetDavok == null || aktualnaDavka == null) {
            return "1/1";
        }
        return aktualnaDavka + "/" + pocetDavok;
    }
    
    // Metóda na získanie zostávajúcich dní do ďalšej dávky
    public Long getZostavajuceDniDoDalsejDavky() {
        if (datumDalsejDavky == null || Boolean.TRUE.equals(jeKompletna)) {
            return null;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), datumDalsejDavky);
    }
    
    @Override
    public String toString() {
        return "OsobaVakcinaDTO{" +
                "id=" + id +
                ", osobaId=" + osobaId +
                ", vakcinaId=" + vakcinaId +
                ", datumVakciny=" + datumVakciny +
                ", nazovVakciny='" + nazovVakciny + '\'' +
                ", menoOsoby='" + menoOsoby + '\'' +
                ", priezviskoOsoby='" + priezviskoOsoby + '\'' +
                ", pocetDavok=" + pocetDavok +
                ", aktualnaDavka=" + aktualnaDavka +
                ", intervalMedziDavkami=" + intervalMedziDavkami +
                ", datumDalsejDavky=" + datumDalsejDavky +
                ", jeKompletna=" + jeKompletna +
                '}';
    }
}