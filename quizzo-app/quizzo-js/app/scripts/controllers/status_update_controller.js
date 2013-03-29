'use strict';

// This controller is installed in a hidden div and checks for status updates. 
// Specifically, it acts as a router - it calls a poller service method in the 
// quiz manager, which polls for the current status of the application. 
// This information is served up and then messages are sent to the $rootScope
// which is our message hub. This controller recieves the messages from the
// $rootScope and acts upon them. It is currently set to poll every two seconds,
// and can be replaced with a WebSocket that is notified when events change 
// once Spring supports WebSocket (or the server implementation uses a tool such
// as Atmosphere directly to broadcast).
angular.module('quizzoApp').controller('StatusUpdateCtrl', function ($log, $timeout, $location, $rootScope, quizManagerService) {

  var timeout;

  $rootScope.onTimeout = function () {
    quizManagerService.pollerLoop();
    timeout = $timeout($rootScope.onTimeout, 2000);
  };

  // the messages...

  // We are not known
  $rootScope.$on('RegisterNickName', function () {
    if ($location.path() !== '/register') {
      $location.path('/register');
    }
  });

  // we've logged in, poll for games
  $rootScope.$on('JoinGame', function () {
    if ($location.path() !== '/show_games') {
      $location.path('/show_games');
    } else {
      $rootScope.findGamesReadyToPlay();
    }
  });

  // waiting for others
  $rootScope.$on('WaitingToPlay', function () {
    if ($location.path() !== '/players_pending') {
      $location.path('/players_pending');
    }
  });

  // waiting for question
  $rootScope.$on('WaitingForQuestion', function() {
    if ($location.path() !== '/question_pending') {
      $location.path('/question_pending');
    }
  });

  // submitted question
  $rootScope.$on('VoteSent', function() {
    $location.path('/question_pending');
  });

  // submitted question but vote failed (dupe)
  $rootScope.$on('VoteFailed', function() {
    $location.path('/question_pending');
  });

  // waiting for an answer
  $rootScope.$on('WaitingForAnswer', function() {
    if ($location.path() !== '/play') {
      $location.path('/play');
    }
  });

  // game over, that's all folks

  $rootScope.$on('GameComplete', function() {
    $timeout.cancel();
  });

  // other terminal (currently) states
  $rootScope.$on('WhoAmIFailed', function () {
    $timeout.cancel();
    $log.error('failure in WhoAmI');
    $location.path('/invalid_game_status');
  });

  $rootScope.$on('InvalidGameStatus', function () {
    $timeout.cancel();
    $log.error('failure in game status...');
    $location.path('/invalid_game_status');
  });

  // begin timing process
  timeout = $timeout($rootScope.onTimeout, 2000);
});
