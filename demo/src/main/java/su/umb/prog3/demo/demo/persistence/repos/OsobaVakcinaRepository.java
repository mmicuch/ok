package su.umb.prog3.demo.demo.persistence.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import su.umb.prog3.demo.demo.persistence.entity.OsobaVakcina;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OsobaVakcinaRepository extends JpaRepository<OsobaVakcina, Long> {
    
    /**
     * Nájde všetky vakcinácie pre konkrétnu osobu
     */
    List<OsobaVakcina> findByOsobaId(Long osobaId);
    
    /**
     * Nájde všetky vakcinácie pre konkrétnu vakcínu
     */
    List<OsobaVakcina> findByVakcinaId(Long vakcinaId);
    
    /**
     * Nájde všetky vakcinácie pre osobu a vakcínu (všetky dávky)
     */
    List<OsobaVakcina> findByOsobaIdAndVakcinaIdOrderByPoradieDavky(Long osobaId, Long vakcinaId);
    
    /**
     * Nájde konkrétnu dávku pre osobu a vakcínu
     */
    Optional<OsobaVakcina> findByOsobaIdAndVakcinaIdAndPoradieDavky(Long osobaId, Long vakcinaId, int poradieDavky);
    
    /**
     * Nájde všetky nedokončené vakcinácie (potrebujú ďalšie dávky)
     */
    @Query("SELECT ov FROM OsobaVakcina ov WHERE ov.jeDokoncena = false")
    List<OsobaVakcina> findNedokonceneVakcinacie();
    
    /**
     * Nájde vakcinácie s nadchádzajúcimi dávkami (pre notifikácie)
     */
    @Query("SELECT ov FROM OsobaVakcina ov WHERE ov.datumPlanovanejDalsejDavky IS NOT NULL " +
           "AND ov.datumPlanovanejDalsejDavky >= :dnes ORDER BY ov.datumPlanovanejDalsejDavky ASC")
    List<OsobaVakcina> findNadchadzajuceDavky(@Param("dnes") LocalDate dnes);
    
    /**
     * Nájde vakcinácie s prekročeným termínom ďalšej dávky
     */
    @Query("SELECT ov FROM OsobaVakcina ov WHERE ov.datumPlanovanejDalsejDavky IS NOT NULL " +
           "AND ov.datumPlanovanejDalsejDavky < :dnes AND ov.jeDokoncena = false")
    List<OsobaVakcina> findPrekroceneTerminy(@Param("dnes") LocalDate dnes);
    
    /**
     * Nájde vakcinácie s termínom ďalšej dávky v nasledujúcich N dňoch
     */
    @Query("SELECT ov FROM OsobaVakcina ov WHERE ov.datumPlanovanejDalsejDavky BETWEEN :dnes AND :maxDatum " +
           "ORDER BY ov.datumPlanovanejDalsejDavky ASC")
    List<OsobaVakcina> findDavkyVNasledujucichDnoch(@Param("dnes") LocalDate dnes, @Param("maxDatum") LocalDate maxDatum);
    
    /**
     * Nájde poslednú dávku pre konkrétnu vakcináciu osoby
     */
    @Query("SELECT ov FROM OsobaVakcina ov WHERE ov.osoba.id = :osobaId AND ov.vakcina.id = :vakcinaId " +
           "ORDER BY ov.poradieDavky DESC LIMIT 1")
    Optional<OsobaVakcina> findTopByOsobaIdAndVakcinaIdOrderByPoradieDavkyDesc(Long osobaId, Long vakcinaId);    
    /**
     * Spočíta počet dokončených vakcinácií pre osobu
     */
    @Query("SELECT COUNT(DISTINCT ov.vakcina.id) FROM OsobaVakcina ov WHERE ov.osoba.id = :osobaId AND ov.jeDokoncena = true")
    long countDokonceneVakcinaciePreOsobu(@Param("osobaId") Long osobaId);
    
    /**
     * Nájde všetky vakcinácie aplikované v konkrétnom dátumovom rozmedzí
     */
    List<OsobaVakcina> findByDatumAplikacieBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Nájde vakcinácie podľa batch čísla
     */
    List<OsobaVakcina> findByBatchCislo(String batchCislo);
    
    /**
     * Nájde vakcinácie podľa miesta aplikácie
     */
    List<OsobaVakcina> findByMiestoAplikacie(String miestoAplikacie);
    
    /**
     * Štatistiky - počet aplikovaných dávok pre každú vakcínu
     */
    @Query("SELECT ov.vakcina.nazov, COUNT(ov) FROM OsobaVakcina ov GROUP BY ov.vakcina.nazov")
    List<Object[]> getStatistikyPodlaVakciny();
    
    /**
     * Štatistiky - počet dokončených vs nedokončených vakcinácií
     */
    @Query("SELECT ov.jeDokoncena, COUNT(DISTINCT CONCAT(ov.osoba.id, '-', ov.vakcina.id)) FROM OsobaVakcina ov GROUP BY ov.jeDokoncena")
    List<Object[]> getStatistikyDokoncenia();
}