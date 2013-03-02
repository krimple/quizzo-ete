'use strict';

angular.module('quizzoAdminJsApp').
  value('ServerPrefix', 'http://quizzo-ete.com:8080/quizzo/').
  factory('QuizzoAdminService', function($http, $rootScope, ServerPrefix) {
    var implementation = {};
    
    implementation.games = [];
    
    implementation.findGamesReadyToPlay = function () {
      var that = this;
      $http.get(ServerPrefix + 'quizRun/games').success(
        function (data, status, headers, config) {
          that.games = data;
          $rootScope.$broadcast('gamesAvailable');
          $rootScope.status = "call made...  data received";
        }).error(
        function (data, status, headers, config) {
          console.error('no data found. ', status);
        });
    };
    
    implementation.getGames = function () {
      return this.games;
    }
    
    implementation.startGame = function(quizId, title) {
      $http.post(ServerPrefix + "game/" + quizId + "/startGame?title=" + encodeURI(title)).
        success(function (data, status, headers, config){
          $rootScope.$broadcast('gameCreated');
          $rootScope.status = 'game created for quiz ' + quizId + ' with gameId of ' + data.gameId + ' and title of ' + title;
        }).error(function (data, status, headers, config) {
            $rootScope.status = 'game creation failed for quiz ' + quizId;
        });
    };
        
    implementation.beginPlay = function(gameId) {
      $http.post(ServerPrefix + "game/" + gameId + '/beginPlay').
        success(function (data, status, headers, config) {
          $rootScope.status = "game " + gameId + " begins.";
          $rootScope.$apply();
        });
    };
    
    implementation.endQuestion = function(gameId) {
      $http.post(ServerPrefix + "game/" + gameId + '/endQuestion').
        success(function (data, status, headers, config) {
          $rootScope.status = "game " + gameId + " ending current question.";
          $rootScope.$apply();
        });      
    };
    
    implementation.nextQuestion = function(gameId) {
      $http.post(ServerPrefix + "game/" + gameId + '/nextQuestion').
        success(function (data, status, headers, config) {
          $rootScope.status = "game " + gameId + " moving to next question.";
          $rootScope.$apply();
        });      
    };

    implementation.endGame = function(gameId) {
      $http.post(ServerPrefix + "game/" + gameId + '/endGame').
        success(function (data, status, headers, config) {
          $rootScope.status = "game " + gameId + " ending game " + gameId;
          $rootScope.$apply();
        });      
    };
    
    implementation.destroyGame = function(gameId) {
      $http.post(ServerPrefix + "game/" + gameId + '/destroyGame').
        success(function (data, status, headers, config) {
          $rootScope.status = "game " + gameId + " destroying game " + gameId + ".";
          $rootScope.$apply();
        });      
    };
    return implementation;
  });