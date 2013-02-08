'use strict';

function JoinCtrl($rootScope, $scope, $location, nickNameCheck) {
    $scope.join_game = function() {
        console.log("clicked join");
        // bad developer ;)
        $rootScope.userName = $scope.nickname;
        $rootScope.email = $scope.email;
        // do the check here...
        $location.path("/play");
    };

    $scope.verify_nick = function() {
      var answer = nickNameCheck($scope.nickname);
      if (! answer) {
        console.log("Bad nickname", $scope.nickname);
      } else {
        console.log("Good nickname: ", $scope.nickname);
      }
    }
}

function PlayCtrl($scope, $http, $location) {
    console.log("trying to show votes");
    $scope.score = 10;
    $scope.messages = ['one', 'two', 'three'];
    $scope.vote = function($http) {
        console.log("Sending vote...");
    };

    $scope.quit = function() {
      $location.path("/bye");
    };
}

function ByeCtrl($scope) {
  $scope.statistics = {
      "score" : "130",
      "maxScore" : "150",
      "percentCorrect" : "86%",
      "topScorers" : ['phil', 'joe', 'alex']
    };
}
