angular.module('quizzoApp').
  controller('GameSelectionCtrl', function ($scope, gameSelectionService) {

    $scope.findGamesReadyToPlay = function() {
      return gameSelectionService.findGamesReadyToPlay();
    };

     // run 'em
    $scope.findGamesReadyToPlay();

    $scope.$on('GamesAvailable', function(event, values) {
    	console.log('Games are available!', gameSelectionService.getGames());
    	$scope.gamesAvailable = gameSelectionService.getGames();
    });
  });
