package uz.pdp.lesson4homework.payload;

import lombok.Data;

import java.util.Date;

@Data
public class CardDto {
    private String cardNumber;
    private Date expireDate;
}
