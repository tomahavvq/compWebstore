'use strict';

describe('Controller Tests', function() {

    describe('StoreOrder Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockStoreOrder, MockUser, MockOrderDetails;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockStoreOrder = jasmine.createSpy('MockStoreOrder');
            MockUser = jasmine.createSpy('MockUser');
            MockOrderDetails = jasmine.createSpy('MockOrderDetails');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'StoreOrder': MockStoreOrder,
                'User': MockUser,
                'OrderDetails': MockOrderDetails
            };
            createController = function() {
                $injector.get('$controller')("StoreOrderDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'computerWebstoreApp:storeOrderUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
