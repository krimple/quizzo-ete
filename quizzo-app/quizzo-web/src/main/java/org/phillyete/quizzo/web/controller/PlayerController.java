package org.phillyete.quizzo.web.controller;

import javax.servlet.http.HttpSession;

import org.phillyete.quizzo.service.PlayerService;
import org.phillyete.quizzo.domain.Player;
import org.phillyete.quizzo.web.engine.PlayerGameSession;
import org.phillyete.quizzo.exception.PlayerAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/player")
public class PlayerController extends AbstractQuizController {

    private PlayerService playerService;

    @Autowired
     public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @RequestMapping(method = RequestMethod.GET, value="{nickName}")
    public @ResponseBody Player getUserByNickName(String nickName) {
        // TODO - fix when more than one returned or not any returned
        return playerService.fetchUniquePlayerByName(nickName);
    }

    @RequestMapping(method = RequestMethod.POST, value="register/{nickName}")
    public @ResponseBody ResponseEntity<Player>
        registerUserByNickName(HttpSession session, @PathVariable String nickName) {

        final Player player;
        final ResponseEntity<Player> responseEntity;

        try {
            player = playerService.registerPlayer(nickName);
        } catch (PlayerAlreadyExistsException p) {
            responseEntity = new ResponseEntity<Player>(HttpStatus.NO_CONTENT);
            return responseEntity;
        }

        PlayerGameSession playerGameSession = getOrCreatePlayerGameSession(session);
        playerGameSession.setPlayerId(player.getName());
        //updatePlayerGameSession(session, playerGameSession);

        responseEntity = new ResponseEntity<Player>(player, HttpStatus.CREATED);
        return responseEntity;
    }
}
