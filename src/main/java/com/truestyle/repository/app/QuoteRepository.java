package com.truestyle.repository.app;

import com.truestyle.entity.app.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteRepository  extends JpaRepository<Quote, Long> {

    @Query(value = "select * from quotes order by random() limit 1",
            nativeQuery = true)
    Quote findOneRandom();

}
