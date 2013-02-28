angular.module('quizzoApp').
  controller('GameSelectionCtrl', function ($scope, GameSelectionService) {

    $scope.findGamesReadyToPlay = function() {
      return GameSelectionService.findGamesReadyToPlay();
    };

     // run 'em
    $scope.findGamesReadyToPlay();

  });
