'use strict';

describe('Controller Tests', function() {

    describe('OrderDetails Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockOrderDetails, MockStoreOrder, MockProduct;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockOrderDetails = jasmine.createSpy('MockOrderDetails');
            MockStoreOrder = jasmine.createSpy('MockStoreOrder');
            MockProduct = jasmine.createSpy('MockProduct');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'OrderDetails': MockOrderDetails,
                'StoreOrder': MockStoreOrder,
                'Product': MockProduct
            };
            createController = function() {
                $injector.get('$controller')("OrderDetailsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'computerWebstoreApp:orderDetailsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
