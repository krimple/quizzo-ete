'use strict';

angular.module('quizzoApp').controller('QuestionPendingCtrl', function ($scope, $timeout, $location, QuizManagerService) {

  var timeout;
  // our exit condition...
  $scope.$on('WaitingForAnswer', function() {
    $timeout.cancel(timeout);
    $location.path('/play');
  });

  $scope.onTimeout = function() {
    QuizManagerService.getStatus();
    timeout = $timeout($scope.onTimeout, 1000);
  };

  timeout = $timeout($scope.onTimeout, 1000);

});
