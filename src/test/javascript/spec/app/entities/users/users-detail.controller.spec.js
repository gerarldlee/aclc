'use strict';

describe('Users Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockUsers, MockPassword_history;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockUsers = jasmine.createSpy('MockUsers');
        MockPassword_history = jasmine.createSpy('MockPassword_history');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Users': MockUsers,
            'Password_history': MockPassword_history
        };
        createController = function() {
            $injector.get('$controller')("UsersDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'aclcrawlerApp:usersUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
