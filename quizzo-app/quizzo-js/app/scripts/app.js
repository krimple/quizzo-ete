'use strict';

angular.module('quizzoApp', []).
  config(['$routeProvider', function ($routeProvider) {
    $routeProvider.
      when('/join', {templateUrl: 'views/join.html', controller: 'JoinCtrl'}).
      when('/play', {templateUrl: 'views/playerpanel.html', controller: 'PlayCtrl'}).
      when('/chat', {templateUrl: 'views/chat.html', controller: 'ChatCtrl'}).
      when('/bye', {templateUrl: 'views/bye.html', controller: 'ByeCtrl'}).
      otherwise({redirectTo: '/join'});
}]);
