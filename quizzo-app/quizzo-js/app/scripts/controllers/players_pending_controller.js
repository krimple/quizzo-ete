'use strict';

angular.module('quizzoApp').controller('PlayersPendingCtrl', function ($scope, $timeout, $location, quizManagerService) {

  var timeout;
  // our exit condition...
  $scope.$on('WaitingForAnswer', function() {
    $timeout.cancel(timeout);
    $location.path('/play');
  });

  $scope.onTimeout = function() {
    quizManagerService.getStatus();
    timeout = $timeout($scope.onTimeout, 1000);
  };

  timeout = $timeout($scope.onTimeout, 1000);

});
