'use strict';

angular.module('quizzoApp').
  controller('TallyCtrl', function($scope, $rootScope, playerAndGameDetailsService) {

  $rootScope.$on('PlayerChanged', function() {
    $scope.playerNickName = playerAndGameDetailsService.getPlayerNickName();
  });

  $rootScope.$on('GameChanged', function() {
    $scope.gameTitle = playerAndGameDetailsService.getTitle();
  });

  $rootScope.$on('ScoreChanged', function() {
    $scope.playerScore = playerAndGameDetailsService.getScore();
  });
});

