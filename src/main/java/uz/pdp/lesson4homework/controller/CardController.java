package uz.pdp.lesson4homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.lesson4homework.entity.Card;
import uz.pdp.lesson4homework.payload.ApiResponse;
import uz.pdp.lesson4homework.payload.CardDto;
import uz.pdp.lesson4homework.service.CardService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/card")
public class CardController {
    @Autowired
    CardService cardService;

    @PostMapping
    public HttpEntity<?> add(@RequestBody CardDto cardDto, HttpServletRequest request) {
        ApiResponse apiResponse = cardService.add(cardDto, request);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 209).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getAll(HttpServletRequest request){
        return ResponseEntity.ok(cardService.getAll(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card>getOne(@PathVariable Integer id, HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(cardService.getOne(id, httpServletRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id, HttpServletRequest httpServletRequest){
        ApiResponse response = cardService.delete(id, httpServletRequest);
        return ResponseEntity.status(response.isSuccess()?202:409).body(response);
    }
}
