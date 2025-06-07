package su.umb.prog3.demo.demo.persistence.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import su.umb.prog3.demo.demo.persistence.entity.VakcinaDavka;

import java.util.List;

@Repository
public interface VakcinaDavkaRepository extends JpaRepository<VakcinaDavka, Long> {
    
    /**
     * Nájde všetky dávky pre konkrétnu schému vakcíny
     */
    List<VakcinaDavka> findByVakcinaSchemaIdOrderByPoradieDavky(Long vakcinaSchemaId);
    
    /**
     * Nájde konkrétnu dávku podľa schémy a poradia
     */
    VakcinaDavka findByVakcinaSchemaIdAndPoradieDavky(Long vakcinaSchemaId, int poradieDavky);
    
    /**
     * Nájde nasledujúcu dávku v poradí
     */
    @Query("SELECT vd FROM VakcinaDavka vd WHERE vd.vakcinaSchema.id = :schemaId AND vd.poradieDavky = :poradie + 1")
    VakcinaDavka findNasledujucuDavku(@Param("schemaId") Long schemaId, @Param("poradie") int poradie);
    
    /**
     * Nájde všetky povinné dávky pre danú schému
     */
    List<VakcinaDavka> findByVakcinaSchemaIdAndJePovinneTrue(Long vakcinaSchemaId);
    
    /**
     * Spočíta počet dávok v schéme
     */
    long countByVakcinaSchemaId(Long vakcinaSchemaId);
}