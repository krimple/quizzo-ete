'use strict';

angular.module('quizzoApp').
  factory('GameSelectionService', function (serverPrefix, $rootScope, $http) {
    var implementation = {};

    // update this when we find data
    implementation.games_ready_to_play = [];

    implementation.findGamesReadyToPlay = function () {
      $http.get(serverPrefix + 'quizRun/games').success(
        function (data, status, headers, config) {
          $rootScope.gamesAvailable = data;
          $rootScope.$broadcast('GamesAvailable');
        }).error(
        function (data, status, headers, config) {
          console.error('no data found.', status);
        });
    };

    implementation.joinGame = function (gameId) {
      // let's go!
      $http.post(serverPrefix + 'quizRun/game/' + gameId + '/joinGame').
        success(function (data, status, headers, config) {
          // response includes category : 'GameJoined' - we're in
          if (data.category == 'GameJoined') {
            $rootScope.$broadcast('GameJoined');
          } else {
            // todo - not sure what to do here... Need a generic error view/controller
          }
        }).
        error(function (data, status, headers, config) {
          $rootScope.$broadcast('GameNotJoined');
        });
      implementation.gameId = gameId;
    };

    return implementation;

  });

