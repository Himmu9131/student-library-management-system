package com.demo.example.student_library_management_system.converters;

import com.demo.example.student_library_management_system.dto.CardRequestDto;
import com.demo.example.student_library_management_system.model.Card;

public class CardConverter {

    public static Card convertCardRequestDtoToCard(CardRequestDto cardRequestDto){

        Card card=Card.builder().cardStatus(cardRequestDto.getCardStatus()).build();
        return card;
    }
}
