package uz.pdp.lesson4homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.lesson4homework.entity.Outcome;

import java.util.List;

public interface OutcomeRepository extends JpaRepository<Outcome,Integer> {
    @Query(value = "select * from outcome inc join card ca on inc.from_card_id=ca.id where ca.username=:par",nativeQuery = true)
    List<Outcome> findAllByFromCardUsername(@Param("par") String username);
}
