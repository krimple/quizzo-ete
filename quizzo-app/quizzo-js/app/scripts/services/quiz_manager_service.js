'use strict';

angular.module('quizzoApp').factory('QuizManagerService', function($http, $rootScope, serverPrefix) {

  var stubImplementation = {};

  var implementation = {};

  implementation.checkReadyToPlay = function(gameId) {
    $http.get(serverPrefix + "status").
      success(function(data, status, headers, config) {
          if (data.status == 'WaitingToPlay') {
            // we just wait... do nothing and return
            return;
          }
          if (data.status == 'WaitingForNextQuestion') {
            $rootScope.$broadcast('WaitingForNextQuestion');
            $rootScope.question.delete();
          }
          if (data.status == 'WaitingForAnswer') {
            $rootScope.question = data.question;
            $rootScope.$broadcast('AnswerQuestion');
          }
        }).
      error(function(data, status, headers, config) {

        });
  }

  // stubbed state
  stubImplementation.currentQuestionIndex = -1;
  stubImplementation.currentQuestion = {};
  stubImplementation.score = 0;

  // this will stay - browser state - any initialized or non-answered question has a -1 value...
  // this code line smells worse than an old donkey walking through a bucket of pig swill...
  stubImplementation.selectedAnswer = -1;

  // this information is all for stubbing.

  stubImplementation.currentQuizState = 'NOT_STARTED';
  stubImplementation.questions = [
    {
    question: 'Who are you?',
    choices: [
      {
      value: 'Sam I am',
      score: 10
    },
    {
      value: 'I am a camera',
      score: -10
    },
    {
      value: 'Foo.bar',
      score: 0
    }
    ]
  },
  {
    question: 'What are you doing here?',
    choices: [
      {
      value: 'Nothing, I just got here on my own.',
      score: 100
    },
    {
      value: 'waiting for you',
      score: 10
    }
    ]
  },
  {
    question: 'What is your name?',
    choices: [
      {
      value: 'Doctor Phil',
      score: -1000
    },
    {
      value: 'Bat Man',
      score: 1000
    }
    ]
  }
  ];

  /////////////////////// BEGIN IMPLEMENTATION METHODS  
  //
  quizManagerServiceImpl.startQuiz = function() {
    // let's go!
    this.currentQuestionIndex = 0;
    this.setQuizState('AWAITING_ANSWER');
  };

  // get the current question payload
  quizManagerServiceImpl.getCurrentQuestion = function() {
    if (this.currentQuestionIndex == -1) {
      throw 'not started.';
    }
    if (this.currentQuestionIndex < this.questions.length) {
      this.setQuizState('AWAITING_ANSWER'); 
      return this.questions[this.currentQuestionIndex];
    } else {
      // todo - what now?
      throw 'no questions left!';
    }
  };


  // scoring is stubbed and will take place on the server
  quizManagerServiceImpl.vote = function(selectedAnswer) {
    if (selectedAnswer > -1) {
      var answerScore = this.getCurrentQuestion().choices[selectedAnswer].score;
      this.score = this.score + answerScore;
      this.setQuizState('WAIT_NEXT_QUESTION');
    } else {
      // todo - what now?
      throw 'Cannot answer question unless system is ready.';
    }
  };

  // will be replaced by subscription to next question message from server
  quizManagerServiceImpl.nextQuestion = function() {
    // if able, move to the next question
    if (this.currentQuestionIndex < (this.questions.length - 1)) {
      this.currentQuestionIndex++;
      this.currentQuestion = this.questions[this.currentQuestionIndex];
      $rootScope.$broadcast('newquestion');
      return true;
    } else {
      this.endQuiz();
      return false;
    }
  };

  // let's call this one a day!
  quizManagerServiceImpl.endQuiz = function() {
    this.currentQuestion = this.questions.length - 1;
    this.setQuizState('COMPLETE');
  };

  // Erzatz state machine - sad programmer...
  quizManagerServiceImpl.setQuizState = function(newQuizState) {
    // guard state transitions
    if (this.currentQuizState == 'NOT_STARTED') {
      if (!newQuizState == 'AWAITING_ANSWER' && this.newQuizState == 'COMPLETE') {
        throw 'Cannot move from NOT_STARTED to the state of ' + newQuizState;
      }
    }

    if (this.currentQuizState == 'AWAITING_ANSWER') {
      if (!newQuizState == 'COMPLETE' && !newQuizState == 'WAIT_NEXT_QUESTION') {
        throw 'Cannot move from AWAITING_ANSWER to the state of ' + newQuizState;
      }
    }

    if (this.currentQuizState == 'WAIT_NEXT_QUESTION') {
      if (newQuizState != 'AWAITING_ANSWER' && newQuizState != 'COMPLETE') {
        throw 'Cannot move from WAIT_NEXT_QUESTION to the state of ' + newQuizState;
      }
    }


    if (this.currentQuizState == 'COMPLETE') {
      throw 'Game over. Cannot change states.';
    }

    this.currentQuizState = newQuizState;
  };

  quizManagerServiceImpl.getScore = function() {
    return this.score;
  };

  quizManagerServiceImpl.startQuiz();
  return quizManagerServiceImpl;
});
