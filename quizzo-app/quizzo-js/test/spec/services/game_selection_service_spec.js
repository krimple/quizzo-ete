'use strict';

describe('gameSelectionService', function() {
  var $httpBackend, gameSelectionService;

  // inject the application
  beforeEach(module('quizzoApp'));

  // inject the fake http backend and Quiz manager real service
  beforeEach(inject(function($injector) {
    $httpBackend = $injector.get('$httpBackend');
    gameSelectionService = $injector.get('gameSelectionService');
  }));

  describe('findGamesReadyToPlay units', function($rootScope) {
    it('should return with data when games are ready to play', inject (function($rootScope, gameSelectionService) {

      var success = false, gamesAvailable = [];

      $rootScope.$on('GamesAvailable', function() {
        success = true;
        gamesAvailable = gameSelectionService.getGames();

      });

      runs(function() {
        $httpBackend.when('GET', '/quizzo/quizRun/games').
          respond( [ 
                  { 
          "gameId": "1234123412341234",
          "title": "Zowie!"
        },
        {
          "gameId": "2341234123412341",
          "title": "Zowie Wowie!"
        }]);
        gameSelectionService.findGamesReadyToPlay();
        $httpBackend.flush();
      }, 'findGamesReadyToPlay returns data payload');

      waitsFor(function() {
        if (success === true) {
          expect(gamesAvailable.length).toBe(2);
          return true;
        } else {
          return false;
        }
      }, "message received", 10000);
    }));
  });

  describe('choose game', function() {

    it('should broadcast GameJoined with a valid game id', inject( function($rootScope, gameSelectionService) {
      var success = false;

      $rootScope.$on('GameJoined', function() {
        success = true;
      });

      runs(function() {
        $httpBackend.when('POST', '/quizzo/quizRun/game/1234/joinGame').
          respond( { category: 'GameJoined' });
        gameSelectionService.joinGame('1234');
        $httpBackend.flush();
      }, 'call to join game 1234 succeeds');

      waitsFor(function() {
        return success === true;
      });
    }));
    afterEach(function() {
      $httpBackend.verifyNoOutstandingExpectation();
      $httpBackend.verifyNoOutstandingRequest();
    });

  });
});
