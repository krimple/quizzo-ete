'use strict';

angular.module('quizzoAdminJsApp')
  .controller('MainCtrl', function ($scope, QuizzoAdminService) {
    
    $scope.quizId = 'JavascriptQuiz';
    $scope.title = 'The Big Javascript Fight';
    
    // startup... watch for new games...
    $scope.$on("gamesAvailable", function() {
      $scope.gamesAvailable = QuizzoAdminService.getGames();
    });
    
    // load up the list...
    QuizzoAdminService.findGamesReadyToPlay();
    
    $scope.fetchGames = function() {
      QuizzoAdminService.findGamesReadyToPlay();
    }
    
    $scope.startGame = function(quizId, title) {
      QuizzoAdminService.startGame(quizId, title);      
    }
    
    $scope.beginPlay = function(gameId) {
      QuizzoAdminService.beginPlay(gameId);
    }
  });
