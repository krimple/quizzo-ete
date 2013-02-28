'use strict';

angular.module('quizzoApp').
  factory('PlayService', function (serverPrefix, $http) {
    var playServiceImpl = {};

    playServiceImpl.doSomething = function () {
      $http.defaults.withCredentials = true;
      $http.get(serverPrefix + 'status').
        success(function (data, status, headers, config) {
          alert(data);
        }).
      error(function(data, status, headers, config){
          alert("failure. " + data);
        });
    };

    return playServiceImpl;

  });
