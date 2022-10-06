package com.footballers.footballers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class Controller {
    

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    // get a single player
    @GetMapping("/players/{id}")
    public Player getSinglePlayer(@PathVariable int id) {
        return playerRepository.findById(id).get();
    }

    // Get the team with all its players

    @GetMapping("/teams/{id}")
    public ResponseEntity<Team> getTeamWithPlayers(@PathVariable int id) {
        Optional<Team> match = teamRepository.findById(id);
        if (match.isPresent()) {
            return new ResponseEntity<>(match.get(), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found");
        }
    }

    // - Get the total score of a team. Do not include the replacement in the score

    @GetMapping("/teams/{id}/score")
    public int getTeamScore(@PathVariable int id) {
        Optional<Team> match = teamRepository.findById(id);
        if (match.isPresent()) {
            Team team = match.get();
            List<Player> players = team.players;
            int totalScore = 0;
            for (Player player : players) {
                if (!player.isReplacement) {
                    totalScore += player.scoreOutOfTen;
                }
            }
            return totalScore;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found");
        }
    }

    // - Add a person to a team
    @PostMapping("/teams/{teamId}/players")
    public Player createPlayer(@RequestBody Player player, @PathVariable int teamId) {
        player.team = teamRepository.findById(teamId).get();
        return playerRepository.save(player);
    }

    // - Remove a person from a team
    @DeleteMapping("/teams/{teamId}/players/{playerId}")
    public void deletePlayer(@PathVariable int teamId, @PathVariable int playerId) {
        Player player = playerRepository.findById(playerId).get();
        player.team = null;
        playerRepository.save(player);
    }

    @PatchMapping("/players/{id}")
    public Player updatePlayer(@RequestBody Player player, @PathVariable int id) {
        Optional<Player> match = playerRepository.findById(id);
        if(match.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found");
        }

        Player playerToUpdate = match.get();

        if(playerToUpdate.scoreOutOfTen < player.scoreOutOfTen){
            playerToUpdate.scoreOutOfTen = player.scoreOutOfTen;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Score must be higher than current score");
        }

        return playerRepository.save(playerToUpdate);
    }
}

interface PlayerRepository extends JpaRepository<Player, Integer> {
}

interface TeamRepository extends JpaRepository<Team, Integer> {
}
