'use strict';

myApp.controller('JoinCtrl', function ($scope, $location, playerService, quizManagerService) {
  $scope.join_game = function (nickName, emailAddress) {
    console.log("clicked join");
    // bad developer ;)
    playerService.setPlayerInfo(nickname, emailAddress);
    // do the check here...
    $location.path("/play");
  };

  $scope.verify_nick = function () {
    console.log("verify nickname - ", $scope.nickname);
    var result = playerService.searchNickName($scope.nickname);
    console.log("verify result - ", result);
    $scope.badNick = result;
  };

  $scope.verify_email = function () {
    console.log("verify email - ", $scope.email);
    var result = playerService.searchEmailAddress($scope.email);
    console.log("verify result - ", result);
    $scope.badEmail = result;
  };
});

myApp.controller('QuestionCtrl', function(quizManagerService) {
  $scope.question = quizManagerService.get
});

myApp.controller('PlayCtrl', function ($scope, $http, $location) {
  console.log("trying to show votes");
  $scope.score = 10;
  $scope.messages = ['one', 'two', 'three'];
  $scope.question = "What is the air speed velocity of a swallow?";
  $scope.top_scores = [134, 130, 112];
  $scope.choices = [
    {
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
    }
  ];

  $scope.vote = function ($http) {
    console.log("Sending vote...");
  };

  $scope.quit = function () {
    $location.path("/bye");
  };
});

myApp.controller('ByeCtrl', function($scope) {
  $scope.statistics = {
    "score": "130",
    "maxScore": "150",
    "percentCorrect": "86%",
    "topScorers": ['phil', 'joe', 'alex']
  };
});
