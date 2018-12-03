package com.techprimers.mongodb.springbootmongodbexample.repository;

import com.techprimers.mongodb.springbootmongodbexample.document.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<Users, Integer> {

    Page<Users> findByNameLike(String name, Pageable pageable);
    Users findById(Long id);
    Users findByNameLike(String name);
}

