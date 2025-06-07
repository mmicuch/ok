package su.umb.prog3.demo.demo.persistence.dto;

import java.time.LocalDate;
import java.util.List;

public class VakcinaciaRequestDTO {
    private Long osobaId;
    private Long vakcinaId;
    private LocalDate datumPrvejDavky;
    private String miestoAplikacie;
    private String batchCislo;
    private String poznamky;
    
    // Pre existujúce vakcíny s definovanou schémou
    private boolean pouziSchemu = false;
    
    // Pre custom schému (ak admin chce definovať vlastnú)
    private boolean customSchema = false;
    private int celkovyPocetDavok = 1;
    private List<Integer> casoveRozostupy; // v dňoch medzi dávkami
    private List<String> nazovyDavok;
    
    // Pre pridanie ďalšej dávky do existujúcej vakcinácie
    private Long existujucaVakcinaciaId;
    private boolean jeDalsaDavka = false;
    private int poradieDavky = 1;

    public VakcinaciaRequestDTO() {
        // default constructor
    }

    // Getters and Setters
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

    public LocalDate getDatumPrvejDavky() {
        return datumPrvejDavky;
    }

    public void setDatumPrvejDavky(LocalDate datumPrvejDavky) {
        this.datumPrvejDavky = datumPrvejDavky;
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

    public String getPoznamky() {
        return poznamky;
    }

    public void setPoznamky(String poznamky) {
        this.poznamky = poznamky;
    }

    public boolean isPouziSchemu() {
        return pouziSchemu;
    }

    public void setPouziSchemu(boolean pouziSchemu) {
        this.pouziSchemu = pouziSchemu;
    }

    public boolean isCustomSchema() {
        return customSchema;
    }

    public void setCustomSchema(boolean customSchema) {
        this.customSchema = customSchema;
    }

    public int getCelkovyPocetDavok() {
        return celkovyPocetDavok;
    }

    public void setCelkovyPocetDavok(int celkovyPocetDavok) {
        this.celkovyPocetDavok = celkovyPocetDavok;
    }

    public List<Integer> getCasoveRozostupy() {
        return casoveRozostupy;
    }

    public void setCasoveRozostupy(List<Integer> casoveRozostupy) {
        this.casoveRozostupy = casoveRozostupy;
    }

    public List<String> getNazovyDavok() {
        return nazovyDavok;
    }

    public void setNazovyDavok(List<String> nazovyDavok) {
        this.nazovyDavok = nazovyDavok;
    }

    public Long getExistujucaVakcinaciaId() {
        return existujucaVakcinaciaId;
    }

    public void setExistujucaVakcinaciaId(Long existujucaVakcinaciaId) {
        this.existujucaVakcinaciaId = existujucaVakcinaciaId;
    }

    public boolean isJeDalsaDavka() {
        return jeDalsaDavka;
    }

    public void setJeDalsaDavka(boolean jeDalsaDavka) {
        this.jeDalsaDavka = jeDalsaDavka;
    }

    public int getPoradieDavky() {
        return poradieDavky;
    }

    public void setPoradieDavky(int poradieDavky) {
        this.poradieDavky = poradieDavky;
    }

    // Validačné metódy
    public boolean isValidForNovuVakcinaciu() {
        return osobaId != null && vakcinaId != null && datumPrvejDavky != null && !jeDalsaDavka;
    }

    public boolean isValidForDalsiaDavka() {
        return existujucaVakcinaciaId != null && jeDalsaDavka && poradieDavky > 1;
    }

    public boolean isValidCustomSchema() {
        return customSchema && celkovyPocetDavok > 1 && 
               casoveRozostupy != null && casoveRozostupy.size() == (celkovyPocetDavok - 1);
    }
}