(function() {
    'use strict';

    angular
        .module('computerWebstoreApp')
        .controller('StoreOrderController', StoreOrderController);

    StoreOrderController.$inject = ['$scope', '$state', 'StoreOrder'];

    function StoreOrderController ($scope, $state, StoreOrder) {
        var vm = this;
        
        vm.storeOrders = [];

        loadAll();

        function loadAll() {
            StoreOrder.query(function(result) {
                vm.storeOrders = result;
            });
        }
    }
})();
