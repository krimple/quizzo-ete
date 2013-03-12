'use strict';

angular.module('quizzoApp').controller('QuestionPendingCtrl', function ($scope, $location) {

  // our exit condition...
  $scope.$on('WaitingForAnswer', function () {
    $location.path('/play');
  });

});
