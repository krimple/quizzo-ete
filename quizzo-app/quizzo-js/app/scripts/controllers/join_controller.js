'use strict';

angular.module('quizzoApp').
  controller('JoinCtrl', function ($scope, $location, PlayerService, QuizManagerService) {
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
