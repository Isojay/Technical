package com.example.DemoTest.Repository;

import com.example.DemoTest.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,String> {

    User findByPhNumber(String number);

    User findByEmail(String email);
}
