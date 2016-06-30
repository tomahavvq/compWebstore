'use strict';

describe('Controller Tests', function() {

    describe('Product Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockProduct, MockCategory;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockProduct = jasmine.createSpy('MockProduct');
            MockCategory = jasmine.createSpy('MockCategory');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Product': MockProduct,
                'Category': MockCategory
            };
            createController = function() {
                $injector.get('$controller')("ProductDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'computerWebstoreApp:productUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
