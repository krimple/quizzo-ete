'use strict';

/* jasmine specs for controllers go here */


describe('JoinCtrl', function(){
  var myJoinCtrl;


  beforeEach(function(){
    var scope = {};
    var location = {};
    var playerService = function() {
      return {
        getPlayer : function() { return "John"; }
      };

    };
    // TODO - not working myJoinCtrl = new JoinCtrl(scope, location, playerService);
  });


  it('should ....', function() {
    //spec body
  });
});
