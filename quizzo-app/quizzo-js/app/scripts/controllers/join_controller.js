'use strict';

angular.module('quizzoApp').
  controller('JoinCtrl', function ($rootScope, $location, PlayerService) {
    $rootScope.showJoinError = false;
    $rootScope.joinError = '';

  $rootScope.$on("badNick", function(args) {
      $rootScope.showJoinError = true;
      $rootScope.joinError = "bad nickname -" + args[0] + " - please try another";
  })
  $rootScope.join_game = function (nickName, emailAddress) {
    PlayerService.createNickName(nickName);
  };

  $rootScope.clear_nick_bad = function() {
    $rootScope.showJoinError = false;
    $rootScope.joinError = "";
  };
});
