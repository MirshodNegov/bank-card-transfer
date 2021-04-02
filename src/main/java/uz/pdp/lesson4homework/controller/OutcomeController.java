package uz.pdp.lesson4homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.lesson4homework.payload.ApiResponse;
import uz.pdp.lesson4homework.payload.OutcomeDto;
import uz.pdp.lesson4homework.service.IncomeService;
import uz.pdp.lesson4homework.service.OutcomeService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/outcome")
public class OutcomeController {
    @Autowired
    OutcomeService outcomeService;

    @PostMapping
    public HttpEntity<ApiResponse> add(@RequestBody OutcomeDto outcomeDto,HttpServletRequest request){
        ApiResponse apiResponse=outcomeService.add(outcomeDto,request);
        return ResponseEntity.status(apiResponse.isSuccess()?201:209).body(apiResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAll(HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(outcomeService.getAll(httpServletRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?>getOne(@PathVariable Integer id, HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(outcomeService.getOne(id, httpServletRequest));
    }
}
