package com.truestyle.entity.user;


import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "style_user")
public class StyleUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 5)
    @Column(length = 20, nullable = false)
    private String name;
}
