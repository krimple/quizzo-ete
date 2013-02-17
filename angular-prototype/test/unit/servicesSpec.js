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

  describe('LeaderBoardService', function() {

    it('should return top 5 scorers - nickname and score', inject(function(LeaderBoardService) {
      var scores = LeaderBoardService.getTopScorers();
      expect(scores.length).toBe(5);
      scores.forEach(function(score) {
        expect(score.nickName).toBeTruthy();
        expect(score.score).toBeGreaterThan(-1);
      });
    }));
  });

  describe('PlayerService', function() {

    it('should find an existing player for dave, chuck, sal', inject(function(PlayerService) {
      var names = ['dave', 'chuck', 'sal'];
      names.forEach(function(val) {
        expect(PlayerService.searchNickName(val)).toEqual(true);
      });
    }));

    it('should not find an existing player for phil', inject(function(PlayerService) {
      expect(PlayerService.searchNickName('phil')).toEqual(false);
    }));

    it('should return false when using an existing nickname', inject(function(PlayerService) {
      expect(PlayerService.getPlayer()).toBeUndefined();
    }));



    it('should set player info when adding unused player information', inject(function(PlayerService) {
      PlayerService.setNickName('Joe');
      expect(PlayerService.getPlayer()).toEqual('Joe');
    }));
  });

  describe('playTheGame', function() {
    it('should ask three questions', inject(function(QuizManagerService) {

      var loop;
      QuizManagerService.startQuiz();
      for (loop = 0; loop <= 2; loop++) {
        var question = QuizManagerService.getCurrentQuestion();
        console.log('question', question);
        expect(question).toBeDefined();
        expect(question.choices).toBeDefined();
        expect(question.question).toBeDefined();
        QuizManagerService.vote(1);
        QuizManagerService.nextQuestion();
      }
      expect(QuizManagerService.getScore()).toBeGreaterThan(0);
    }));
  });
});
