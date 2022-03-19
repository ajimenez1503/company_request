package com.company_request;

import org.springframework.data.mongodb.repository.MongoRepository;

interface RequestRepository extends MongoRepository<Request, String> {
}