'use strict';


// Declare app level module which depends on filters, and services
angular.module('myApp', ['myApp.filters', 'myApp.services', 'myApp.directives']).
  config(['$routeProvider', function($routeProvider) {
    $routeProvider.
        when('/join', {templateUrl: 'partials/join.html', controller: JoinCtrl}).
        when('/play', {templateUrl: 'partials/playerpanel.html', controller: PlayCtrl}).
        when("/bye", {templateUrl: 'partials/bye.html', controller: ByeCtrl}).
        otherwise({redirectTo: '/join'});
  }]);
