package southsystem.votechallenge.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import southsystem.votechallenge.domain.Employee;
import southsystem.votechallenge.domain.Pool;
import southsystem.votechallenge.repository.PoolRepository;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class PoolService {

    @Autowired
    private PoolRepository poolRepository;

    @Autowired
    private HttpSession session;

    public Pool createPool(Pool pool) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable closePool = () -> {
            pool.setOpened(false);
            poolRepository.save(pool);
        };
        executor.schedule(closePool, pool.getDuration(), TimeUnit.MINUTES);
        return poolRepository.insert(pool);
    }

    public Pool getPoolById(String id) {
        return poolRepository.findById(id).orElseThrow();
    }

    public List<Pool> getAllPools() {
        return poolRepository.findAll();
    }

    public String voteInPool(String id, String cpf, String vote) {
        Employee employee = (Employee) session.getAttribute("employee");
        Pool pool = poolRepository.findById(id).orElseThrow();
        verifyExistingVote(cpf, vote, employee, pool);

        return "Vote computed!";
    }

    private void verifyExistingVote(String cpf, String vote, Employee employee, Pool pool) {
        if (!pool.getEmployeesWhoVoted().contains(employee.getCpf())) {
            verifyOpenedPool(cpf, vote, pool);
        } else {
            throw new RuntimeException("This employee has already voted.");
        }
    }

    private void verifyOpenedPool(String cpf, String vote, Pool pool) {
        if (pool.isOpened()) {
            computeVote(vote, cpf, pool);
        } else {
            throw new RuntimeException("This pool is already closed.");
        }
    }

    private void computeVote(String vote, String cpf, Pool pool) {
        if (vote.equalsIgnoreCase("yes")) {
            int countYes = pool.getCountYes();
            countYes++;
            pool.setCountYes(countYes);
        } else if (vote.equalsIgnoreCase("no")) {
            int countNo = pool.getCountNo();
            countNo++;
            pool.setCountNo(countNo);
        } else {
            throw new NullPointerException("Option do not exist.");
        }
        pool.getEmployeesWhoVoted().add(cpf);
        poolRepository.save(pool);
    }

}