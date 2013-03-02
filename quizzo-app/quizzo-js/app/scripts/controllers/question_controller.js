'use strict';

angular.module('quizzoApp').
 controller('QuestionCtrl', function($scope, $routeParams, QuizManagerService) {

  $scope.question = QuizManagerService.getCurrentQuestion().question;

  $scope.$on('WaitingForAnswer', function() {
    $scope.question = QuizManagerService.getCurrentQuestion();
  });

});
