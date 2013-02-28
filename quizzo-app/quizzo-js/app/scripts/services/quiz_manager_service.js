'use strict';

angular.module('quizzoApp').factory('QuizManagerService', function ($http, $rootScope, $location, serverPrefix) {

  var implementation = {};

    implementation.getStatus = function () {
      $http.get(serverPrefix + "status").
        success(function (data, status, headers, config) {
          if (data.status == 'WaitingToPlay') {
            // we just wait... do nothing and return
            return;
          }
          if (data.status == 'WaitingForAnswer') {
            $rootScope.question = data.question;
            $rootScope.$broadcast('WaitingForAnswer');
            return;
          }
          if (data.status == 'WaitingForNextQuestion') {
            $rootScope.question.delete();
            $rootScope.$broadcast('WaitingForNextQuestion');
            return;
          }
          if (data.status == 'GameComplete') {
            $rootScope.question.delete();
            // todo
            $location.path('/game_over');
            return;
          }
          if (data.status == 'InvalidGameStatus') {
            // todo
            $location.path('/invalid_game_status');
          }
        }).
        error(function (data, status, headers, config) {
          $location.path('/invalid_game_status');
        });
    }

    implementation.vote = function (selectedAnswer) {
      var answerPayload = {
        questionNumber : $rootScope.question.questionNumber,
        choice: selectedAnswer
      };
      $http.post(serverPrefix + 'quizRun/submitAnswer', answerPayload);
    };

  return implementation;
});
