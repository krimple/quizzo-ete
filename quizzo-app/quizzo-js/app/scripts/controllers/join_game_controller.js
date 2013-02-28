'use strict';

angular.module('quizzoApp').controller('JoinGameCtrl',
  function ($rootScope, $routeParams, $location, GameSelectionService) {

    var gameId = $routeParams.gameId;

    $rootScope.$on("GameJoined", function() {
      $location.path('/play/' + gameId);
    });

    GameSelectionService.joinGame(gameId);

  });