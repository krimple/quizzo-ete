'use strict';

angular.module('quizzoApp').
 controller('QuestionCtrl', function($scope, QuizManagerService) {

  $scope.question = QuizManagerService.getCurrentQuestion().question;

  $scope.$on('newquestion', function() {
    $scope.question = QuizManagerService.getCurrentQuestion().question;
  });

});
