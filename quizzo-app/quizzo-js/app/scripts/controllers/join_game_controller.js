'use strict';

angular.module('quizzoApp').controller('JoinGameCtrl', function ($scope, $routeParams, $location, gameSelectionService) {

    var gameId = $routeParams.gameId;

    $scope.$on('GameJoined', function() {
      $location.path('/players_pending');
    });

    gameSelectionService.joinGame(gameId);

  });
