'use strict';

angular.module('quizzoApp').factory('registerPlayerService', function (serverPrefix, $location, $rootScope, $http) {
  var implementation = {};

  // are we in a call?
  implementation.createPending = false;
  // who is the current player"?
  implementation.currentPlayer = '';

  implementation.createNickName = function (nickName) {
    this.createPending = true;
    $http.defaults.withCredentials = true;
    var that = this;
    $http.post(serverPrefix + 'player/register/' + nickName).
      success(function (data, status, headers, config) {
        if (status == 201) {
          $rootScope.badNick = false;
          that.currentPlayer = nickName;
          that.createPending = false;
        } else if (status == 204) {
          that.createPending = false;
          that.currentPlayer = '';
          $rootScope.$broadcast('BadNick', nickName);
        }
      });
  };

  implementation.getPlayer = function () {
    return this.currentPlayer;
  };

  return implementation;
});
