'use strict';

angular.module('quizzoApp').controller('PlayCtrl', function ($scope, QuizManagerService) {
  $scope.question = QuizManagerService.getCurrentQuestion().text;
});
