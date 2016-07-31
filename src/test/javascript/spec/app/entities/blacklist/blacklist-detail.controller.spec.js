'use strict';

describe('Blacklist Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockBlacklist;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockBlacklist = jasmine.createSpy('MockBlacklist');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Blacklist': MockBlacklist
        };
        createController = function() {
            $injector.get('$controller')("BlacklistDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'aclcrawlerApp:blacklistUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
