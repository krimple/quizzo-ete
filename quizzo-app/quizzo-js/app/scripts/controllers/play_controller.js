'use strict';

angular.module('quizzoApp').controller('PlayCtrl', function ($scope, $rootScope, $location, quizManagerService) {

  $scope.$on('WaitingForAnswer', function () {
    console.log('New question arrived.');
    $scope.question = quizManagerService.getCurrentQuestion();
  });

  // if we vote, we're done... move on to waiting
  $scope.$on('VoteSent', function() {
    $location.path('/question_pending');
  });

  // if we are done, go to end
  $scope.$on('GameComplete', function() {
    $location.path('/bye');
  });

    // define our 'end of question time' event
  $scope.$on('WaitingForNextQuestion', function () {
    // show panel waiting for next question
    $location.path('/question_pending');
  });

  $rootScope.castVote = function (selectedChoice) {
    console.log("selected choice is", selectedChoice);
    $scope.enableVoting = false;
    if (selectedChoice) {
      // we send along the current question # to make sure we are recording
      // the vote on the right question - if it doesn't match the one in
      // server state, throw it away
      quizManagerService.vote(selectedChoice, $scope.question.questionNumber);
    }
  };

  // fetch our question from the cached copy in our service
  $scope.question = quizManagerService.getCurrentQuestion();
  $scope.enableVoting = true;


});
