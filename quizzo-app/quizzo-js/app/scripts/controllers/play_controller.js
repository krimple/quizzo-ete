'use strict';

angular.module('quizzoApp').controller('PlayCtrl',
  function ($scope, $location, $timeout, QuizManagerService) {

    var timeout;
    
    // define our 'end of question time' event
    $scope.$on('WaitingForNextQuestion', function () {
      // see definition below
      $timeout.cancel(timeout);

      $scope.question.destroy();
      // show panel waiting for next question and begin polling
      $location.path('/question_pending');
    });

    $scope.castVote = function () {
      var selectedChoice = $scope.choices.item().value;
      if (selectedChoice) {
        // we send along the current question # to make sure we are recording
        // the vote on the right question - if it doesn't match the one in
        // server state, throw it away
        QuizManagerService.vote(selectedChoice, $scope.question.questionNumber);
      }
    };

    // our timer loop - check for question timeout or end of game
    $scope.onTimeout = function() {
      QuizManagerService.getStatus();
      timeout = $timeout($scope.onTimeout, 1000);
    };

    // fetch our question
    $scope.question = QuizManagerService.getCurrentQuestion();

    // begin timer
    timeout = $timeout($scope.onTimeout, 1000);


  });
