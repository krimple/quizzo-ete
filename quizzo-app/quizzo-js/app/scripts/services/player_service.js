'use strict';

angular.module('quizzoApp').factory('PlayerService', function () {
  /** stub service implementation */
  var playerServiceImpl = {};

  /** our service state - stubbed until we use a web service against a live engine */
  playerServiceImpl.nickNameValues = ['dave', 'chuck', 'sal'];

  /** returns true if a value exists, false otherwise */
  playerServiceImpl.searchNickName = function (value) {
    return (this.nickNameValues.indexOf(value) != -1);
  };

  playerServiceImpl.setNickName = function (nickName) {
    // in real server this would be implemented in a concurrent way. This is
    // just a stub.
    if (!playerServiceImpl.searchNickName(nickName)) {
      // set service state
      this.currentPlayer = nickName;
      return true;
    } else {
      return false;
    }
  };

  playerServiceImpl.getPlayer = function() {
    return this.currentPlayer;
  };

  return playerServiceImpl;
});
