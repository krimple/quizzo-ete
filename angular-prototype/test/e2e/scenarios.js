'use strict';

/* http://docs.angularjs.org/guide/dev_guide.e2e-testing */

describe('my app', function() {

  beforeEach(function() {
    browser().navigateTo('index.html');
  });


  describe('join', function() {
    beforeEach(function() {
      browser().navigateTo('#/join');
    });

    it('should render the login panel when browsing to /join', function() {
     input('nickname').value('ken');
    }); 
  });

});
