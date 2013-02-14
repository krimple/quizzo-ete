'use strict';

myApp.controller('JoinCtrl', function ($scope, $location, playerService, quizManagerService) {
  $scope.join_game = function (nickName, emailAddress) {
    console.log("clicked join");
    // bad developer ;)
    playerService.setNickName(nickname);
    // do the check here...
    $location.path("/play");
  };

  $scope.verify_nick = function () {
    console.log("verify nickname - ", $scope.nickname);
    var result = playerService.searchNickName($scope.nickname);
    console.log("verify result - ", result);
    $scope.badNick = result;
  };
});

myApp.controller('QuestionCtrl', function($scope, quizManagerService) {

  $scope.question = quizManagerService.getCurrentQuestion();

  $scope.setAnswer = function() {
    // we'll cache it in the local session state
    quizManagerService.setAnswer(choiceNumber);
  };

  // this would actually call the service that communicated the vote
  $scope.castVote = function () {
    if (quizManagerService.answerRecorded()) {
      quizManagerService.vote();
    } else {
      console.log('no answer set');
      // TODO - how to show this? maybe we hide the voting button?
    }
  };
});

/** deprecated */
myApp.controller('PlayCtrl', function ($scope, $http, $location) {
  console.log("trying to show votes");
  $scope.score = 10;
  $scope.top_scores = [134, 130, 112];

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
