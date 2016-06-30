'use strict';

describe('Controller Tests', function() {

    describe('UserInfo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockUserInfo, MockUser, MockAddress;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockUserInfo = jasmine.createSpy('MockUserInfo');
            MockUser = jasmine.createSpy('MockUser');
            MockAddress = jasmine.createSpy('MockAddress');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'UserInfo': MockUserInfo,
                'User': MockUser,
                'Address': MockAddress
            };
            createController = function() {
                $injector.get('$controller')("UserInfoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'computerWebstoreApp:userInfoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
