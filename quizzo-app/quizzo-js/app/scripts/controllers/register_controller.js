'use strict';

angular.module('quizzoApp').controller('RegisterCtrl', function ($rootScope, $location, registerPlayerService) {
  $rootScope.showJoinError = false;
  $rootScope.joinError = '';

  $rootScope.$on('BadNick', function (event, values) {
    $rootScope.showJoinError = true;
    $rootScope.joinError = 'bad nickname -' + values.nickName + ' - please try another';
  });

  $rootScope.$on('GoodNick', function (event, values) {
    console.log('Good nickname', values.nickName);
    $rootScope.player = registerPlayerService.getPlayer();
    $location.path('/show_games');
  });

  $rootScope.join_game = function (nickName) {
    registerPlayerService.createNickName(nickName);
  };

  $rootScope.clear_nick_bad = function () {
    $rootScope.showJoinError = false;
    $rootScope.joinError = '';
  };
});
