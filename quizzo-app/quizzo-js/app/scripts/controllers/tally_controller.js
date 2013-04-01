'use strict';

angular.module('quizzoApp').
  controller('TallyCtrl', function($scope, playerAndGameDetailsService) {

  $scope.$on('PlayerChanged', function() {
    $scope.playerNickName = playerAndGameDetailsService.getPlayerNickName();
  });

  $scope.$on('GameChanged', function() {
    $scope.gameTitle = playerAndGameDetailsService.getTitle();
  });

  $scope.$on('ScoreChanged', function() {
    $scope.playerScore = playerAndGameDetailsService.getScore();
  });
});

