'use strict';

describe('Password_history Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockPassword_history, MockUsers;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockPassword_history = jasmine.createSpy('MockPassword_history');
        MockUsers = jasmine.createSpy('MockUsers');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Password_history': MockPassword_history,
            'Users': MockUsers
        };
        createController = function() {
            $injector.get('$controller')("Password_historyDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'aclcrawlerApp:password_historyUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
