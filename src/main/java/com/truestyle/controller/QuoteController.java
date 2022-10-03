package com.truestyle.controller;

import com.truestyle.entity.app.Quote;
import com.truestyle.service.QuoteService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/quote")
public class QuoteController {

    //делал ЮГ

    private static Logger log = LogManager.getLogger(TestController.class);
    @Autowired
    private QuoteService quoteService;

    /** Получить рандомную цитату
     *
     * @return вернет рандомную цитату, либо ошибку на стороне сервера
     */
    @GetMapping("/random")
    public Quote Quote(){
        return quoteService.getRandomQuote();
    }

}
