package su.umb.prog3.demo.demo.persistence.dto;

import su.umb.prog3.demo.demo.persistence.entity.OsobaVakcina;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class NotifikaciaDTO {
    private Long osobaVakcinaId;
    private String osobaMeno;
    private String osobaPriezvisko;
    private String vakcinaNazov;
    private String vakcinaTyp;
    private int aktualnaeDavka;
    private int nasledujucaDavka;
    private LocalDate datumPlanovanejDalsejDavky;
    private int pocetZostupujucichDni;
    private boolean jePrekroceny;
    private String poznamky;

    public NotifikaciaDTO() {
        // default constructor
    }

    public NotifikaciaDTO(OsobaVakcina entity) {
        this.osobaVakcinaId = entity.getId();
        this.osobaMeno = entity.getOsoba() != null ? entity.getOsoba().getMeno() : null;
        this.osobaPriezvisko = entity.getOsoba() != null ? entity.getOsoba().getPriezvisko() : null;
        this.vakcinaNazov = entity.getVakcina() != null ? entity.getVakcina().getNazov() : null;
        this.vakcinaTyp = entity.getVakcina() != null ? entity.getVakcina().getTyp().toString() : null;
        this.aktualnaeDavka = entity.getPoradieDavky();
        this.nasledujucaDavka = entity.getPoradieDavky() + 1;
        this.datumPlanovanejDalsejDavky = entity.getDatumPlanovanejDalsejDavky();
        this.poznamky = entity.getPoznamky();
        
        // Vypočítaj počet zostávajúcich dní
        if (datumPlanovanejDalsejDavky != null) {
            this.pocetZostupujucichDni = (int) ChronoUnit.DAYS.between(LocalDate.now(), datumPlanovanejDalsejDavky);
            this.jePrekroceny = pocetZostupujucichDni < 0;
        } else {
            this.pocetZostupujucichDni = 0;
            this.jePrekroceny = false;
        }
    }

    // Getters and Setters
    public Long getOsobaVakcinaId() {
        return osobaVakcinaId;
    }

    public void setOsobaVakcinaId(Long osobaVakcinaId) {
        this.osobaVakcinaId = osobaVakcinaId;
    }

    public String getOsobaMeno() {
        return osobaMeno;
    }

    public void setOsobaMeno(String osobaMeno) {
        this.osobaMeno = osobaMeno;
    }

    public String getOsobaPriezvisko() {
        return osobaPriezvisko;
    }

    public void setOsobaPriezvisko(String osobaPriezvisko) {
        this.osobaPriezvisko = osobaPriezvisko;
    }

    public String getVakcinaNazov() {
        return vakcinaNazov;
    }

    public void setVakcinaNazov(String vakcinaNazov) {
        this.vakcinaNazov = vakcinaNazov;
    }

    public String getVakcinaTyp() {
        return vakcinaTyp;
    }

    public void setVakcinaTyp(String vakcinaTyp) {
        this.vakcinaTyp = vakcinaTyp;
    }

    public int getAktualnaeDavka() {
        return aktualnaeDavka;
    }

    public void setAktualnaeDavka(int aktualnaeDavka) {
        this.aktualnaeDavka = aktualnaeDavka;
    }

    public int getNasledujucaDavka() {
        return nasledujucaDavka;
    }

    public void setNasledujucaDavka(int nasledujucaDavka) {
        this.nasledujucaDavka = nasledujucaDavka;
    }

    public LocalDate getDatumPlanovanejDalsejDavky() {
        return datumPlanovanejDalsejDavky;
    }

    public void setDatumPlanovanejDalsejDavky(LocalDate datumPlanovanejDalsejDavky) {
        this.datumPlanovanejDalsejDavky = datumPlanovanejDalsejDavky;
        
        // Prepočítaj počet dní pri zmene dátumu
        if (datumPlanovanejDalsejDavky != null) {
            this.pocetZostupujucichDni = (int) ChronoUnit.DAYS.between(LocalDate.now(), datumPlanovanejDalsejDavky);
            this.jePrekroceny = pocetZostupujucichDni < 0;
        }
    }

    public int getPocetZostupujucichDni() {
        return pocetZostupujucichDni;
    }

    public void setPocetZostupujucichDni(int pocetZostupujucichDni) {
        this.pocetZostupujucichDni = pocetZostupujucichDni;
    }

    public boolean isJePrekroceny() {
        return jePrekroceny;
    }

    public void setJePrekroceny(boolean jePrekroceny) {
        this.jePrekroceny = jePrekroceny;
    }

    public String getPoznamky() {
        return poznamky;
    }

    public void setPoznamky(String poznamky) {
        this.poznamky = poznamky;
    }

    // Pomocné metódy pre frontend
    public String getStatusText() {
        if (jePrekroceny) {
            return "Prekročený termín";
        } else if (pocetZostupujucichDni == 0) {
            return "Dnes";
        } else if (pocetZostupujucichDni == 1) {
            return "Zajtra";
        } else if (pocetZostupujucichDni <= 7) {
            return "Tento týždeň";
        } else {
            return "Za " + pocetZostupujucichDni + " dní";
        }
    }

    public String getCeleMeno() {
        return (osobaMeno != null ? osobaMeno : "") + " " + (osobaPriezvisko != null ? osobaPriezvisko : "");
    }
}