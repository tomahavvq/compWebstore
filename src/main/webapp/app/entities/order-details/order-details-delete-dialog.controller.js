(function() {
    'use strict';

    angular
        .module('computerWebstoreApp')
        .controller('OrderDetailsDeleteController',OrderDetailsDeleteController);

    OrderDetailsDeleteController.$inject = ['$uibModalInstance', 'entity', 'OrderDetails'];

    function OrderDetailsDeleteController($uibModalInstance, entity, OrderDetails) {
        var vm = this;

        vm.orderDetails = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OrderDetails.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
