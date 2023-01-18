package southsystem.votechallenge.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import southsystem.votechallenge.domain.Employee;
import southsystem.votechallenge.repository.EmployeeRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void insertEmployeeShouldCreateNewEmployeeSuccessfully() {
        Employee employee = new Employee();
        employee.setCpf("123");
        employeeService.insertEmployee(employee);
        verify(employeeRepository).insert(employee);
    }

    @Test
    void getByCPFShouldGetEmployee() {
        Employee employee = new Employee();
        employee.setCpf("123");
        when(employeeRepository.findById("123")).thenReturn(Optional.of(employee));
        Employee result = employeeService.getByCPF("123");
        assertEquals(employee, result);
        verify(employeeRepository).findById("123");
    }

    @Test
    void getByCPFShouldThrowException() {
        when(employeeRepository.findById("456")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> employeeService.getByCPF("456"));
    }

}