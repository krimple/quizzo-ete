package org.springframework.samples.async.quizzo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.examples.quizzo.PlayerService;
import org.springframework.data.examples.quizzo.domain.Player;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created with IntelliJ IDEA.
 * User: kenrimple
 * Date: 2/20/13
 * Time: 8:11 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/player/*")
public class PlayerController {

    private PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @RequestMapping(method= RequestMethod.GET, value="searchFor/{nickName}")
    public ResponseEntity<Integer> searchForNickName(String nickName) {
        /*
        TODO - add repository impl and map/reduce or other mechanism to count in mongo
        this is horribly inefficient and in the wrong place.
        Also see https://jira.springsource.org/browse/DATACMNS-195.
        */
        int count = playerService.countPlayersByNamePattern(nickName);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.GET, value="{nickName}")
    public @ResponseBody Player getUserByNickName(String nickName) {
        // TODO - fix when more than one returned or not any returned
        return playerService.fetchUniquePlayerByName(nickName);
    }
}
