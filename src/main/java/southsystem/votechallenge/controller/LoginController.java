package southsystem.votechallenge.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import southsystem.votechallenge.domain.Employee;
import southsystem.votechallenge.service.EmployeeService;

@RestController
public class LoginController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public ResponseEntity<String> login(HttpSession session, String cpf) {
        Employee employee = employeeService.getByCPF(cpf);
        try {
            session.setAttribute("employee", employee);
            return ResponseEntity.ok().body("Logged in!");
        } catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
