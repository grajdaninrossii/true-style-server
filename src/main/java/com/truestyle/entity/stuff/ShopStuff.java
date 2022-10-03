package com.truestyle.entity.stuff;

import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class ShopStuff extends BillingStuff{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String storeLink;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ShopStuff shopsStuff = (ShopStuff) o;
        return id != null && Objects.equals(id, shopsStuff.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
