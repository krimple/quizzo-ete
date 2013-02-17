'use strict';

angular.module('quizzoApp').
  factory('LeaderBoardService', function() {
  var leaderBoardServiceImpl = {},
  leaders = [
    {
    nickName: 'Joe',
    score: 100
  },
  {
    nickName: 'Sally',
    score: 90
  },
  {
    nickName: 'Jared',
    score: 80
  },
  {
    nickName: 'FooBar',
    score: 70
  },
  {
    nickName: 'Celso',
    score: 10
  }
  ];

  leaderBoardServiceImpl.getTopScorers = function() {
    return leaders;
  };

  return leaderBoardServiceImpl;

});
