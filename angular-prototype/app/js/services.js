'use strict';

/* Services */
var myAppServices = angular.module('myApp.services', []);

myAppServices.value('version', '0.1');

myAppServices.factory('leaderBoardService', function() {
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

myAppServices.factory('playerService', function () {
  /** stub service implementation */
  var playerServiceImpl = {},
  currentPlayer,
  currentPlayerEmailAddress,
  loggedIn = false;

  /** our service state - stubbed until we use a web service against a live engine */
  playerServiceImpl.nickNameValues = ['dave', 'chuck', 'sal'];
  playerServiceImpl.emailAddressValues = ['foo@bar.baz.com', 'zork@mit.edu'];

  /** returns true if a value exists, false otherwise */
  playerServiceImpl.searchNickName = function (value) {
    return (this.nickNameValues.indexOf(value) != -1);
  };

  playerServiceImpl.searchEmailAddress = function (value) {
    return (this.emailAddressValues.indexOf(value) != -1);
  };

  playerServiceImpl.setPlayerInfo = function (player, email) {
    // in real server this would be implemented in a concurrent way. This is
    // just a stub.
    if (!playerServiceImpl.searchNickName(player) &&
        !playerServiceImpl.searchEmailAddress(email)) {
      // set service state
      this.currentPlayer = player;
      this.currentPlayerEmailAddress = email;
      return true;
    } else {
      return false;
    }
  };

  playerServiceImpl.getPlayer = function() {
    return this.currentPlayer;
  };

  playerServiceImpl.getEmailAddress = function () {
    return this.currentPlayerEmailAddress;
  };

  return playerServiceImpl;
});


myAppServices.factory('quizManagerService', function() {

  var quizManagerServiceImpl = {};

  quizManagerServiceImpl.currentQuizState = 'NOT_STARTED';
  quizManagerServiceImpl.questions = [
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
  quizManagerServiceImpl.currentQuestionIndex = 0;
  quizManagerServiceImpl.currentQuestion = {};
  quizManagerServiceImpl.score = 0;

  quizManagerServiceImpl.getCurrentQuestion = function() {
    var idx = this.currentQuestionIndex;
    console.log('current question index', idx);
    console.log('current question is', this.questions[idx]);
    return this.questions[this.currentQuestionIndex];
  };

  quizManagerServiceImpl.getChoices = function() {
    this.assertProperState('IN_PROGRESS');
    return this.questions[this.currentQuestionIndex].choices;
  };

  quizManagerServiceImpl.answer = function(choiceIdx) {
    var maxChoiceIdx = this.questions[this.currentQuestionIndex].choices.length - 1;

    if (choiceIdx >= 0 && choiceIdx <= maxChoiceIdx) {
      var answerScore = this.questions[this.currentQuestionIndex].choices[choiceIdx].score;
      this.score = this.score + answerScore;
      this.setQuizState('WAIT_NEXT_QUESTION');
    } else {
      throw 'Cannot answer question unless system is ready.';
    }
  };

  quizManagerServiceImpl.nextQuestion = function() {

    this.currentQuestionIndex++;
    if (this.currentQuestionIndex > this.questions.length - 1) {
      this.endQuiz();
      return false;
    } else {
      this.setQuizState('IN_PROGRESS');
      this.currentQuestion = this.questions[this.currentQuestionIndex];
      return true;
    }
  };

  quizManagerServiceImpl.startQuiz = function() {
    // let's go!
    this.setQuizState('IN_PROGRESS');
  };

  quizManagerServiceImpl.endQuiz = function() {
    this.currentQuestion = this.questions.length - 1;
    this.setQuizState('COMPLETE');
  };

  quizManagerServiceImpl.setQuizState = function(newQuizState) {
    // guard state transitions
    if (this.currentQuizState == 'NOT_STARTED') {
      if (!newQuizState == 'IN_PROGRESS' && this.newQuizState == 'COMPLETE') {
        throw 'Cannot move from NOT_STARTED to the state of ' + newQuizState;
      }
    }

    if (this.currentQuizState == 'IN_PROGRESS') {
      if (!newQuizState == 'COMPLETE' && !newQuizState == 'WAIT_NEXT_QUESTION') {
        throw 'Cannot move from IN_PROGRESS to the state of ' + newQuizState;
      }
    }

    if (this.currentQuizState == 'WAIT_NEXT_QUESTION') {
      if (newQuizState != 'IN_PROGRESS' && newQuizState != 'COMPLETE') {
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

  return quizManagerServiceImpl;
});
