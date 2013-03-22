'use strict';

angular.module('quizzoApp', ['ui.bootstrap']).
  config(['$routeProvider', '$httpProvider', function ($routeProvider, $httpProvider) {
    $routeProvider.
      when('/register', {templateUrl: 'views/assign_player.html', controller: 'RegisterCtrl'}).
      when('/show_games', {templateUrl: 'views/show_games.html', controller: 'GameSelectionCtrl'}).
      when('/join_game/:gameId', {templateUrl: 'views/joining_game.html', controller: 'JoinGameCtrl'}).
      when('/players_pending', {templateUrl: 'views/players_pending.html', controller: 'PlayersPendingCtrl'}).
      when('/question_pending', {templateUrl: 'views/question_pending.html', controller: 'QuestionPendingCtrl'}).
      when('/play', {templateUrl: 'views/playerpanel.html', controller: 'PlayCtrl'}).
      when('/chat', {templateUrl: 'views/chat.html', controller: 'ChatCtrl'}).
      when('/bye', {templateUrl: 'views/bye.html', controller: 'ByeCtrl'}).
      when('invalid_game_status', {templateUrl: 'views/invalid_game_status.html'}).
      when('/determine_current_session_state', {controller: 'DetermineCurrentSessionState'}).
      otherwise({redirectTo: '/determine_current_session_state'});
    // allow http headers for session cookie management
    $httpProvider.defaults.withCredentials = true;
  }]);
