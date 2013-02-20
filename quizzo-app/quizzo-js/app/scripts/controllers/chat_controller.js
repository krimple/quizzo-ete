angular.module('quizzoApp').
  controller('ChatCtrl', function ($scope, $http, $timeout) {
    // for remote testing
    $scope.prefix = 'http://quizzo-ete.com:8080/quizzo/';
    // for local testing
    // $scope.prefix = '../../
    $scope.userName = '';
    $scope.chatContent = '';
    $scope.messageIndex = 0;
    $scope.message = '';
    $scope.keepPolling = false;


    $scope.pollForMessages = function () {
      if ($scope.keepPolling === true) {

        $http({
          method: 'GET',
          url: $scope.prefix + 'mvc/chat',
          params: {'messageIndex': $scope.messageIndex}
        }).success(function (data, status, headers, config) {
            for (var i = 0; i < data.length; i++) {
              // prepend to top (history below, like twitter)
              $scope.chatContent = data[i] + '\n' + $scope.chatContent;
              $scope.messageIndex = $scope.messageIndex + 1;
            }
          }).error(function (data, status, headers, config) {
            //$scope.resetUI();
            console.error("Unable to retrieve chat messages. Chat ended.", status);
          });

      }
      // this is a timer? complete : pollForMessages
      // doubt this will work as is
      $scope.$apply();
      $timeout($scope.pollForMessages, 1000);
    };

    $timeout($scope.pollForMessages, 1000);

    $scope.joinChat = function () {
      if ($scope.userName) {
        $scope.keepPolling = true;
        // start the chain rolling...
      }
    };

    $scope.sendMessage = function () {
      $http({
        method: 'POST',
        url: $scope.prefix + 'mvc/chat',
        params: {'message': $scope.message}
      }).success(function (data, status, headers, config) {

        }).error(function (data, status, headers, config) {
          console.log('failure', data, status, headers, config);
        });
    };


    $scope.leaveChat = function () {
      $scope.keepPolling = false;
      $scope.userName = '';
    }
  });