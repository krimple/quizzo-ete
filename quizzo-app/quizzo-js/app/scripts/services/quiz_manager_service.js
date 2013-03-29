'use strict';

angular.module('quizzoApp').factory('quizManagerService', function ($log, $http, playerAndGameDetailsService, $rootScope, serverPrefix) {

  // TODO - move playerAndGameInformation into shared resource so invalid_game_status can check whether 
  // we have a valid nickname on the server...
  var implementation = {}, registerPending = false;

  // used as a delta to indicate a state change between polls
  implementation.pollerLoop = function() {
    var that = this;
    // do we have a player nickname? No, check the server!
    if (!playerAndGameDetailsService.hasPlayerNickName()) {
      that.whoAmI();
      if (!registerPending) {
        // keep us from refreshing over and over again
        registerPending = true;
        // ask to register
        $rootScope.$broadcast('RegisterNickName');
      }
      return;
    }

    // now we check to see if we have a nickname, but don't 
    // have a game - go to the game join view if so 
    if (playerAndGameDetailsService.hasPlayerNickName() &&
        !playerAndGameDetailsService.hasGameInformation()) {
      $rootScope.$broadcast('JoinGame');
      // reset register pending condition
      registerPending = false;
    } else {
      // we're in a game, poll it
      that.getStatus();
    }
  };

  implementation.getStatus = function () {
    var that = this;
    $http.get(serverPrefix + 'status').
     success(function (data, status, headers, config) {
      switch (data.status) {
        case 'WaitingToPlay':
        $rootScope.$broadcast('WaitingToPlay');
        break;
        case 'WaitingForAnswer':
        if (that.previousStatus !== 'WaitingForAnswer') {
          playerAndGameDetailsService.setQuestion(data.question);
        }
        // always broadcast - we compare the question 
        // id to see if we have it already on the other side
        $rootScope.$broadcast('WaitingForAnswer');
        
        break;
        case 'WaitingForNextQuestion':
        if (that.previousStatus !== 'WaitingForNextQuestion') {
          // wipe out last question data so we can't accidentally submit it again
          playerAndGameDetailsService.setQuestion(null);
          $rootScope.$broadcast('WaitingForNextQuestion');
        }
        break;
        case 'GameComplete':
        if (that.previousStatus !== 'GameComplete') {
          playerAndGameDetailsService.clearGameData();
          $rootScope.$broadcast('GameComplete');
        }
        break;
        case 'InvalidGameStatus':
        $log.error('Game status invalid.', status, headers, config);
        playerAndGameDetailsService.clearGameData();
        playerAndGameDetailsService.clearUserData();
        $rootScope.$broadcast('InvalidGameStatus');
        break;
      }
      that.previousStatus = data.status;
    }).error(function (data, status, headers, config) {
      playerAndGameDetailsService.clearGameData();
      playerAndGameDetailsService.clearUserData();
      $log.error('error, not a proper status returned...', data);
      $log.error('other data', status, headers, config);
      $rootScope.$broadcast('InvalidGameStatus');
    });
  };

  implementation.whoAmI = function() {
      $http.get(serverPrefix + 'quizRun/whoami').
       success(function(data, status, headers, config) {
          // data contains playerNickName and gameId (empty if not set yet)
          playerAndGameDetailsService.adaptWhoAmIData(data);
      }).error(function(data, status, headers, config) {
          playerAndGameDetailsService.clearUserData();
          $log.error('failed to get information on player and game');
          $rootScope.$broadcast('WhoAmIFailed');
      });
  };

  implementation.vote = function (selectedAnswer, sentQuestionNumber) {
    var answerPayload = {
      questionNumber : sentQuestionNumber,
      choice: selectedAnswer
    };
    $http.put(serverPrefix + 'quizRun/submitAnswer',
              answerPayload).success(function(data, status, headers, config) {
      $rootScope.$broadcast('VoteSent');
    }).error(function(data, status, headers, config) {
      $log.error('failed to submit vote.', status);
      $rootScope.$broadcast('VoteFailed');
    });
  };
  return implementation;
});
