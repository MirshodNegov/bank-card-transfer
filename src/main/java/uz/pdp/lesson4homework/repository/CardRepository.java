package uz.pdp.lesson4homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.lesson4homework.entity.Card;

import java.util.List;

public interface CardRepository extends JpaRepository<Card,Integer> {
    List<Card> findAllByUsername(String username);
}
