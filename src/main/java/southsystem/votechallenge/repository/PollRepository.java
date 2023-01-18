package southsystem.votechallenge.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import southsystem.votechallenge.domain.Poll;

@Repository
public interface PollRepository extends MongoRepository<Poll, String> {

}
