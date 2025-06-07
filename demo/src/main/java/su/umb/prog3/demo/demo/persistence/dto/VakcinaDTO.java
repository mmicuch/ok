package su.umb.prog3.demo.demo.persistence.dto;

public class VakcinaDTO {
    private Long id;
    private String nazov;
    private String popis;
    private String vyrobca;
    
    // Nové polia pre podporu viacerých dávok
    private Integer defaultPocetDavok;
    private Integer defaultIntervalMedziDavkami; // v dňoch
    private String pokyny; // pokyny pre podávanie
    private Boolean jeAktivna;
    
    // Konštruktory
    public VakcinaDTO() {
        this.defaultPocetDavok = 1;
        this.jeAktivna = true;
    }
    
    public VakcinaDTO(Long id, String nazov, String popis, String vyrobca) {
        this.id = id;
        this.nazov = nazov;
        this.popis = popis;
        this.vyrobca = vyrobca;
        this.defaultPocetDavok = 1;
        this.jeAktivna = true;
    }
    
    // Rozšírený konštruktor
    public VakcinaDTO(Long id, String nazov, String popis, String vyrobca, 
                     Integer defaultPocetDavok, Integer defaultIntervalMedziDavkami, 
                     String pokyny, Boolean jeAktivna) {
        this.id = id;
        this.nazov = nazov;
        this.popis = popis;
        this.vyrobca = vyrobca;
        this.defaultPocetDavok = defaultPocetDavok != null ? defaultPocetDavok : 1;
        this.defaultIntervalMedziDavkami = defaultIntervalMedziDavkami;
        this.pokyny = pokyny;
        this.jeAktivna = jeAktivna != null ? jeAktivna : true;
    }
    
    // Gettery a settery
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNazov() { return nazov; }
    public void setNazov(String nazov) { this.nazov = nazov; }
    
    public String getPopis() { return popis; }
    public void setPopis(String popis) { this.popis = popis; }
    
    public String getVyrobca() { return vyrobca; }
    public void setVyrobca(String vyrobca) { this.vyrobca = vyrobca; }
    
    // Nové gettery a settery
    public Integer getDefaultPocetDavok() { return defaultPocetDavok; }
    public void setDefaultPocetDavok(Integer defaultPocetDavok) { 
        this.defaultPocetDavok = defaultPocetDavok != null ? defaultPocetDavok : 1; 
    }
    
    public Integer getDefaultIntervalMedziDavkami() { return defaultIntervalMedziDavkami; }
    public void setDefaultIntervalMedziDavkami(Integer defaultIntervalMedziDavkami) { 
        this.defaultIntervalMedziDavkami = defaultIntervalMedziDavkami; 
    }
    
    public String getPokyny() { return pokyny; }
    public void setPokyny(String pokyny) { this.pokyny = pokyny; }
    
    public Boolean getJeAktivna() { return jeAktivna; }
    public void setJeAktivna(Boolean jeAktivna) { this.jeAktivna = jeAktivna; }
    
    // Pomocné metódy
    public boolean vyyzadujeViacDavok() {
        return defaultPocetDavok != null && defaultPocetDavok > 1;
    }
    
    public String getInfoODavkach() {
        if (defaultPocetDavok == null || defaultPocetDavok <= 1) {
            return "Jednorazová vakcína";
        }
        
        String info = defaultPocetDavok + " dávok";
        if (defaultIntervalMedziDavkami != null) {
            info += " (interval: " + defaultIntervalMedziDavkami + " dní)";
        }
        return info;
    }
    
    @Override
    public String toString() {
        return "VakcinaDTO{" +
                "id=" + id +
                ", nazov='" + nazov + '\'' +
                ", popis='" + popis + '\'' +
                ", vyrobca='" + vyrobca + '\'' +
                ", defaultPocetDavok=" + defaultPocetDavok +
                ", defaultIntervalMedziDavkami=" + defaultIntervalMedziDavkami +
                ", pokyny='" + pokyny + '\'' +
                ", jeAktivna=" + jeAktivna +
                '}';
    }
}