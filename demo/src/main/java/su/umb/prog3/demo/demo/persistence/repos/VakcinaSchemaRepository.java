package su.umb.prog3.demo.demo.persistence.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import su.umb.prog3.demo.demo.persistence.entity.VakcinaSchema;

import java.util.Optional;
import java.util.List;

@Repository
public interface VakcinaSchemaRepository extends JpaRepository<VakcinaSchema, Long> {
    
    /**
     * Nájde schému podľa ID vakcíny
     */
    Optional<VakcinaSchema> findByVakcinaId(Long vakcinaId);
    
    /**
     * Nájde všetky schémy pre aktívne vakcíny
     */
    @Query("SELECT vs FROM VakcinaSchema vs WHERE vs.vakcina.jeAktivna = true")
    List<VakcinaSchema> findAllForAktivneVakciny();
    
    /**
     * Nájde schémy s viac ako jednou dávkou
     */
    @Query("SELECT vs FROM VakcinaSchema vs WHERE vs.celkovyPocetDavok > 1")
    List<VakcinaSchema> findSchemyViacDavok();
    
    /**
     * Skontroluje, či vakcína už má definovanú schému
     */
    boolean existsByVakcinaId(Long vakcinaId);
    
    /**
     * Nájde schémy podľa typu vakcíny
     */
    @Query("SELECT vs FROM VakcinaSchema vs WHERE vs.vakcina.typ = :typ")
    List<VakcinaSchema> findByTypVakciny(@Param("typ") su.umb.prog3.demo.demo.persistence.enums.TypVakciny typ);
}