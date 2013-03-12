'use strict';

angular.module('quizzoApp').controller('PlayersPendingCtrl', function ($rootScope, $scope, $location) {

  $rootScope.enablePolling();
  // our exit condition...
  $scope.$on('WaitingForAnswer', function () {
    $location.path('/play');
  });
});
