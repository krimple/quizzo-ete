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

    it('should find existing emails for foo@bar.baz.com and zork.mit.edu', inject(function(playerService) {
      var emails = ['foo@bar.baz.com', 'zork@mit.edu'];
      emails.forEach(function(val) {
        expect(playerService.searchEmailAddress(val)).toEqual(true);
      });
    }));


    it("should set player info when adding unused player information", inject(function(playerService) {
      playerService.setPlayerInfo("Joe", "Joe@test.com");
      expect(playerService.getPlayer()).toEqual("Joe");
      expect(playerService.getEmailAddress()).toEqual("Joe@test.com");
    }));
  });

  describe('quizManagerService', function() {

    it('should present the next question when started', inject(function(quizManagerService) {
      quizManagerService.startQuiz();
      expect(quizManagerService.getCurrentQuestion().question).toEqual('Who are you?');
    }));

    describe('playTheGame', function() {
      it('should ask three questions', inject(function(quizManagerService) {

        var loop;
        quizManagerService.startQuiz();
        for (loop = 0; loop <= 2; loop++) {
          var question = quizManagerService.getCurrentQuestion();
          expect(question).toBeDefined();
          expect(question.choices).toBeDefined();
          expect(question.question).toBeDefined();
          quizManagerService.answer(1);
          quizManagerService.nextQuestion();
        }
        expect(quizManagerService.getScore()).toBeGreaterThan(0);
      }));
    });
  });
});
