package southsystem.votechallenge.service;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.springframework.boot.test.context.SpringBootTest;
import southsystem.votechallenge.domain.Employee;
import southsystem.votechallenge.domain.Poll;
import southsystem.votechallenge.repository.PollRepository;

import java.util.List;

@SpringBootTest
class PollServiceTest {
    @Mock
    private PollRepository pollRepository;
    @Mock
    private HttpSession session;
    @InjectMocks
    private PollService pollService;

    @Test
    void creatPollShouldCreateNewPollSuccessfully() {
        Poll poll = new Poll("1", "Querem happy hour na sexta-feira?", 5);
        Mockito.when(pollRepository.insert(poll)).thenReturn(poll);
        Poll returnedPoll = pollService.createPool(poll);
        assertEquals(poll, returnedPoll);
    }

    @Test
    void getPollByIdShouldReturnPollSuccessfully() {
        Poll poll = new Poll("1", "Querem happy hour na sexta-feira?", 5);
        Mockito.when(pollRepository.findById("123")).thenReturn(java.util.Optional.of(poll));
        Poll returnedPoll = pollService.getPoolById("123");
        assertEquals(poll, returnedPoll);
    }

    @Test
    void voteInPoolShouldComputeVoteSuccessfully() {
        Employee employee = new Employee();
        employee.setCpf("123");
        Mockito.when(session.getAttribute("employee")).thenReturn(employee);
        Poll poll = new Poll("1", "Querem happy hour na sexta-feira?", 5);
        Mockito.when(pollRepository.findAll()).thenReturn(List.of(poll));

        String result = pollService.voteInPoll("yes");
        assertEquals("Vote computed!", result);
        assertEquals(1, poll.getCountYes());
        assertTrue(poll.getEmployeesWhoVoted().contains("123"));
    }

    @Test
    void voteInPoolShouldThrowEmployeeVotingMoreThanOnceError() {
        Employee employee = new Employee();
        employee.setCpf("123");
        Mockito.when(session.getAttribute("employee")).thenReturn(employee);
        Poll poll = new Poll("1", "Querem happy hour na sexta-feira?", 5);
        Mockito.when(pollRepository.findAll()).thenReturn(List.of(poll));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            pollService.voteInPoll("yes");
            pollService.voteInPoll("yes");
        }, "This employee has already voted.");
        assertEquals("This employee has already voted.", thrown.getMessage());
    }

    @Test
    void voteInPoolShouldThrowClosedPollError() {
        Employee employee = new Employee();
        employee.setCpf("123");
        Mockito.when(session.getAttribute("employee")).thenReturn(employee);
        Poll poll = new Poll("1", "Querem happy hour na sexta-feira?", 5);
        poll.setOpened(false);
        Mockito.when(pollRepository.findAll()).thenReturn(List.of(poll));

        RuntimeException thrown =
                assertThrows(RuntimeException.class, () -> pollService.voteInPoll("yes"),
                        "No opened poll found.");
        assertEquals("No opened poll found.", thrown.getMessage());
    }

    @Test
    void voteInPoolShouldThrowOptionDoesntExistError() {
        Employee employee = new Employee();
        employee.setCpf("123");
        Mockito.when(session.getAttribute("employee")).thenReturn(employee);
        Poll poll = new Poll("1", "Querem happy hour na sexta-feira?", 5);
        Mockito.when(pollRepository.findAll()).thenReturn(List.of(poll));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> pollService.voteInPoll("xxx"),
                "Option does not exist.");
        assertEquals("Option does not exist.", thrown.getMessage());
    }

}