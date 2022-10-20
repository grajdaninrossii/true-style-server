package com.truestyle.service.app;

import com.truestyle.entity.app.Quote;
import com.truestyle.repository.app.QuoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class QuoteService {

    private final QuoteRepository quoteRepository;

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
