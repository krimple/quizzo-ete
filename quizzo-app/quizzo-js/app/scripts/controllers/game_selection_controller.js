angular.module('quizzoApp').
  controller('GameSelectionCtrl', function ($scope, gameSelectionService) {

    $scope.findGamesReadyToPlay = function() {
      return gameSelectionService.findGamesReadyToPlay();
    };

     // run 'em
    $scope.findGamesReadyToPlay();

  });
