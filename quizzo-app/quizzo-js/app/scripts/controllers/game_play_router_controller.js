angular.module('quizzoApp').
    controller('GamePlayRouterCtrl', function ($scope, $location, QuizManagerService) {

      $scope.$on('WhoAmISucceeded', function () {
        var playerAndGameInformation = QuizManagerService.getPlayerAndGameInformation();
        // if we have what we think is a running game, go somewhere to poll

        if (playerAndGameInformation.playerNickName) {
          if (playerAndGameInformation.gameId) {
            $location.path('/play');
          } else {
            $location.path('/show_games');
          }
        } else {
            $location.path('/register');
        }

      });

      $scope.$on('WhoAmIFailed', function () {
        $location.path('/invalid_game_status');
      });

      // fire off the check...
      QuizManagerService.whoAmI();

    });