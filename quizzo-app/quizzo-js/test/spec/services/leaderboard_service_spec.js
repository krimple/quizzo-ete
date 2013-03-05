'use strict';

describe('LeaderBoardService', function() {
  beforeEach(module('quizzoApp'));

  it('should return top 5 scorers - nickname and score', inject(function(leaderBoardService) {
    var scores = leaderBoardService.getTopScorers();
    expect(scores.length).toBe(5);
    scores.forEach(function(score) {
      expect(score.nickName).toBeTruthy();
      expect(score.score).toBeGreaterThan(-1);
    });
  }));
});
