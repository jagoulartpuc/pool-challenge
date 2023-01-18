package southsystem.votechallenge.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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

    @Autowired
    private MongoTemplate mongoTemplate;

    private static Logger LOGGER = LoggerFactory.getLogger(PollService.class);

    public Poll createPool(Poll poll) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(closePool(poll.getId()), poll.getDuration(), TimeUnit.MINUTES);
        if (!openedPoolExists()) {
            LOGGER.info("New poll created: " + poll.getId());
            return pollRepository.insert(poll);
        }
        LOGGER.info("A pool is already opened: " + poll.getId());
        return null;
    }

    private Runnable closePool(String pollId) {
        return () -> {
            Query query = new Query(Criteria.where("id").is(pollId));
            Update update = new Update().set("opened", false);
            mongoTemplate.updateFirst(query, update, Poll.class);
            LOGGER.info("Poll has been closed:" + pollId);
            try {
                Poll poll = pollRepository.findById(pollId).get();
                rabbitTemplate.convertAndSend("pool-results", mapper.writeValueAsString(poll));
                LOGGER.info("The poll was sent to rabbit queue: " + poll.getId());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
    }


    private boolean openedPoolExists() {
        try {
            getOpenedPoll();
            return true;
        } catch (Exception e) {
            return false;
        }
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
            computeVote(vote, cpf, poll);
        } else {
            LOGGER.error("This employee has already voted: " + employee.getCpf() + " in poll: " + poll.getId());
            throw new RuntimeException("This employee has already voted.");
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
            LOGGER.error("Option does not exist.");
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
                .orElseThrow(this::noOpenedPoll);
    }

    private RuntimeException noOpenedPoll() {
        LOGGER.error("No opened poll found.");
        return new RuntimeException("No opened poll found.");
    }

    public Poll getLastPoll() {
        return pollRepository.findAll().stream().reduce((first, second) -> second).get();
    }
}