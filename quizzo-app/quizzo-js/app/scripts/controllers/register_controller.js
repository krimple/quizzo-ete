'use strict';

angular.module('quizzoApp').
  controller('RegisterCtrl', function ($rootScope, $location, RegisterPlayerService) {
    $rootScope.showJoinError = false;
    $rootScope.joinError = '';

  $rootScope.$on('BadNick', function(args) {
      $rootScope.showJoinError = true;
      $rootScope.joinError = 'bad nickname -' + args[0] + ' - please try another';
  })
  $rootScope.join_game = function (nickName) {
    RegisterPlayerService.createNickName(nickName);
  };

  $rootScope.clear_nick_bad = function() {
    $rootScope.showJoinError = false;
    $rootScope.joinError = '';
  };
});
