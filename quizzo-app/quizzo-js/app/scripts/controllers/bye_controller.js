angular.module('quizzoApp').
  controller('ByeCtrl', function($scope, QuizManagerService) {
  $scope.statistics = {
    "score": QuizManagerService.getScore(),
    "maxScore": "150"
  };
});
