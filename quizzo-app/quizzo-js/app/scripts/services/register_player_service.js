'use strict';

angular.module('quizzoApp').factory('RegisterPlayerService',
  function (serverPrefix, $location, $rootScope, $http) {
  var implementation = {};

  implementation.createNickName = function (nickName) {
    $http.defaults.withCredentials = true;
    $http.post(serverPrefix + 'player/register/' + nickName).
      success(function (data, status, headers, config) {
        if (status == 201) {
          $rootScope.badNick = false;
          $location.path('/show_games');
        } else if (status == 204) {
          $rootScope.$broadcast('BadNick', nickName);
        }
      });
  };

  implementation.getPlayer = function () {
    return this.currentPlayer;
  };

  return implementation;
});
