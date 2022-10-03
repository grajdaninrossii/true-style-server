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
public class UserStuff extends BillingStuff {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserStuff usersStuff = (UserStuff) o;
        return id != null && Objects.equals(id, usersStuff.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
