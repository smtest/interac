package com.ibm.interac.db.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.interac.domain.Transfer;

public interface TransferRepository extends MongoRepository<Transfer, String>
{
}
