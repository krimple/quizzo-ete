'use strict';
describe('RegisterPlayerService', function(serverPrefix, $rootScope, $http) {
  var $httpBackend, registerPlayerService;
  beforeEach(module('quizzoApp'));

  // make sure we inject the fake backend
  beforeEach(inject(function($injector) {
    $httpBackend = $injector.get('$httpBackend');
     registerPlayerService = $injector.get('registerPlayerService');
  }));

  describe('create nickname - success', function() {
    it('should create a nickname if not already extant', function() {
      var success = false;

      $httpBackend.when('POST', '/quizzo/player/register/foo').
        respond(201, '');
      registerPlayerService.createNickName('foo');
      $httpBackend.flush();
      expect(registerPlayerService.getPlayer()).toBe('foo');
    });

  });
});
