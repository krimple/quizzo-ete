'use strict';

angular.module('quizzoApp').controller('PlayCtrl', function ($scope, $location, $timeout, quizManagerService) {

  var timeout;

  $scope.selectedChoice = { value : '' };

  $scope.$on('WaitingForAnswer', function () {
    console.log('New question arrived.');
    $scope.question = quizManagerService.getCurrentQuestion();
  });

    // define our 'end of question time' event
  $scope.$on('WaitingForNextQuestion', function () {
    // show panel waiting for next question and begin polling
    $location.path('/question_pending');
  });

  $scope.castVote = function (value) {
    var choice = $scope.selectedChoice;
    console.log("selected choice is", choice);
    if (choice) {
      // we send along the current question # to make sure we are recording
      // the vote on the right question - if it doesn't match the one in
      // server state, throw it away
      quizManagerService.vote(choice, $scope.question.questionNumber);
    }
  };

    // fetch our question
  $scope.question = quizManagerService.getCurrentQuestion();

});


function DialogDemoCtrl($scope, $dialog){

  // Inlined template for demo
  var t = '<div class="modal-header">'+
          '<h1>This is the title</h1>'+
          '</div>'+
          '<div class="modal-body">'+
          '<p>Enter a value to pass to <code>close</code> as the result: <input ng-model="result" /></p>'+
          '</div>'+
          '<div class="modal-footer">'+
          '<button ng-click="close(result)" class="btn btn-primary" >Close</button>'+
          '</div>';

  $scope.opts = {
    backdrop: true,
    keyboard: true,
    backdropClick: true,
    template:  t, // OR: templateUrl: 'path/to/view.html',
    controller: 'TestDialogController'
  };

  $scope.openDialog = function(){
    var d = $dialog.dialog($scope.opts);
    d.open().then(function(result){
      if(result)
      {
        alert('dialog closed with result: ' + result);
      }
    });
  };

  $scope.openMessageBox = function(){
    var title = 'This is a message box';
    var msg = 'This is the content of the message box';
    var btns = [{result:'cancel', label: 'Cancel'}, {result:'ok', label: 'OK', cssClass: 'btn-primary'}];

    $dialog.messageBox(title, msg, btns)
      .open()
      .then(function(result){
        alert('dialog closed with result: ' + result);
    });
  };
}

// the dialog is injected in the specified controller
function TestDialogController($scope, dialog){
  $scope.close = function(result){
    dialog.close(result);
  };
}