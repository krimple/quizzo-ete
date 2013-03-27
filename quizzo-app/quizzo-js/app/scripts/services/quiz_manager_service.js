'use strict';

angular.module('quizzoApp').factory('quizManagerService', function ($log, $http, $rootScope, serverPrefix) {

  // TODO - move playerAndGameInformation into shared resource so invalid_game_status can check whether 
  // we have a valid nickname on the server...
  var implementation = {}, registerPending = false;

  implementation.question = {};

  // used as a delta to indicate a state change between polls
  implementation.previousStatus = 'NoStatus';

  implementation.pollerLoop = function() {
    var that = this;
    // watch for === with typeof - the type is not the same so == returns true but === doesn't...
    // ah, the 'good' parts :) (kjr) - this will throw a typecheck error in jshint/jslint
    if (typeof $rootScope.playerAndGameInformation == 'undefined') {
      that.whoAmI();
      return;
    }
    // now that we're in an object, all the subtypes if null return a type of object. 
    // so you can't test for that. Instead, you check for null and you CAN use === again
    if ($rootScope.playerAndGameInformation.playerNickName === null) {
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
    if ($rootScope.playerAndGameInformation.playerNickName !== null  &&
        $rootScope.playerAndGameInformation.gameId === null) {
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
    $http.get(serverPrefix + 'status').success(function (data, status, headers, config) {
      switch (data.status) {
        case 'WaitingToPlay':
        $rootScope.$broadcast('WaitingToPlay');
        break;
        case 'WaitingForAnswer':
        if (that.previousStatus !== 'WaitingForAnswer') {
          that.question = data.question;
          $rootScope.$broadcast('WaitingForAnswer');
        }
        break;
        case 'WaitingForNextQuestion':
        if (that.previousStatus !== 'WaitingForNextQuestion') {
          $rootScope.$broadcast('WaitingForNextQuestion');
        }
        break;
        case 'GameComplete':
        if (that.previousStatus !== 'GameComplete') {
          $rootScope.$broadcast('GameComplete');
        }
        break;
        case 'InvalidGameStatus':
        $log.error('Game status invalid.', status, headers, config);
        $rootScope.$broadcast('InvalidGameStatus');
        break;
      }
      that.previousStatus = data.status;
    }).error(function (data, status, headers, config) {
      $log.error('error, not a proper status returned...', data);
      $log.error('other data', status, headers, config);
      $rootScope.$broadcast('InvalidGameStatus');
    });
  };

  implementation.getCurrentQuestion = function () {
    return this.question;
  };

  implementation.getPlayerAndGameInformation = function() {
    return $rootScope.playerAndGameInformation;
  };

  implementation.whoAmI = function() {
      $http.get(serverPrefix + 'quizRun/whoami').success(function(data, status, headers, config) {
          // data contains playerNickName and gameId
          $rootScope.playerAndGameInformation = data;
      }).error(function(data, status, headers, config) {
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
