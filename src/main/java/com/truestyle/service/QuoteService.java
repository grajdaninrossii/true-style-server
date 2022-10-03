package com.truestyle.service;

import com.truestyle.entity.app.Quote;
import com.truestyle.repository.app.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class QuoteService {

    @Autowired
    private QuoteRepository quoteRepository;


    private Quote quoteOfDay;
    private Date dateQuote;

    // Обновление цитаты, только раз в день
    public Quote getRandomQuote(){
        Date newDateQuote = new Date();
        if (quoteOfDay == null && dateQuote == null ||
                (newDateQuote.getTime() - dateQuote.getTime() > 1000 * 3600 * 24)) {
            quoteOfDay = quoteRepository.findOneRandom();
            dateQuote = newDateQuote;
        }
        return quoteOfDay;
    }
}
