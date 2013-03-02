'use strict';

angular.module('quizzoApp').controller('JoinGameCtrl', function ($scope, $routeParams, $location, GameSelectionService) {

    var gameId = $routeParams.gameId;

    $scope.$on('GameJoined', function() {
      $location.path('/players_pending');
    });

    GameSelectionService.joinGame(gameId);

  });