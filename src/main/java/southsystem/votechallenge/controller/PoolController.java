package southsystem.votechallenge.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import southsystem.votechallenge.domain.Pool;
import southsystem.votechallenge.service.PoolService;

import java.util.List;

@RestController
@RequestMapping("/pool")
public class PoolController {

    @Autowired
    private PoolService poolService;

    @PostMapping
    public Pool postPool(@RequestBody Pool pool) {
        return poolService.createPool(pool);
    }

    @GetMapping("/{id}")
    public Pool getPool(@PathVariable String id) {
        return poolService.getPoolById(id);
    }

    @GetMapping
    public List<Pool> getAllPools() {
        return poolService.getAllPools();
    }

    @PutMapping("/vote/{id}")
    public ResponseEntity<String> voteInPool(@PathVariable String id, @RequestParam String cpf, @RequestParam String vote) {
        try {
            return ResponseEntity.ok(poolService.voteInPool(id, cpf, vote));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

}