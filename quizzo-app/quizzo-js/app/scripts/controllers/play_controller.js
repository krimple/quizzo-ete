'use strict';

angular.module('quizzoApp').controller('PlayCtrl', function ($scope, PlayService) {
    $scope.playground = function () {
      PlayService.doSomething();
    };
  });