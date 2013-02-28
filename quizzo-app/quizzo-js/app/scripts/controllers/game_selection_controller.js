angular.module('quizzoApp').
  controller('GameSelectionCtrl', function ($rootScope, $scope, GameSelectionService) {

    $scope.findGamesReadyToPlay = function() {
      return GameSelectionService.findGamesReadyToPlay();
    };

    $rootScope.$on('gamesAvailable', function(event, message) {

    });

    // run 'em
    $scope.findGamesReadyToPlay();

  });
