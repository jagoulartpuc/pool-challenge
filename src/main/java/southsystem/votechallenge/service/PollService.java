package southsystem.votechallenge.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import southsystem.votechallenge.domain.Employee;
import southsystem.votechallenge.domain.Poll;
import southsystem.votechallenge.repository.PollRepository;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class PollService {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private HttpSession session;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper mapper;

    public Poll createPool(Poll poll) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(closePool(poll), poll.getDuration(), TimeUnit.MINUTES);
        if (!openedPoolExists()) {
            return pollRepository.insert(poll);
        }
        return null;
    }

    private boolean openedPoolExists() {
        try {
            getOpenedPoll();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Runnable closePool(Poll poll) {
        Poll pollUpdated = getLastPoll();
        return () -> {
            poll.setOpened(false);
            poll.setCountYes(pollUpdated.getCountYes());
            poll.setCountNo(pollUpdated.getCountNo());
            pollRepository.save(poll);
            try {
                rabbitTemplate.convertAndSend("pool-results", mapper.writeValueAsString(poll));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public Poll getPoolById(String id) {
        return pollRepository.findById(id).orElseThrow();
    }

    public List<Poll> getAllPools() {
        return pollRepository.findAll();
    }

    public String voteInPoll(String vote) {
        Employee employee = (Employee) session.getAttribute("employee");
        Poll poll = getOpenedPoll();
        verifyExistingVote(vote, employee, poll);

        return "Vote computed!";
    }

    private void verifyExistingVote(String vote, Employee employee, Poll poll) {
        String cpf = employee.getCpf();
        if (!poll.getEmployeesWhoVoted().contains(cpf)) {
            verifyOpenedPoll(cpf, vote, poll);
        } else {
            throw new RuntimeException("This employee has already voted.");
        }
    }

    private void verifyOpenedPoll(String cpf, String vote, Poll poll) {
        if (poll.isOpened()) {
            computeVote(vote, cpf, poll);
        } else {
            throw new RuntimeException("This pool is already closed.");
        }
    }

    private void computeVote(String vote, String cpf, Poll poll) {
        if (vote.equalsIgnoreCase("yes")) {
            int countYes = poll.getCountYes();
            countYes++;
            poll.setCountYes(countYes);
        } else if (vote.equalsIgnoreCase("no")) {
            int countNo = poll.getCountNo();
            countNo++;
            poll.setCountNo(countNo);
        } else {
            throw new RuntimeException("Option does not exist.");
        }
        poll.getEmployeesWhoVoted().add(cpf);
        pollRepository.save(poll);
    }

    public Poll getOpenedPoll() {
        return pollRepository.findAll()
                .stream()
                .filter(Poll::isOpened)
                .findFirst()
                .orElseThrow();
    }

    public Poll getLastPoll() {
        return pollRepository.findAll().stream().reduce((first, second) -> second).get();
    }
}