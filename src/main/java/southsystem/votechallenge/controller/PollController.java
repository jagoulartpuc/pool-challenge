package southsystem.votechallenge.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import southsystem.votechallenge.domain.Poll;
import southsystem.votechallenge.service.PollService;

import java.util.List;

@RestController
@RequestMapping("/poll")
public class PollController {

    @Autowired
    private PollService pollService;

    @PostMapping
    public Poll postPoll(@RequestBody Poll poll) {
        return pollService.createPool(poll);
    }

    @CrossOrigin
    @GetMapping("/opened")
    public Poll getOpenedPoll() {
        return pollService.getOpenedPoll();
    }

    @CrossOrigin
    @GetMapping("/last")
    public Poll getLastPoll() {
        return pollService.getLastPoll();
    }

    @GetMapping
    public List<Poll> getAllPools() {
        return pollService.getAllPools();
    }

    @CrossOrigin(origins = {"http://3.87.228.36:3000"}, allowedHeaders = "*", allowCredentials = "true")
    @PutMapping
    public ResponseEntity<String> voteInPoll(@RequestParam String vote) {
        try {
            return ResponseEntity.ok(pollService.voteInPoll(vote));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

}