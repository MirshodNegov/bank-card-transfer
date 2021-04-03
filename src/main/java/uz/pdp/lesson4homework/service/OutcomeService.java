package uz.pdp.lesson4homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uz.pdp.lesson4homework.entity.Card;
import uz.pdp.lesson4homework.entity.Income;
import uz.pdp.lesson4homework.entity.Outcome;
import uz.pdp.lesson4homework.payload.ApiResponse;
import uz.pdp.lesson4homework.payload.OutcomeDto;
import uz.pdp.lesson4homework.repository.CardRepository;
import uz.pdp.lesson4homework.repository.IncomeRepository;
import uz.pdp.lesson4homework.repository.OutcomeRepository;
import uz.pdp.lesson4homework.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OutcomeService {
    @Autowired
    OutcomeRepository outcomeRepository;
    @Autowired
    IncomeRepository incomeRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    JwtProvider jwtProvider;

    public ApiResponse add(OutcomeDto outcomeDto, HttpServletRequest request) {
        Optional<Card> optionalFromCard = cardRepository.findById(outcomeDto.getFromCardId());
        if (!optionalFromCard.isPresent())
            return new ApiResponse("From Card not found !", false);
        Optional<Card> optionalToCard = cardRepository.findById(outcomeDto.getToCardId());
        if (!optionalToCard.isPresent())
            return new ApiResponse("To Card not found !", false);
        Card fromCard = optionalFromCard.get();
        Card toCard = optionalToCard.get();

        String token = request.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        if (!userName.equals(fromCard.getUsername()))
            return new ApiResponse("The card you sending from does not belong to you", false);
        double balance = fromCard.getBalance();
        double totalAmount = outcomeDto.getAmount() + (outcomeDto.getAmount() / 100 * outcomeDto.getCommission());
        if (balance<totalAmount)
            return new ApiResponse("Your card balance is not enough !",false);

        Outcome outcome = new Outcome();
        Income income = new Income();

        outcome.setAmount(outcomeDto.getAmount());
        outcome.setCommission(outcomeDto.getCommission());
        outcome.setFrom_card(fromCard);
        outcome.setTo_card(toCard);
        outcome.setExpireDate(new Date());
        outcomeRepository.save(outcome);

        income.setAmount(outcome.getAmount());
        income.setExpireDate(new Date());
        income.setFrom_card(fromCard);
        income.setTo_card(toCard);
        incomeRepository.save(income);

        fromCard.setBalance(fromCard.getBalance()-totalAmount);
        toCard.setBalance(toCard.getBalance()+outcomeDto.getAmount());
        cardRepository.save(fromCard);
        cardRepository.save(toCard);

        return new ApiResponse("Outcome successfully sent !",true);
    }

    public List<Outcome> getAll(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        return outcomeRepository.findAllByFromCardUsername(userName);
    }

    public ApiResponse getOne(Integer id, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        Optional<Outcome> optionalOutcome = outcomeRepository.findById(id);
        if (!optionalOutcome.isPresent()) return new ApiResponse("Outcome not found by given id !", false);
        if (!userName.equals(optionalOutcome.get().getFrom_card().getUsername()))
            return new ApiResponse("This income doesn't not belong to you !", false);
        return new ApiResponse("OK", true,optionalOutcome.get());
    }
}
