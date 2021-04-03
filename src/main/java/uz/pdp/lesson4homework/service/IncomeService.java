package uz.pdp.lesson4homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.lesson4homework.entity.Income;
import uz.pdp.lesson4homework.payload.ApiResponse;
import uz.pdp.lesson4homework.repository.IncomeRepository;
import uz.pdp.lesson4homework.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IncomeService {
    @Autowired
    IncomeRepository incomeRepository;
    @Autowired
    JwtProvider jwtProvider;


    public List<Income> getAll(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        return incomeRepository.findAllByFromCardUsername(userName);
    }

    public ApiResponse getOne(Integer id, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        Optional<Income> optionalIncome = incomeRepository.findById(id);
        if (!optionalIncome.isPresent())
            return new ApiResponse("Income not found by given id !", false);
        if (!userName.equals(optionalIncome.get().getTo_card().getUsername()))
            return new ApiResponse("This income doesn't not belong to you !", false);
        return new ApiResponse("OK", true,optionalIncome.get());
    }
}
