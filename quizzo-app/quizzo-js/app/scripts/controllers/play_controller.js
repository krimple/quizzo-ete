'use strict';

angular.module('quizzoApp').controller('PlayCtrl', function ($scope, $rootScope, playerAndGameDetailsService, quizManagerService) {


  $rootScope.castVote = function (selectedChoice) {
    console.log('selected choice is', selectedChoice);
    $scope.enableVoting = false;
    if (selectedChoice) {
      // we send along the current question # to make sure we are recording
      // the vote on the right question - if it doesn't match the one in
      // server state, throw it away
      quizManagerService.vote(selectedChoice, $scope.question.questionNumber);
    }
  };

  // fetch our question from the cached copy in our service
  $scope.question = playerAndGameDetailsService.getQuestion();
  $scope.enableVoting = true;

});
