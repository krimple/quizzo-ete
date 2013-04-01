'use strict';

angular.module('quizzoApp').factory('playerAndGameDetailsService', function ($rootScope) {

	var implementation = {};

	// start with a respectable score :)
	implementation.score = 0;
	implementation.authCheckMade = false;

	implementation.previousStatus = 'NoStatus';

	implementation.hasPlayerNickName = function() {
		return this.playerNickName !== undefined &&
           this.playerNickName !== null;
	};

	implementation.hasGameInformation = function() {
		return this.gameId !== undefined &&
		       this.gameId !== null;
	};

	implementation.adaptWhoAmIData = function(data) {
    // if we went to a new game, reset our score...
    // will be sent by the server soon enough to fix
    if (this.gameId !== data.gameId) {
		  this.gameId = data.gameId;
		  this.title = data.title;
      this.score = 0;
      this.notifyScoreChanged();
      this.notifyGameChanged();
      this.notifyPlayerChanged();
    }

		this.authCheckMade = true;
    if (this.playerNickName !== data.playerNickName) {
        this.playerNickName = data.playerNickName;
        this.notifyPlayerChanged();
    }
	};

  implementation.notifyScoreChanged = function() {
    $rootScope.$broadcast('ScoreChanged');
  };

  implementation.notifyPlayerChanged = function() {
    $rootScope.$broadcast('PlayerChanged');
  };

  implementation.notifyGameChanged = function() {
    $rootScope.$broadcast('GameChanged');
  };

	implementation.authCheckMade = function() {
		return this.authCheckMade;
	};

	implementation.clearUserData = function() {
		this.authCheckMade = false;
		delete this.playerNickName;
    this.notifyPlayerChanged();
	};

	implementation.clearGameData = function() {
		delete this.gameId;
		delete this.title;
		this.score = 0;
    this.notifyGameChanged();
	};

	implementation.setPlayerNickName = function(name) {
		this.playerNickName = name;
    this.notifyPlayerChanged();
	};

	implementation.getPlayerNickName = function() {
		return this.playerNickName;
	};

	implementation.setGameIdAndTitle = function(gameId, title) {
		this.gameId = gameId;
		this.title = title;
    this.notifyGameChanged();
	};

	implementation.getGameId = function() {
    return this.gameId;
	};

	implementation.getTitle = function() {
		return this.title;
	};

	implementation.setScore = function(newScore) {
    if (this.score !== newScore) {
  		this.score = newScore;
      this.notifyScoreChanged();
    }
	};

  implementation.getScore = function() {
    return this.score;
  };

	implementation.setQuestion = function(question) {
		this.question = question;
	};

	implementation.getQuestion = function() {
		return this.question;
	};

	implementation.setQuestionAnswer = function(answer) {
		this.question.answer = answer;
	};

	implementation.getQuestionAnswer = function() {
		if (this.question.answer === undefined) {
			return '';
		} else {
			return this.question.answer;
		}
	};

	return implementation;
});
