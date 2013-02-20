'use strict';

angular.module('quizzoApp', []).
  config(['$routeProvider', function ($routeProvider) {
    $routeProvider.
      when('/join', {templateUrl: 'views/join.html', controller: 'JoinCtrl'}).
      when('/play', {templateUrl: 'views/playerpanel.html', controller: 'PlayCtrl'}).
      when('/chat', {templateUrl: 'views/chat.html', controller: 'ChatCtrl'}).
      when('/bye', {templateUrl: 'views/bye.html', controller: 'ByeCtrl'}).
      otherwise({redirectTo: '/join'});
}]);

'use strict';

angular.module('quizzoApp').
  controller('JoinCtrl', function ($scope, $location, PlayerService, QuizManagerService) {
  $scope.join_game = function (nickName, emailAddress) {
    console.log("clicked join");
    // bad developer ;)
    PlayerService.setNickName(nickname);
    QuizManagerService.startQuiz();
    // do the check here...
    $location.path("/play");
  };

  $scope.verify_nick = function () {
    console.log("verify nickname - ", $scope.nickname);
    var result = PlayerService.searchNickName($scope.nickname);
    console.log("verify result - ", result);
    $scope.badNick = result;
  };
});

'use strict';

angular.module('quizzoApp').
 controller('QuestionCtrl', function($scope, QuizManagerService) {

  $scope.question = QuizManagerService.getCurrentQuestion().question;

  $scope.$on('newquestion', function() {
    $scope.question = QuizManagerService.getCurrentQuestion().question;
  });

});

'use strict';

angular.module('quizzoApp').
  controller('TallyCtrl', function() {
});


'use strict';

angular.module('quizzoApp').
  controller('PlayCtrl', function() {
});


'use strict';

angular.module('quizzoApp').controller('VoteCtrl', function($scope, $location, QuizManagerService) {
  $scope.answer_choices = QuizManagerService.getCurrentQuestion().choices;

  $scope.$on('newquestion', function() {
    $scope.answer_choices = QuizManagerService.getCurrentQuestion().choices;
  });


  // this would actually call the service that communicated the vote
  $scope.castVote = function () {
    var selectedChoice = voteForm.choices.item().value;
    if (selectedChoice) {
      console.log("voting"); 
      QuizManagerService.vote(selectedChoice);
      var moreQuestions = QuizManagerService.nextQuestion();
      if (!moreQuestions) {
        $location.path('/bye');
      }
    } else {
      console.log('no answer set');
      // TODO - how to show this? maybe we hide the voting button?
    }
  };
});

angular.module('quizzoApp').
  controller('ByeCtrl', function($scope, QuizManagerService) {
  $scope.statistics = {
    "score": QuizManagerService.getScore(),
    "maxScore": "150"
  };
});

angular.module('quizzoApp').
  controller('ChatCtrl', function ($scope, $http, $timeout) {
    // for remote testing
    //$scope.prefix = 'http://quizzo-ete.com:8080/quizzo/';
    // for local testing
    $scope.prefix = '../../';
    $scope.userName = '';
    $scope.chatContent = '';
    $scope.messageIndex = 0;
    $scope.message = '';
    $scope.keepPolling = false;


    $scope.pollForMessages = function () {
      if ($scope.keepPolling === true) {

        $http({
          method: 'GET',
          url: $scope.prefix + 'mvc/chat',
          params: {'messageIndex': $scope.messageIndex}
        }).success(function (data, status, headers, config) {
            for (var i = 0; i < data.length; i++) {
              // prepend to top (history below, like twitter)
              $scope.chatContent = data[i] + '\n' + $scope.chatContent;
              $scope.messageIndex = $scope.messageIndex + 1;
            }
          }).error(function (data, status, headers, config) {
            //$scope.resetUI();
            console.error("Unable to retrieve chat messages. Chat ended.", status);
          });

      }
      // this is a timer? complete : pollForMessages
      // doubt this will work as is
      $scope.$apply();
      $timeout($scope.pollForMessages, 1000);
    };

    $timeout($scope.pollForMessages, 1000);

    $scope.joinChat = function () {
      if ($scope.userName) {
        $scope.keepPolling = true;
        // start the chain rolling...
      }
    };

    $scope.sendMessage = function () {
      $http({
        method: 'POST',
        url: $scope.prefix + 'mvc/chat',
        params: {'message': $scope.message}
      }).success(function (data, status, headers, config) {

        }).error(function (data, status, headers, config) {
          console.log('failure', data, status, headers, config);
        });
    };


    $scope.leaveChat = function () {
      $scope.keepPolling = false;
      $scope.userName = '';
    }
  });
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

'use strict';

angular.module('quizzoApp').factory('PlayerService', function () {
  /** stub service implementation */
  var playerServiceImpl = {};

  /** our service state - stubbed until we use a web service against a live engine */
  playerServiceImpl.nickNameValues = ['dave', 'chuck', 'sal'];

  /** returns true if a value exists, false otherwise */
  playerServiceImpl.searchNickName = function (value) {
    return (this.nickNameValues.indexOf(value) != -1);
  };

  playerServiceImpl.setNickName = function (nickName) {
    // in real server this would be implemented in a concurrent way. This is
    // just a stub.
    if (!playerServiceImpl.searchNickName(nickName)) {
      // set service state
      this.currentPlayer = nickName;
      return true;
    } else {
      return false;
    }
  };

  playerServiceImpl.getPlayer = function() {
    return this.currentPlayer;
  };

  return playerServiceImpl;
});

'use strict';

angular.module('quizzoApp').factory('QuizManagerService', function($rootScope) {

  var quizManagerServiceImpl = {};
  // stubbed state
  quizManagerServiceImpl.currentQuestionIndex = -1;
  quizManagerServiceImpl.currentQuestion = {};
  quizManagerServiceImpl.score = 0;

  // this will stay - browser state - any initialized or non-answered question has a -1 value...
  // this code line smells worse than an old donkey walking through a bucket of pig swill...
  quizManagerServiceImpl.selectedAnswer = -1;

  // this information is all for stubbing.

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
