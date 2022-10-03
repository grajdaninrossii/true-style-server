package com.truestyle.entity.app;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="quotes")
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String value;
    @Column
    private String Author;

    @Override
    public String toString() {
        return "Quote{" +
                "id=" + id +
                ", content_='" + value + '\'' +
                ", Author='" + Author + '\'' +
                '}';
    }
}
