package com.ibm.interac.db.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.interac.domain.User;

public interface UserRepository extends MongoRepository<User, String>
{

}
