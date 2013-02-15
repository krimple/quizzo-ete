'use strict';

// http://www.plnkr.co/edit/FbejSm

/* jasmine specs for services go here */

describe('service', function() {
  beforeEach(module('myApp.services'));

  describe('version', function() {
    it('should return current version', inject(function(version) {
      expect(version).toEqual('0.1');
    }));
  });

  describe('leaderBoardService', function() {

    it('should return top 5 scorers - nickname and score', inject(function(leaderBoardService) {
      var scores = leaderBoardService.getTopScorers();
      expect(scores.length).toBe(5);
      scores.forEach(function(score) {
        expect(score.nickName).toBeTruthy();
        expect(score.score).toBeGreaterThan(-1);
      });
    }));
  });

  describe("playerService", function() {

    it('should find an existing player for dave, chuck, sal', inject(function(playerService) {
      var names = ['dave', 'chuck', 'sal'];
      names.forEach(function(val) {
        expect(playerService.searchNickName(val)).toEqual(true);
      });
    }));

    it('should not find an existing player for phil', inject(function(playerService) {
      expect(playerService.searchNickName('phil')).toEqual(false);
    }));

    it('should return false when using an existing nickname', inject(function(playerService) {
      expect(playerService.getPlayer()).toBeUndefined();
    }));



    it("should set player info when adding unused player information", inject(function(playerService) {
      playerService.setNickName("Joe");
      expect(playerService.getPlayer()).toEqual("Joe");
    }));
  });

  describe('playTheGame', function() {
    it('should ask three questions', inject(function(quizManagerService) {

      var loop;
      quizManagerService.startQuiz();
      for (loop = 0; loop <= 2; loop++) {
        var question = quizManagerService.getCurrentQuestion();
        expect(question).toBeDefined();
        expect(question.choices).toBeDefined();
        expect(question.question).toBeDefined();
        quizManagerService.vote(1);
        quizManagerService.nextQuestion();
      }
      expect(quizManagerService.getScore()).toBeGreaterThan(0);
    }));
  });
});
