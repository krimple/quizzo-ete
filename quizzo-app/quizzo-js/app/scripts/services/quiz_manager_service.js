'use strict';

angular.module('quizzoApp').factory('QuizManagerService', function ($http, $rootScope, $location, serverPrefix) {

  var implementation = {};
  implementation.question = {};

  // used as a delta to indicate a state change between polls
  implementation.previousStatus = 'NoStatus';

    implementation.getStatus = function () {
      var that = this;
      $http.get(serverPrefix + "status").
        success(function (data, status, headers, config) {
//          if (data.status == 'WaitingToPlay') {
//            // we just wait... do nothing and return
//            return;
//          }
          if (data.status == 'WaitingForAnswer' &&
              that.previousStatus != 'WaitingForAnswer') {
            // we just switched to getting a new question -
            // tell the world
            that.question = data.question;
            $rootScope.$broadcast('WaitingForAnswer');
          } else if (data.status == 'WaitingForNextQuestion' &&
                     that.previousStatus != 'WaitingForNextQuestion') {
            $rootScope.$broadcast('WaitingForNextQuestion');
          } else if (data.status == 'GameComplete' &&
                     that.previousStatus != 'GameComplete') {
            $rootScope.question.delete();
            // todo
            $location.path('/game_over');
          } else if (data.status == 'InvalidGameStatus') {
            // todo
            $location.path('/invalid_game_status');
          }
          // save off this status as the new previous status...
          that.previousStatus = data.status;
        }).
        error(function (data, status, headers, config) {
          $location.path('/invalid_game_status');
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
