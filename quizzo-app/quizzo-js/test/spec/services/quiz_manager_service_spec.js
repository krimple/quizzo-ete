'use strict';

describe('quizManagerService', function() {
  var $httpBackend, quizManagerService, scope;

  // inject the application
  beforeEach(module('quizzoApp'));

  // inject the fake http backend and quiz manager real service
  beforeEach(inject(function($injector) {
    $httpBackend = $injector.get('$httpBackend');
    quizManagerService = $injector.get('quizManagerService');
  }));

  describe('getStatus -Waiting to Play', function() {
    it('should return and make no changes to state', inject( function ($rootScope) {
      $httpBackend.when('GET', '/quizzo/status').
        respond({ status : 'WaitingToPlay' });
      quizManagerService.getStatus();
      $httpBackend.flush();
    }));
  });

  describe('getStatus - Waiting for Answer', function() {
    it('should broadcast WaitingForAnswer and load question payload when status is same', inject( function($rootScope, quizManagerService) {
      var success = false, question = '';

      $rootScope.$on('WaitingForAnswer', function() {
        success = true;
        question = quizManagerService.getCurrentQuestion();
      });

      $httpBackend.when('GET', '/quizzo/status').
        respond({ status : 'WaitingForAnswer', question : 'blah blah blah' });
      quizManagerService.getStatus();
      $httpBackend.flush();

      expect(success).toBe(true);
      expect(question).toBe('blah blah blah');

      afterEach(function() {
        $httpBackend.verifyNoOutstandingExpectation();
        $httpBackend.verifyNoOutstandingRequest();
      });
    }));

    describe('getStatus - Waiting for Next Question', function() {
      it('should broadcast WaitingForNextQuestion when status is same', inject( function($rootScope, quizManagerService) {
        var success = false;

        $rootScope.$on('WaitingForNextQuestion', function() {
          success = true;
        });

        $httpBackend.when('GET', '/quizzo/status').
          respond({ status : 'WaitingForNextQuestion' });

        quizManagerService.getStatus();
        $httpBackend.flush();

        expect(success).toBe(true);

        afterEach(function() {
          $httpBackend.verifyNoOutstandingExpectation();
          $httpBackend.verifyNoOutstandingRequest();
        });
      }));
    });
    describe('getStatus - Waiting for Answer', function() {
      it('should broadcast WaitingForAnswer and load question payload when status is same', inject( function($rootScope, quizManagerService) {
        var success = false, question = '';

        $rootScope.$on('WaitingForAnswer', function() {
          success = true;
          question = quizManagerService.getCurrentQuestion();
        });

        $httpBackend.when('GET', '/quizzo/status').
          respond({ status : 'WaitingForAnswer', question : 'blah blah blah' });

        quizManagerService.getStatus();
        $httpBackend.flush();
        expect(success).toBe(true);
        expect(question).toBe('blah blah blah');

        afterEach(function() {
          $httpBackend.verifyNoOutstandingExpectation();
          $httpBackend.verifyNoOutstandingRequest();
        });
      }));

      describe('getStatus - GameComplete', function() {
        it('should broadcast GameComplete status is same', inject( function($rootScope, quizManagerService) {
          var success = false;

          // precondition
          $rootScope.question = { foo : "bar" };
          $rootScope.$on('GameComplete', function() {
            success = true;
          });

          $httpBackend.when('GET', '/quizzo/status').
            respond({ status : 'GameComplete' });
          quizManagerService.getStatus();
          $httpBackend.flush();

          expect(success).toBe(true);

          afterEach(function() {
            $httpBackend.verifyNoOutstandingExpectation();
            $httpBackend.verifyNoOutstandingRequest();
          });
        }));
      });
    });
  });
});
