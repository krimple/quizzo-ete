'use strict';

angular.module('quizzoApp').factory('QuizManagerService', function ($http, $rootScope, serverPrefix) {

  var implementation = {};
  implementation.question = {};

    implementation.getStatus = function () {
      var that = this;
      $http.get(serverPrefix + "status").
        success(function (data, status, headers, config) {
          if (data.status == 'WaitingToPlay') {
            // we just wait... do nothing and return
            return;
          }
          if (data.status == 'WaitingForAnswer') {
            that.question = data.question;
            $rootScope.$broadcast('WaitingForAnswer');
            return;
          }
          if (data.status == 'WaitingForNextQuestion') {
            $rootScope.$broadcast('WaitingForNextQuestion');
            return;
          }
          if (data.status == 'GameComplete') {
            delete $rootScope.question;
            // todo
            $rootScope.$broadcast('GameComplete');
            return;
          }
          if (data.status == 'InvalidGameStatus') {
            // todo
            $rootScope.$broadcast('InvalidGameStatus');
            return;
          }
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
