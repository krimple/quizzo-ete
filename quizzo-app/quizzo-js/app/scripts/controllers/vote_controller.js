'use strict';

angular.module('quizzoApp').controller('VoteCtrl', function($scope, $location, QuizManagerService) {
  $scope.answer_choices = QuizManagerService.getCurrentQuestion().choices;

  $scope.$on('newquestion', function() {
    $scope.answer_choices = QuizManagerService.getCurrentQuestion().choices;
  });


  // this would actually call the service that communicated the vote
  $scope.castVote = function () {
    var selectedChoice = voteForm.choices.item().value;
    if (selectedChoice) {
      console.log("voting"); 
      QuizManagerService.vote(selectedChoice);
      var moreQuestions = QuizManagerService.nextQuestion();
      if (!moreQuestions) {
        $location.path('/bye');
      }
    } else {
      console.log('no answer set');
      // TODO - how to show this? maybe we hide the voting button?
    }
  };
});
