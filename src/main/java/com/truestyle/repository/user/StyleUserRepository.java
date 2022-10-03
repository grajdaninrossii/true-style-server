package com.truestyle.repository.user;

import com.truestyle.entity.user.StyleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StyleUserRepository extends JpaRepository<StyleUser, Long> {

    List<StyleUser> findAll();
}
