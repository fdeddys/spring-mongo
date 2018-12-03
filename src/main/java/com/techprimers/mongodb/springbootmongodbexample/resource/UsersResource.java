package com.techprimers.mongodb.springbootmongodbexample.resource;

import com.techprimers.mongodb.springbootmongodbexample.document.Users;
import com.techprimers.mongodb.springbootmongodbexample.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/users")
public class UsersResource {

    private UserRepository userRepository;

    @Autowired private MongoTemplate mongoTemplate;

    public UsersResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    public List<Users> getAll() {
        return userRepository.findAll();
    }

    @GetMapping("id/{id}")
    public Users getById(@PathVariable("id") Long id){
        return userRepository.findById(id);
    }

    @GetMapping("name/{name}")
    public Users getByName(@PathVariable("name") String name){
        return userRepository.findByNameLike(name);
    }

    @GetMapping("/all/page")
    public Page<Users> getAllPage() {
        PageRequest pageRequest = new PageRequest(0,1);
        return userRepository.findByNameLike("%a%", pageRequest);
    }

    // query starting char name
    // ^name
    // query last char name
    // name$
    @GetMapping("nameQ/{name}")
    public Page<Users> getByNameQuery(@PathVariable("name") String name){
        PageRequest pageRequest = new PageRequest(0,10);

        Query query = new Query();
        query.addCriteria(Criteria.where("name").regex("^"+name));
        query.with(new Sort(Sort.Direction.ASC,"id"));
        query.with(pageRequest);

        List<Users> resList = mongoTemplate.find(query, Users.class);
        Page<Users> usersPage = PageableExecutionUtils.getPage(
                resList,
                pageRequest,
                () -> mongoTemplate.count(query, Users.class));

        return usersPage;
    }

    @GetMapping("salaryQ/{sal}")
    public List<Users> getBySalaryQuery(@PathVariable("sal") Long sal){
        Query query = new Query();
        query.addCriteria(Criteria.where("salary").gte(sal) );
        return mongoTemplate.find(query, Users.class);
    }

}
