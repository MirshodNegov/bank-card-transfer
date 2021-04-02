package uz.pdp.lesson4homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.lesson4homework.entity.Outcome;

public interface OutcomeRepository extends JpaRepository<Outcome,Integer> {
}
