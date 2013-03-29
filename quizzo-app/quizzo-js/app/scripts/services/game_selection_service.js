'use strict';

angular.module('quizzoApp').
  factory('gameSelectionService', function ($http, $rootScope, playerAndGameDetailsService, serverPrefix) {
    var implementation = {};

    // update this when we find data
    implementation.gamesReadyToPlay = [];

    implementation.findGamesReadyToPlay = function () {
      var that = this;
      $http.get(serverPrefix + 'quizRun/games').
        success(function (data, status, headers, config) {
          that.gamesReadyToPlay = data;
          $rootScope.$broadcast('GamesAvailable');
        }).error(function (data, status, headers, config) {
          delete that.gamesReadyToPlay;
          console.error('no data found.', status);
        });
    };

    implementation.getGames = function () {
      var that = this;
      return that.gamesReadyToPlay;
    };

    implementation.joinGame = function (gameId) {
      // let's go!
      $http.post(serverPrefix + 'quizRun/game/' + gameId + '/joinGame').
        success(function (data, status, headers, config) {
          // response includes category : 'GameJoined' - we're in
          if (data.category === 'GameJoined') {
            // update details of joined game and then notify the UI
            playerAndGameDetailsService.setGameIdAndTitle(gameId, data.title);
            $rootScope.$broadcast('GameJoined');
            } else {
              console.log('Potentially incorrect: not a valid game join payload', data);
            }
        }).
        error(function (data, status, headers, config) {
          $rootScope.$broadcast('GameNotJoined');
        });
    };

    return implementation;

  });

