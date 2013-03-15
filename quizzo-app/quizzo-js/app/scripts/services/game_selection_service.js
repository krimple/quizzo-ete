'use strict';

angular.module('quizzoApp').
  factory('gameSelectionService', function ($http, $rootScope, serverPrefix) {
    var implementation = {};

    // update this when we find data
    implementation.games_ready_to_play = [];

    implementation.findGamesReadyToPlay = function () {
      var that = this;
      $http.get(serverPrefix + 'quizRun/games').
        success(function (data, status, headers, config) {
          that.games_ready_to_play = data;
          $rootScope.$broadcast('GamesAvailable');
        }).error(function (data, status, headers, config) {
          delete that.games_ready_to_play;
          console.error('no data found.', status);
        });
    };

    implementation.getGames = function () {
      return this.games_ready_to_play;
    };

    implementation.joinGame = function (gameId) {
      // let's go!
      $http.post(serverPrefix + 'quizRun/game/' + gameId + '/joinGame').
        success(function (data, status, headers, config) {
          // response includes category : 'GameJoined' - we're in
          if (data.category === 'GameJoined') {
            $rootScope.$broadcast('GameJoined');
          }
        }).
        error(function (data, status, headers, config) {
          $rootScope.$broadcast('GameNotJoined');
        });
      implementation.gameId = gameId;
    };

    return implementation;

  });

