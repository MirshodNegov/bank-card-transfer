package uz.pdp.lesson4homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.lesson4homework.entity.Income;

public interface IncomeRepository extends JpaRepository<Income,Integer> {
}
