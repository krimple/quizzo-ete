'use strict';

angular.module('quizzoApp').factory('quizManagerService', function ($http, $rootScope, serverPrefix) {

  var implementation = {};
  implementation.question = {};

  // used as a delta to indicate a state change between polls
  implementation.previousStatus = 'NoStatus';

    implementation.getStatus = function () {
      var that = this;
      $http.get(serverPrefix + "status").
        success(function (data, status, headers, config) {
          if (data.status == 'WaitingToPlay') {
            // we just wait... do nothing and return
            return;
          }
          if (data.status == 'WaitingForAnswer' &&
              that.previousStatus != 'WaitingForAnswer') {
            // we just switched to getting a new question -
            // tell the world
            that.question = data.question;
            $rootScope.$broadcast('WaitingForAnswer');
          } else if (data.status == 'WaitingForNextQuestion' &&
                     that.previousStatus != 'WaitingForNextQuestion') {
            $rootScope.$broadcast('WaitingForNextQuestion');
            return;
          }
          if (data.status == 'GameComplete' &&
                     that.previousStatus != 'GameComplete') {
            delete $rootScope.question;
            $rootScope.$broadcast('GameComplete');
            return;
          }
          if (data.status == 'InvalidGameStatus') {
            $rootScope.$broadcast('InvalidGameStatus');
             return;
          } else if (data.status == 'GameComplete') {
          }
          // save off this status as the new previous status...
          that.previousStatus = data.status;
        }).
        error(function (data, status, headers, config) {
            $rootScope.$broadcast('InvalidGameStatus');
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
      $http.post(serverPrefix + 'quizRun/submitAnswer', answerPayload);
    };

  return implementation;
});
