package southsystem.votechallenge.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import southsystem.votechallenge.domain.Pool;

@Repository
public interface PoolRepository extends MongoRepository<Pool, String> {

}
