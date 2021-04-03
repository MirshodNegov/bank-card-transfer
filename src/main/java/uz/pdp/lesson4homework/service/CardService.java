package uz.pdp.lesson4homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.lesson4homework.entity.Card;
import uz.pdp.lesson4homework.payload.ApiResponse;
import uz.pdp.lesson4homework.payload.CardDto;
import uz.pdp.lesson4homework.repository.CardRepository;
import uz.pdp.lesson4homework.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {
    @Autowired
    CardRepository cardRepository;
    @Autowired
    JwtProvider jwtProvider;

    public ApiResponse add(CardDto cardDto, HttpServletRequest request) {
        try{
            String token = request.getHeader("Authorization");
            token = token.substring(7);
            String userNameFromToken = jwtProvider.getUserNameFromToken(token);
            Card card=new Card();
            card.setUsername(userNameFromToken);
            card.setCardNumber(cardDto.getCardNumber());
            card.setExpiredDate(cardDto.getExpireDate());
            cardRepository.save(card);
            return new ApiResponse("Your new card added !",true);
        }catch (Exception e){
            return new ApiResponse("Something is wrong !",false);
        }

    }

    public List<Card> getAll(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        return cardRepository.findAllByUsername(userName);
    }

    public Card getOne(Integer id, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        Optional<Card> optionalCard = cardRepository.findById(id);
        if (!optionalCard.isPresent()) return null;
        if (!userName.equals(optionalCard.get().getUsername()))
            return null;
        return optionalCard.orElse(null);
    }

    public ApiResponse delete(Integer id, HttpServletRequest httpServletRequest) {
        try {
            String token = httpServletRequest.getHeader("Authorization");
            token = token.substring(7);
            String userNameFromToken = jwtProvider.getUserNameFromToken(token);

            Optional<Card> optionalCard = cardRepository.findById(id);
            if (!optionalCard.isPresent())
                return new ApiResponse("Card not found with given id !",false);
            if (!userNameFromToken.equals(optionalCard.get().getUsername()))
                return new ApiResponse("This card doesn't belong to you !",false);
            cardRepository.deleteById(id);
            return new ApiResponse("Card successfully deleted !",true);
        }catch (Exception exception){
            return new ApiResponse("Something went wrong !",false);
        }
    }
}
