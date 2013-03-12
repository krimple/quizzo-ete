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
        if (that.previousState !== 'GameComplete') {
          delete $rootScope.question;
          $rootScope.$broadcast('GameComplete');
        }
        break;
        case 'InvalidGameStatus':
        $rootScope.$broadcast('InvalidGameStatus');
        break;
      }
      that.previousStatus = data.status;
    }).error(function (data, status, headers, config) {
      console.log('error, not a proper status returned...', data);
      $rootScope.$broadcast('InvalidGameStatus');
    });
  };

  implementation.getCurrentQuestion = function () {
    return this.question;
  };

  implementation.vote = function (selectedAnswer, sentQuestionNumber) {
    var answerPayload = {
      questionNumber : sentQuestionNumber,
      choice: selectedAnswer.value
    };
    $http.put(serverPrefix + 'quizRun/submitAnswer', answerPayload);
  };
  return implementation;
});
