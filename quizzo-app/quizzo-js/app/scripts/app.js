'use strict';

angular.module('quizzoApp', []).
  config(['$routeProvider', '$httpProvider', function ($routeProvider, $httpProvider) {
    $routeProvider.
      when('/register', {templateUrl: 'views/assign_player.html', controller: 'RegisterCtrl'}).
      when('/join_game', {templateUrl: 'views/join_game.html', controller: 'GameSelectionCtrl'}).
      when('/game_pending/:gameId', {templateUrl: 'views/game_pending.html', controller: 'JoinGameCtrl'}).
      when('/players_pending/:gameId', {templateUrl: 'views/players_pending.html', controller: 'PlayersPendingCtrl'}).
      when('/play/:gameId', {templateUrl: 'views/playerpanel.html', controller: 'QuestionCtrl'}).
      when('/chat', {templateUrl: 'views/chat.html', controller: 'ChatCtrl'}).
      when('/bye', {templateUrl: 'views/bye.html', controller: 'ByeCtrl'}).
      otherwise({redirectTo: '/register'});
    // allow http headers for session cookie management
    $httpProvider.defaults.withCredentials = true;
  }]);
