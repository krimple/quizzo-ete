angular.module('quizzoApp').controller('StatusUpdateCtrl', function ($timeout, $rootScope, quizManagerService) {

  var timeout;

  $rootScope.pollEnabled = false;

  $rootScope.onTimeout = function () {
    // only call poller if enabled
    if ($rootScope.enablePolling === true) {
      console.log('polling enabled - checking status');
      quizManagerService.getStatus();
    }

    console.log('timing out for 1 second...');
    timeout = $timeout($rootScope.onTimeout, 1000);
  };

  $rootScope.enablePolling = function () {
    $rootScope.pollEnabled = true;
  };

  $rootScope.disablePolling = function () {
    $rootScope.pollEnabled = true;
  };

  // begin timing process 
  timeout = $timeout($rootScope.onTimeout, 1000);
});
