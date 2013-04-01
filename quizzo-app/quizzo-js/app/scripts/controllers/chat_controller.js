'use strict';

angular.module('quizzoApp').
  controller('ChatCtrl',
             function ($scope, chatService) {

  // TODO - move primary data into service? Probably not.
  $scope.chatContent = 'type something';

  // Whether or not to enable chat input box
  $scope.isChatDisabled = function () {
    chatService.isChatDisabled();
  };

  $scope.sendChatMessage = function (message) {
    chatService.sendMessage(message);
    $scope.chatMessage = '';
  };

  // async call loop - make sure we only send another chat call
  // when we get notified of data recieved...
  $scope.$on('ChatPollFinished', function() {
    // todo - optimize this? Maybe only if new?
    $scope.chatContent = chatService.getChatMessages();
    chatService.messagePoll();

  });

  // in case we refreshed the browser
  chatService.resetChatMessageIndex();

  // start poll...
  chatService.messagePoll();

});
