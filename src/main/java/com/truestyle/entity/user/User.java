package com.truestyle.entity.user;

import com.truestyle.entity.stuff.ShopStuff;
import com.truestyle.entity.stuff.UserStuff;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor // Для того, чтобы Спринг смог создать бин
@AllArgsConstructor
@Table(name = "users",
       uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
               @UniqueConstraint(columnNames = "email")
       })
public class User {

    // id пользователя
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    // username имя пользователя
    @NotEmpty(message = "Username should not be empty")
    @Size(min = 2, max = 35, message = "Name should be between 2 and 35 characters")
    @Column(nullable = false)
    private String username;

    // email пользователя
    @NotEmpty(message = "Email should not be empty")
    @Column(nullable = false)
    @Email(message = "Email should be valid")
    private String email;

    // Ссылка на пароли
    @NotEmpty(message = "Password should not be empty")
    @Column(nullable = false)
    private String password;

    // Хранение номера телефона
    @Column(length = 4)
    private String code_number1;

    @Column(length = 4)
    private String code_number2;

    @Column(length = 7)
    private String number;

    @Column(name="full_number", length = 15)
    private String fullNumber;

    // Гендер
    @ManyToOne
    @JoinColumn(name = "gender_id", referencedColumnName = "id")
    private Gender gender;

    // Цитата пользователя
    @ManyToOne
    @JoinColumn(name="style_user_id", referencedColumnName = "id")
    private StyleUser styleUser;

    // Страна
    @Column
    @Size(min=2, max=120, message = "Country should be between 2 and 120 characters")
    private String country;

    // Фото(ссылка на фото)
    @Column(name = "photo_url")
    private String photoUrl;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "likes_stuff",
             joinColumns = @JoinColumn(name = "user_id"),
             inverseJoinColumns = @JoinColumn(name = "stuff_id"))
    private Set<ShopStuff> likesShopsStuff = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "wardrobe_from_shops",
             joinColumns = @JoinColumn(name = "user_id"),
             inverseJoinColumns = @JoinColumn(name = "shops_stuff_id"))
    private Set<ShopStuff> wardrobeShops = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "wardrobe_from_users",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "users_stuff_id"))
    private Set<UserStuff> wardrobeUsers = new HashSet<>();

    public User(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void likeShopsStuff(ShopStuff shopsStuff){
        likesShopsStuff.add(shopsStuff);
    }

    public void dislikeShopsStuff(ShopStuff shopsStuff){
        likesShopsStuff.remove(shopsStuff);
    }

    public Boolean addShopsStuff(ShopStuff shopsStuff){
        return wardrobeShops.add(shopsStuff);
    }

    public Boolean deleteShopsStuff(ShopStuff shopsStuff) {
        return wardrobeShops.remove(shopsStuff);
    }

    public Boolean addUsersStuff(UserStuff usersStuff){
        return wardrobeUsers.add(usersStuff);
    }

    public Boolean deleteUsersStuff(UserStuff usersStuff) {
        return wardrobeUsers.remove(usersStuff);
    }


//    // Токен авторизации
//    @OneToOne
//    @JoinColumn(name = "token_id", referencedColumnName = "id")
//    private Token token;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}