'use strict';

function JoinCtrl($rootScope, $scope, $location, nickNameCheckService, emailAddressCheckService) {
    $scope.join_game = function() {
        console.log("clicked join");
        // bad developer ;)
        $rootScope.userName = $scope.nickname;
        $rootScope.email = $scope.email;
        // do the check here...
        $location.path("/play");
    };

    $scope.verify_nick = function() {
      console.log("verify nickname - ", $scope.nickname);
      var result = nickNameCheckService.search($scope.nickname);
      console.log("verify result - ", result);
      $scope.badNick = result;
    };

    $scope.verify_email = function() {
      console.log("verify email - ", $scope.email);
      var result = emailAddressCheckService.search($scope.email);
      console.log("verify result - ", result);
      $scope.badEmail = result;
    }
}

function PlayCtrl($scope, $http, $location) {
    console.log("trying to show votes");
    $scope.score = 10;
    $scope.messages = ['one', 'two', 'three'];
    $scope.question = "What is the air speed velocity of a swallow?";
    $scope.top_scores = [134, 130, 112];
    $scope.choices = [{
        label: "African or European?",
        selected: false
      },
      {
        label: "What color?",
        selected: false
      },
      {
        label: "I didn't know there was a difference",
        selected: false
      },
      {
        label: "Edam",
        selected: false
      }];

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
