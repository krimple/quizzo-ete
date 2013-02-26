'use strict';

angular.module('quizzoApp').factory('PlayerService', function (serverPrefix, $location, $rootScope, $http) {
  /** stub service implementation */
  var playerServiceImpl = {};


  playerServiceImpl.createNickName = function (nickName) {

    $http.post(serverPrefix + 'player/register/' + nickName).
        success(function(data, status, headers, config) {
            if (status == 201) {
                $rootScope.badNick = false;
                $location.path("/play");
            } else if (status == 204) {
                $rootScope.$broadcast("badNick", nickName);
            }
        });
  };

  playerServiceImpl.getPlayer = function() {
    return this.currentPlayer;
  };

  return playerServiceImpl;
});
