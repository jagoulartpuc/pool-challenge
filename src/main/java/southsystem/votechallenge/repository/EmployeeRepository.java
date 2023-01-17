package southsystem.votechallenge.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import southsystem.votechallenge.domain.Employee;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {
}
