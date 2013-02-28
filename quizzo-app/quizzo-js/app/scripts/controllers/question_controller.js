'use strict';

angular.module('quizzoApp').
 controller('QuestionCtrl', function($scope, $routeParams, QuizManagerService) {

  QuizManagerService.selectGamebyId($routeParams.gameId);

  $scope.question = QuizManagerService.getCurrentQuestion().question;

  $scope.$on('newquestion', function() {
    $scope.question = QuizManagerService.getCurrentQuestion().question;
  });

});
