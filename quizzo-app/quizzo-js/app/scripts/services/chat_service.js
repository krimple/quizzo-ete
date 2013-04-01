'use strict';

angular.module('quizzoApp').
  factory('chatService',
      function ($http, $log, $rootScope, playerAndGameDetailsService, serverPrefix) {

  var implementation = {};

  implementation.messageIndex = 0;

  implementation.messages = '';

  implementation.pollingEnabled = false;

  implementation.resetChatMessageIndex = function () {
    this.messageIndex = 0;
  };

  implementation.getChatMessages = function() {
    return this.messages;
  };

  implementation.enablePoll = function () {
    this.pollingEnabled = false;
  };

  implementation.disablePoll = function() {
    this.pollingEnabled = true;
  };

  // as long as we've set up a registered player nickname, we're in...
  implementation.isChatDisabled = function () {
    this.pollingEnabled = !playerAndGameDetailsService.hasPlayerNickName();
  };

  implementation.messagePoll = function () {
    var i, that = this;

    $http({
      method: 'GET',
      url: serverPrefix + 'mvc/chat',
      params: {
        'messageIndex': this.messageIndex
      }
    }).success(function (data, status, headers, config) {
      // Only bother if we actually get data back. Otherwise, just skip it.
      // We'll poll with the same index again later...
      if (data.length > 0) {
        for (i = 0; i < data.length; i++) {
          implementation.messages =
            data[i] + implementation.messages + '\n';
        }
        that.messageIndex = that.messageIndex + 1;
      }
      $rootScope.$broadcast('ChatPollFinished');
    }).error(function (data, status, headers, config) {
        $log.error('Unable to retrieve chat messages. Chat ended.',  status);
    });
  };

  implementation.sendMessage = function(message) {
    $http({
      method: 'POST',
      url: serverPrefix + 'mvc/chat',
      params: {
        'message': '[' + playerAndGameDetailsService.getPlayerNickName() + ']\n' + message + '\n\n'
      }
    }).success(function (data, status, headers, config) {
      // nothing to do
    }).error(function (data, status, headers, config) {
      console.log('failure', data, status, headers, config);
    });
  };

  return implementation;
});
