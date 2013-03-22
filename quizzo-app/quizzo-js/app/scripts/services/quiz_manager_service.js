'use strict';

angular.module('quizzoApp').factory('quizManagerService', function ($http, $rootScope, serverPrefix) {

  var implementation = {};
  implementation.question = {};

  // used as a delta to indicate a state change between polls
  implementation.previousStatus = 'NoStatus';

  implementation.getStatus = function () {
    var that = this;
    $http.get(serverPrefix + 'status').success(function (data, status, headers, config) {
      switch (data.status) {
        case 'WaitingToPlay':
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
          $rootScope.disablePolling();
        }
        break;
        case 'InvalidGameStatus':
        console.log('Game status invalid.', status, headers, config);
        $rootScope.$broadcast('InvalidGameStatus');
        $rootScope.disablePolling();
        break;
      }
      that.previousStatus = data.status;
    }).error(function (data, status, headers, config) {
      console.log('error, not a proper status returned...', data);
      console.log('other data', status, headers, config);
      $rootScope.$broadcast('InvalidGameStatus');
      $rootScope.disablePolling();
    });
  };

  implementation.getCurrentQuestion = function () {
    return this.question;
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
      $rootScope.$broadcast('VoteFailed');
    });
  };
  return implementation;
});
