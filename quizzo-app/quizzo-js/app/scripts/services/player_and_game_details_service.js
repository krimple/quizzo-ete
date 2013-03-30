'use strict';

angular.module('quizzoApp').factory('playerAndGameDetailsService', function () {

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
		this.authCheckMade = true;
		this.playerNickName = data.playerNickName;
		this.gameId = data.gameId;
		this.title = data.title;
	};

	implementation.authCheckMade = function() {
		return this.authCheckMade;
	};

	implementation.clearUserData = function() {
		this.authCheckMade = false;
		delete this.playerNickName;
	};

	implementation.clearGameData = function() {
		delete this.gameId;
		delete this.title;
		this.score = 0;
	};

	implementation.setPlayerNickName = function(name) {
		this.playerNickName = name;
	};

	implementation.getPlayerNickName = function() {
		return this.playerNickName;
	};

	implementation.setGameIdAndTitle = function(gameId, title) {
		this.gameId = gameId;
		this.title = title;
	};

	implementation.getGameId = function() {
		return this.gameId;
	};

	implementation.getTitle = function() {
		return this.title;
	};

	implementation.setScore = function(newScore) {
		this.score = newScore;
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
	}

	return implementation;
});