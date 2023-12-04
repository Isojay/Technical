package com.example.DemoTest.Repository;

import com.example.DemoTest.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {

    User findByPhNumber(String number);

}
