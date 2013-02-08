'use strict';

function JoinCtrl($scope, $location) {
    $scope.join_game = function() {
        console.log("clicked join");
        $location.path("/play");
    };
}

function PlayCtrl($scope, $http) {
    $scope.vote = function($http) {
        console.log("Sending vote...");
    };
}
