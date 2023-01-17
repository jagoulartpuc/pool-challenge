package southsystem.votechallenge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import southsystem.votechallenge.domain.Employee;
import southsystem.votechallenge.repository.EmployeeRepository;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    public Employee insertEmployee(Employee employee) {
        return employeeRepository.insert(employee);
    }

    public Employee getByCPF(String cpf) {
        return employeeRepository.findById(cpf).orElseThrow();
    }

}
