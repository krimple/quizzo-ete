angular.module('quizzoApp').
  controller('GameSelectionCtrl', function ($scope, $rootScope, gameSelectionService) {

  
    // putting this in rootScope b/c we are calling from status poller during waiting for game loop
    $rootScope.findGamesReadyToPlay = function() {
      gameSelectionService.findGamesReadyToPlay();
    };

     // run 'em
    $rootScope.findGamesReadyToPlay();

    $scope.$on('GamesAvailable', function(event, values) {    	
    	$scope.gamesAvailable = gameSelectionService.getGames();
    });
  });
