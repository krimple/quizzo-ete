'use strict';

myApp.controller('JoinCtrl', function ($scope, $location, PlayerService, QuizManagerService) {
  $scope.join_game = function (nickName, emailAddress) {
    console.log("clicked join");
    // bad developer ;)
    PlayerService.setNickName(nickname);
    QuizManagerService.startQuiz();
    // do the check here...
    $location.path("/play");
  };

  $scope.verify_nick = function () {
    console.log("verify nickname - ", $scope.nickname);
    var result = PlayerService.searchNickName($scope.nickname);
    console.log("verify result - ", result);
    $scope.badNick = result;
  };
});

myApp.controller('TallyCtrl', function() {
});

myApp.controller('QuestionCtrl', function($scope, QuizManagerService) {

  $scope.question = QuizManagerService.getCurrentQuestion().question;

  $scope.$on('newquestion', function() {
    $scope.question = QuizManagerService.getCurrentQuestion().question;
  });

});

myApp.controller('VoteCtrl', function($scope, $location, QuizManagerService) {

  $scope.answer_choices = QuizManagerService.getCurrentQuestion().choices;

  $scope.$on('newquestion', function() {
    $scope.answer_choices = QuizManagerService.getCurrentQuestion().choices;
  });
 

  // this would actually call the service that communicated the vote
  $scope.castVote = function () {
    var selectedChoice = voteForm.choices.item().value;
    if (selectedChoice) {
      console.log("voting"); 
      QuizManagerService.vote(selectedChoice);
      var moreQuestions = QuizManagerService.nextQuestion();
      if (!moreQuestions) {
        $location.path('/bye');
      }
    } else {
      console.log('no answer set');
      // TODO - how to show this? maybe we hide the voting button?
    }
  };
});

myApp.controller('PlayCtrl', function ($scope, $http, $location) {
  $scope.score = 10;
  $scope.top_scores = [134, 130, 112];
});

myApp.controller('ByeCtrl', function($scope, QuizManagerService) {
  $scope.statistics = {
    "score": QuizManagerService.getScore(),
    "maxScore": "150"
  };
});
