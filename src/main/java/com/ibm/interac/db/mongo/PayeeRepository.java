package com.ibm.interac.db.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.interac.domain.Payee;

public interface PayeeRepository extends MongoRepository<Payee, String>
{

}
