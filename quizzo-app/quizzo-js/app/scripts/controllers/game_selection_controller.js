angular.module('quizzoApp').
  controller('GameSelectionCtrl', function ($scope, gameSelectionService) {

    $scope.findGamesReadyToPlay = function() {
      return gameSelectionService.findGamesReadyToPlay();
    };

     // run 'em
    $scope.findGamesReadyToPlay();

    $scope.$on('GamesAvailable', function(event, values) {    	
    	$scope.gamesAvailable = gameSelectionService.getGames();
    });
  });
