'use strict';

describe('QuizManagerService', function() {
  beforeEach(module('quizzoApp'));

  // todo - this is really a scenario, not such a unit test...
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
