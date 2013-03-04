'use strict';

describe('QuizManagerService', function() {
  var $httpBackend, QuizManagerService, scope;

  // inject the application
  beforeEach(module('quizzoApp'));

  // inject the fake http backend and Quiz manager real service
  beforeEach(inject(function($injector) {
    $httpBackend = $injector.get('$httpBackend');
    QuizManagerService = $injector.get('QuizManagerService');
  }));

  describe('getStatus -Waiting to Play', function() {
    it('should return and make no changes to state', inject( function ($rootScope) {
      $httpBackend.when('GET', '/quizzo/status').
        respond({ status : 'WaitingToPlay' });
      QuizManagerService.getStatus();
      $httpBackend.flush();
    }));
  });

  describe('getStatus - Waiting for Answer', function() {
    it('should broadcast WaitingForAnswer and load question payload when status is same', inject( function($rootScope, QuizManagerService) {
      var success = false, question = '';

      $rootScope.$on('WaitingForAnswer', function() {
        success = true;
        question = QuizManagerService.getCurrentQuestion();
      });

      runs(function() {
        $httpBackend.when('GET', '/quizzo/status').
          respond({ status : 'WaitingForAnswer', question : 'blah blah blah' });
        QuizManagerService.getStatus();
        $httpBackend.flush();
      }, 'call backend service and return result asynch.');

      waitsFor(function() {
        return success === true && question === 'blah blah blah';
      }, "message received", 10000);
    }));

    afterEach(function() {
      $httpBackend.verifyNoOutstandingExpectation();
      $httpBackend.verifyNoOutstandingRequest();
    });
  });

  describe('getStatus - Waiting for Next Question', function() {
    it('should broadcast WaitingForNextQuestion when status is same', inject( function($rootScope, QuizManagerService) {
      var success = false;

      $rootScope.$on('WaitingForNextQuestion', function() {
        success = true;
      });

      runs(function() {
        $httpBackend.when('GET', '/quizzo/status').
          respond({ status : 'WaitingForNextQuestion' });
        QuizManagerService.getStatus();
        $httpBackend.flush();
      }, 'call backend service and return result asynch.');

      waitsFor(function() {
        return success === true;
      }, "message received", 10000);

    }));

    afterEach(function() {
      $httpBackend.verifyNoOutstandingExpectation();
      $httpBackend.verifyNoOutstandingRequest();
    });
  });
  describe('getStatus - Waiting for Answer', function() {
    it('should broadcast WaitingForAnswer and load question payload when status is same', inject( function($rootScope, QuizManagerService) {
      var success = false, question = '';

      $rootScope.$on('WaitingForAnswer', function() {
        success = true;
        question = QuizManagerService.getCurrentQuestion();
      });

      runs(function() {
        $httpBackend.when('GET', '/quizzo/status').
          respond({ status : 'WaitingForAnswer', question : 'blah blah blah' });
        QuizManagerService.getStatus();
        $httpBackend.flush();
      }, 'call backend service and return result asynch.');

      waitsFor(function() {
        return success === true && question === 'blah blah blah';
      }, "message received", 10000);
    }));

    afterEach(function() {
      $httpBackend.verifyNoOutstandingExpectation();
      $httpBackend.verifyNoOutstandingRequest();
    });
  });

  describe('getStatus - GameComplete', function() {
    it('should broadcast GameComplete status is same', inject( function($rootScope, QuizManagerService) {
      var success = false;

      // precondition
      $rootScope.question = { foo : "bar" };
      $rootScope.$on('GameComplete', function() {
        success = true;
      });

      runs(function() {
        $httpBackend.when('GET', '/quizzo/status').
          respond({ status : 'GameComplete' });
        QuizManagerService.getStatus();
        $httpBackend.flush();
      }, 'call backend service and return result asynch.');

      waitsFor(function() {
        return success === true;
      }, "message received", 10000);
    }));

    afterEach(function() {
      $httpBackend.verifyNoOutstandingExpectation();
      $httpBackend.verifyNoOutstandingRequest();
    });
  });
});
