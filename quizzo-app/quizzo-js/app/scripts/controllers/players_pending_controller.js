'use strict';

angular.module('quizzoApp').controller('PlayersPendingCtrl', function ($scope, $routeParams, $location, QuizManagerService) {

  var gameId = $routeParams.gameId;
  // our exit condition...
  $scope.$on('ReadyToPlay', function() {
    $timeout.cancel(timeout);
    $location.path('/players_pending/' + gameId);

  });

  $scope.onTimeout = function() {
    QuizManagerService.checkReadyToPlay(gameId);
    timeout = $timeout($scope.onTimeout, 1000);
  }
  timeout = $timeout($scope.onTimeout, 1000);

});