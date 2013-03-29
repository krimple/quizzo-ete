'use strict';

angular.module('quizzoApp').factory('registerPlayerService', function (serverPrefix, $location, $rootScope, $http) {
  var implementation = {};

  // are we in a call?
  implementation.createPending = false;
  // who is the current player"?
  implementation.currentPlayer = '';

  implementation.createNickName = function (nickName) {
    var that = this;
    this.createPending = true;
    $http.defaults.withCredentials = true;
    $http.post(serverPrefix + 'player/register/' + nickName).
      success(function (data, status, headers, config) {
        if (status === 201) {
          $rootScope.badNick = false;
          that.currentPlayer = nickName;
          $rootScope.playerAndGameInformation
          that.createPending = false;
          $rootScope.$broadcast('GoodNick', { nickName: nickName });
        } else if (status === 204) {
          that.createPending = false;
          that.currentPlayer = '';
          $rootScope.$broadcast('BadNick', { nickName : nickName });
        }
      });
  };

  implementation.getPlayer = function () {
    return this.currentPlayer;
  };

  return implementation;
});
