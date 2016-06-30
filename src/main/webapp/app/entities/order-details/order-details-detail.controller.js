(function() {
    'use strict';

    angular
        .module('computerWebstoreApp')
        .controller('OrderDetailsDetailController', OrderDetailsDetailController);

    OrderDetailsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'OrderDetails', 'StoreOrder', 'Product'];

    function OrderDetailsDetailController($scope, $rootScope, $stateParams, entity, OrderDetails, StoreOrder, Product) {
        var vm = this;

        vm.orderDetails = entity;

        var unsubscribe = $rootScope.$on('computerWebstoreApp:orderDetailsUpdate', function(event, result) {
            vm.orderDetails = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
