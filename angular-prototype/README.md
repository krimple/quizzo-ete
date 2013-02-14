This is the UI front end prototype

To install the server:

1. Make sure you've installed node (see `brew install node`)
2. Execute `npm install` - this installs the dependencies - which actually probably will be pre-installed in `node-modules`

To run the server:

1. cd to the `angular-prototype/app` directory (subdirectory under this one)
2. Issue `node server.js` - the server runs on port 4567

To set up the Testactular testing environment:

1. Ensure you have installed node 0.8+ 
2. npm install -g testacular

To run tests:

1. cd angular-prototype/scripts
2. Jasmine-based unit spec tests - run every time something is saved - ./test.sh
3. Angular scenario end to end (e2e) tests - run ./e2e-test.sh - this does not repeat - make sure to start the server first...

Docs:

*  unit Matchers are Jasmine-based:  https://github.com/pivotal/jasmine/wiki/Matchers
*  e2e mocks - include $httpBackend for simulating rear resources - http://docs.angularjs.org/api/ngMock.$httpBackend
*  e2e expectations - use http://docs.angularjs.org/api/angular.element to match elements in the DOM
*  A good e2e overview is here: http://docs.angularjs.org/guide/dev_guide.e2e-testing

