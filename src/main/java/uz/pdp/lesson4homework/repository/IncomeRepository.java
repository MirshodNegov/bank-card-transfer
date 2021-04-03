package uz.pdp.lesson4homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.lesson4homework.entity.Income;

import java.util.List;

public interface IncomeRepository extends JpaRepository<Income,Integer> {
    @Query(value = "select * from income inc join card ca on inc.to_card_id=ca.id where ca.username=:par",nativeQuery = true)
    List<Income> findAllByFromCardUsername(@Param("par") String username);
}
