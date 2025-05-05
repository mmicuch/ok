package su.umb.prog3.demo.demo.persistence.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import su.umb.prog3.demo.demo.persistence.entity.Vakcina;

public interface VakcinaRepository extends JpaRepository<Vakcina, Long> {
}