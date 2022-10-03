package com.truestyle.entity.stuff;

import com.truestyle.entity.user.Gender;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@MappedSuperclass
@Getter
@Setter
public class BillingStuff {

    @Column(nullable = false)
    private String productDisplayName;

    // Гендер
    @ManyToOne
    @JoinColumn(name = "gender_id", referencedColumnName = "id")
    private Gender gender;

    @Column(name = "master_category")
    private String masterCategory;

    @Column(name = "article_type", length = 50, nullable = false)
    private String articleType;

    @Column(name = "base_color", length = 50)
    private String baseColor;

    @Column(length = 20)
    private String season;

    @Column(length = 20)
    private String usage;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
